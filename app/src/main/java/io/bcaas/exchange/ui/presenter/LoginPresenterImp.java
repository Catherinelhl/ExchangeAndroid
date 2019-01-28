package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.LoginContract;
import io.bcaas.exchange.ui.interactor.LoginInteractor;
import io.bcaas.exchange.vo.LoginInfoVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.security.NoSuchAlgorithmException;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public class LoginPresenterImp extends BasePresenterImp implements LoginContract.Presenter {
    private String TAG = LoginPresenterImp.class.getSimpleName();
    private LoginContract.View view;
    private LoginInteractor loginInteractor;

    private Disposable disposableLogin;

    public LoginPresenterImp(LoginContract.View view) {
        super();
        this.view = view;
        loginInteractor = new LoginInteractor();
    }

    @Override
    public void login(String memberId, String password, String verifyCode) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableLogin);
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
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "login:", requestJson);
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
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "login:", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            LoginInfoVO loginInfoVO = responseJson.getLoginInfoVO();
                            String accessToken = loginInfoVO.getAccessToken();
                            //存储当前的token
                            BaseApplication.setAccessToken(accessToken);
                            //存储当前的memberID
                            BaseApplication.setMemberID(memberId);
                            BaseApplication.setStringToSP(Constants.Preference.MEMBER_ID, memberId);
                            //将成功状态返回界面
                            view.loginSuccess(accessToken);
                        } else {
                            // {"success":false,"code":2005,"message":"Email not register."}
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2005) {
                                view.loginFailure(getString(R.string.email_not_register));
                            } else if (code == MessageConstants.CODE_2010) {
                                //1：如果当前图形验证码错误，那么返回的是2010，包括输入上一次的code
                                //  {"success":false,"code":2010,"message":"Verify code fail."}
                                view.ImageVerifyCodeError(getString(R.string.verify_code_fail));
                            } else if (code == MessageConstants.CODE_2067) {
                                view.loginFailure(getString(R.string.verify_code_format_invalid));
                            } else if (code == MessageConstants.CODE_2025) {
                                //    {"success":false,"code":2025,"message":"Verify code expire."}
                                view.ImageVerifyCodeError(getString(R.string.verify_code_expire));
                            } else if (code == MessageConstants.CODE_2015) {
                                // {"success":false,"code":2015,"message":"Current password is wrong."}
                                view.loginFailure(getString(R.string.current_password_is_wrong));

                            } else {
                                view.loginFailure(getString(R.string.login_failure_please_try_again));
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.loginFailure(getString(R.string.login_failure_please_try_again));
                        disposeDisposable(disposableLogin);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableLogin);

                    }
                });
    }

}
