package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.SafetyCenterContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
import io.bcaas.exchange.vo.LoginInfoVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 数据交互实现类：「安全中心」
 */
public class SafetyCenterPresenterImp extends AccountSecurityPresenterImp implements SafetyCenterContract.Presenter {
    private String TAG = SafetyCenterPresenterImp.class.getSimpleName();
    private SafetyCenterContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;
    private Disposable disposableLogout, disposableSecurityPhone,
            disposableSecurityEmail, disposableSecurityGoogle;

    private CompositeDisposable compositeDisposable;

    public SafetyCenterPresenterImp(SafetyCenterContract.View view) {
        super(view);
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void logout(String memberId) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableLogout);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setMemberVO(memberVO);
        requestJson.setLoginInfoVO(loginInfoVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "logout:", requestJson);
        safetyCenterInteractor.logout(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableLogout = d;
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "logout:", responseJson);

                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.logoutSuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2019
                                    || code == MessageConstants.CODE_2016
                                    || code == MessageConstants.CODE_2018) {
                                //    {"success":false,"code":2019,"message":"AccessToken expire."}
                                view.logoutSuccess(responseJson.getMessage());
                            } else {
                                view.logoutSuccess(responseJson.getMessage());

                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.logoutSuccess(e.getMessage());
                        disposeDisposable(disposableLogout);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableLogout);

                    }
                });
    }

    @Override
    public void securityPhone(String phone, String verifyCode) {
        disposeDisposable(disposableSecurityPhone);
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        memberVO.setPhone(phone);
        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setVerifyCode(verifyCode);
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setMemberVO(memberVO);
        requestJson.setLoginInfoVO(loginInfoVO);
        requestJson.setVerificationBean(verificationBean);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "securityPhone", requestJson);
        safetyCenterInteractor.securityPhone(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSecurityPhone = d;
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "securityPhone:", responseJson);

                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.securityPhoneSuccess(responseJson.getMessage());
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                view.securityPhoneFailure(getString(R.string.failure_to_security_phone));
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.securityPhoneFailure(getString(R.string.failure_to_security_phone));
                        disposeDisposable(disposableSecurityPhone);
                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableSecurityPhone);
                    }
                });
    }

    @Override
    public void securityEmail(String verifyCode) {
        disposeDisposable(disposableSecurityEmail);
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
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "securityEmail:", requestJson);
        safetyCenterInteractor.securityEmail(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSecurityEmail = d;
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "securityEmail:", responseJson);

                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.securityPhoneSuccess(responseJson.getMessage());
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2021) {
                                    view.securityPhoneFailure(getString(R.string.send_mail_code_fail));
                                } else {
                                    view.securityPhoneFailure(getString(R.string.failure_to_security_email));
                                }

                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.securityPhoneFailure(getString(R.string.failure_to_security_email));
                        disposeDisposable(disposableSecurityEmail);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableSecurityEmail);


                    }
                });
    }

    @Override
    public void securityGoogle(String verifyCode) {
        disposeDisposable(disposableSecurityGoogle);
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
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "securityGoogle:", requestJson);
        safetyCenterInteractor.securityTwoFactorVerify(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSecurityGoogle = d;
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "securityGoogle:", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.securityPhoneSuccess(responseJson.getMessage());
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                view.securityPhoneFailure(getString(R.string.failure_to_security_google));
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.securityPhoneFailure(getString(R.string.failure_to_security_google));
                        disposeDisposable(disposableSecurityGoogle);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableSecurityGoogle);


                    }
                });
    }

    @Override
    public void cancelSubscribe() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        disposeDisposable(disposableSecurityGoogle);
        disposeDisposable(disposableSecurityEmail);
        disposeDisposable(disposableSecurityPhone);
        disposeDisposable(disposableLogout);
    }
}
