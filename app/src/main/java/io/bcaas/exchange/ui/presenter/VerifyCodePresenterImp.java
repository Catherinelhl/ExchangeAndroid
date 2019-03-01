package io.bcaas.exchange.ui.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BasePresenterImp;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.device.NetWorkTool;
import io.bcaas.exchange.ui.contracts.VerifyCodeContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
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
 * @since 2018/12/21
 * <p>
 * 数据交互实现类： 验证码的获取
 */
public class VerifyCodePresenterImp extends BasePresenterImp
        implements VerifyCodeContract.Presenter {
    private String TAG = VerifyCodePresenterImp.class.getSimpleName();
    private VerifyCodeContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;

    private Disposable disposableImageVerifyCode, disposableEmailVerify, disposableVerifyAccount;

    public VerifyCodePresenterImp(VerifyCodeContract.View view) {
        super();
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();

    }

    @Override
    public void emailVerify(String memberId, String languageCode, String mail) {
        disposeDisposable(disposableEmailVerify);
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        requestJson.setMemberVO(memberVO);
        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setLanguageCode(languageCode);
        verificationBean.setMail(mail);
        requestJson.setVerificationBean(verificationBean);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "emailVerify:", requestJson);
        safetyCenterInteractor.emailVerify(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        disposableEmailVerify = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "emailVerify:", requestJson);
                        if (responseJson == null) {
                            view.getEmailVerifyFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.getEmailVerifySuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2021) {
                                view.getEmailVerifyFailure(getString(R.string.send_mail_code_fail));

                            } else {
                                view.getEmailVerifyFailure(responseJson.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getEmailVerifyFailure(e.getMessage());
                        disposeDisposable(disposableEmailVerify);
                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableEmailVerify);

                    }
                });
    }

    @Override
    public void getImageVerifyCode() {
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableImageVerifyCode);
        safetyCenterInteractor.getImageVerifyCode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ResponseBody::byteStream)
                .map(BitmapFactory::decodeStream)
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableImageVerifyCode = d;

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (bitmap == null) {
                            view.getImageVerifyCodeFailure(MessageConstants.EMPTY);
                            return;
                        }
                        view.getImageVerifyCodeSuccess(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        //如果当前是属于请求超时的状态
                        if (NetWorkTool.connectTimeOut(e)) {
                            getImageVerifyCode();
                        } else {
                            view.getImageVerifyCodeFailure(e.getMessage());
                            disposeDisposable(disposableImageVerifyCode);
                        }
                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableImageVerifyCode);

                    }
                });
    }

    @Override
    public void verifyAccount(String memberId, String from) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableVerifyAccount);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        requestJson.setMemberVO(memberVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "verifyAccount:", requestJson);

        safetyCenterInteractor.verifyAccount(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableVerifyAccount = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "verifyAccount:", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        // {"success":false,"code":2005,"message":"Email not register."}
                        int code = responseJson.getCode();
                        //如果当前from==注册，那么检测2005，如果==忘记密码，那么检测200
                        if (StringTool.equals(from, Constants.EditTextFrom.REGISTER_VERIFY_EMAIL)) {
                            if (code == MessageConstants.CODE_2005) {
                                //代表当前账号是没有被注册过的
                                BaseApplication.setMemberID(memberId);
                                view.verifyAccountSuccess(responseJson.getMessage());
                            } else {
                                view.verifyAccountFailure(getString(R.string.email_already_be_register));
                            }
                        } else {
                            if (code == MessageConstants.CODE_200) {
                                //代表当前账号是没有被注册过的
                                BaseApplication.setMemberID(memberId);
                                view.verifyAccountSuccess(responseJson.getMessage());
                            } else {
                                view.verifyAccountFailure(getString(R.string.email_not_register));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        if (StringTool.equals(from, Constants.EditTextFrom.REGISTER_VERIFY_EMAIL)) {
                            view.verifyAccountFailure(getString(R.string.register_failure_please_try_again));
                        } else {
                            view.verifyAccountFailure(getString(R.string.failure_to_reset_password));
                        }
                        disposeDisposable(disposableVerifyAccount);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableVerifyAccount);
                    }
                });
    }
}
