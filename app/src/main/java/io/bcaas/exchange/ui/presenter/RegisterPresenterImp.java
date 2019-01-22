package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.RegisterContract;
import io.bcaas.exchange.ui.interactor.LoginInteractor;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.bcaas.exchange.bean.VerificationBean;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.security.NoSuchAlgorithmException;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 注册
 */
public class RegisterPresenterImp extends BasePresenterImp implements RegisterContract.Presenter {
    private String TAG = RegisterPresenterImp.class.getSimpleName();
    private RegisterContract.View view;
    private LoginInteractor loginInteractor;

    private Disposable disposableRegister, disposableVerifyAccount;

    public RegisterPresenterImp(RegisterContract.View view) {
        super();
        this.view = view;
        loginInteractor = new LoginInteractor();
    }


    @Override
    public void register(String memberId, String password, String verifyCode) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableRegister);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        try {
            memberVO.setPassword(Sha256Tool.doubleSha256ToString(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        requestJson.setMemberVO(memberVO);
        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setVerifyCode(verifyCode);
        requestJson.setVerificationBean(verificationBean);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "register", requestJson);

        loginInteractor.register(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableRegister = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        LogTool.d(TAG, responseJson);
                        if (responseJson == null) {
                            view.registerFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.registerSuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2006) {
                                view.registerFailure(MessageConstants.ERROR_EMAIL_ALREADY_REGISTER);
                            } else {
                                view.registerFailure(responseJson.getMessage());

                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.registerFailure(e.getMessage());
                        disposeDisposable(disposableRegister);

                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableRegister);
                    }
                });
    }


    @Override
    public void verifyAccount(String memberId) {
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

        loginInteractor.verifyAccount(GsonTool.beanToRequestBody(requestJson))
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
                        if (code == MessageConstants.CODE_2005) {
                            //代表当前账号是没有被注册过的
                            BaseApplication.setMemberID(memberId);
                            view.verifyAccountSuccess(responseJson.getMessage());
                        } else {
                            view.verifyAccountFailure(getString(R.string.email_already_be_register));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.verifyAccountFailure(getString(R.string.register_failure_please_try_again));
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
