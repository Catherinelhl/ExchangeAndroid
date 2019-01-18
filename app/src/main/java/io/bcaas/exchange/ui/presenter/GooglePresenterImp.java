package io.bcaas.exchange.ui.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.GoogleContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
import io.bcaas.exchange.vo.LoginInfoVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/9
 * Google 验证相关
 */
public class GooglePresenterImp extends BasePresenterImp implements GoogleContract.Presenter {

    private String TAG = GooglePresenterImp.class.getSimpleName();
    private GoogleContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;

    public GooglePresenterImp(GoogleContract.View view) {
        super();
        this.view = view;
        this.safetyCenterInteractor = new SafetyCenterInteractor();
    }

    @Override
    public void getAuthenticatorUrl() {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setMemberVO(memberVO);
        requestJson.setLoginInfoVO(loginInfoVO);
        LogTool.d(TAG, requestJson);
        safetyCenterInteractor.getAuthenticatorUrl(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        LogTool.d(TAG, responseJson);
                        if (responseJson == null) {
                            view.getAuthenticatorUrlFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            VerificationBean verificationBean = responseJson.getVerificationBean();
                            if (verificationBean == null) {
                                view.getAuthenticatorUrlFailure(responseJson.getMessage());
                            } else {
                                view.getAuthenticatorUrlSuccess(verificationBean);
                                //判断当前URL是否为空
                                String getAuthenticatorUrl = verificationBean.getAuthenticatorUrl();
                                if (StringTool.notEmpty(getAuthenticatorUrl)) {
                                    getAuthenticatorUrlCreateImage(verificationBean.getAuthenticatorUrl());
                                } else {
                                    view.getAuthenticatorUrlFailure(getString(R.string.get_data_failure));
                                }
                            }
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                view.getAuthenticatorUrlFailure(responseJson.getMessage());
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.getAuthenticatorUrlFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void securityGoogleAuthenticator(String verifyCode) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setVerifyCode(verifyCode);
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setMemberVO(memberVO);
        requestJson.setLoginInfoVO(loginInfoVO);
        requestJson.setVerificationBean(verificationBean);
        LogTool.d(TAG, requestJson);
        safetyCenterInteractor.securityTwoFactorVerify(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        LogTool.d(TAG, responseJson);
                        if (responseJson == null) {
                            view.securityGoogleAuthenticatorFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            // 获取当前的验证状态
                            MemberVO memberVOResponse = responseJson.getMemberVO();
                            if (memberVOResponse != null) {
                                int twoFactorAuthVerify = memberVOResponse.getTwoFactorAuthVerify();
                                if (twoFactorAuthVerify == Constants.Status.OPEN) {
                                    view.securityGoogleAuthenticatorSuccess(responseJson.getMessage());
                                } else {
                                    view.securityGoogleAuthenticatorFailure(responseJson.getMessage());
                                }

                            } else {
                                view.securityGoogleAuthenticatorFailure(MessageConstants.EMPTY);

                            }
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                view.securityGoogleAuthenticatorFailure(responseJson.getMessage());
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.securityGoogleAuthenticatorFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }

    @Override
    public void getAuthenticatorUrlCreateImage(String url) {
        //得到当前创建url的地址，重新请求网络获取图片
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        //显示加载框
        view.showLoading();
        safetyCenterInteractor.getAuthenticatorUrlCreateImage(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ResponseBody::byteStream)
                .map(BitmapFactory::decodeStream)
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        view.getAuthenticatorImageSuccess(bitmap);

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        view.getAuthenticatorImageFailure();

                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });

    }
}
