package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.LoginContract;
import io.bcaas.exchange.ui.interactor.LoginInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.security.NoSuchAlgorithmException;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public class LoginPresenterImp implements LoginContract.Presenter {
    private String TAG = LoginPresenterImp.class.getSimpleName();
    private LoginContract.View view;
    private LoginInteractor loginInteractor;

    private Disposable disposableLogin, disposableImageVerifyCode;

    public LoginPresenterImp(LoginContract.View view) {
        super();
        this.view = view;
        loginInteractor = new LoginInteractor();
    }

    @Override
    public void login(String memberId, String password, String verifyCode) {
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
        LogTool.d(TAG, requestJson);
        loginInteractor.login(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableLogin = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        LogTool.d(TAG, responseJson);
                        if (responseJson == null) {
                            view.loginFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            LoginInfoVO loginInfoVO = responseJson.getLoginInfoVO();
                            String accessToken = loginInfoVO.getAccessToken();
                            LogTool.d(TAG, accessToken);
                            BaseApplication.setAccessToken(accessToken);
                            view.loginSuccess(accessToken);
                        } else {
                            // {"success":false,"code":2005,"message":"Email not register."}
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2005) {
                                view.loginFailure(MessageConstants.ERROR_EMAIL_NOT_REGISTER);
                            } else {
                                view.loginFailure(responseJson.getMessage());
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.loginFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
