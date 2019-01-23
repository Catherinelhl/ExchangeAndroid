package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.CloseVerifyCodeContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
import io.bcaas.exchange.vo.LoginInfoVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/8
 */
public class CloseVerifyPresenterImp
        extends AccountSecurityPresenterImp
        implements CloseVerifyCodeContract.Presenter {

    private String TAG = CloseVerifyPresenterImp.class.getSimpleName();
    private CloseVerifyCodeContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;
    private Disposable disposableCloseVerifyCode;

    public CloseVerifyPresenterImp(CloseVerifyCodeContract.View view) {
        super(view);
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();
    }

    /**
     * 可一次验证多组验证码,验证成功后会关闭该项
     * 安全验证功能,依照verificationBean中是否有帶
     * closeType做判断依据：
     * <p>
     * type：
     * "0“：邮箱
     * "1“：手机
     * "2“：双因素
     * <p>
     * closeType：
     * "1"：關閉
     *
     * @param verificationBeans
     */
    @Override
    public void closeVerifyCode(List<VerificationBean> verificationBeans) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableCloseVerifyCode);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        requestJson.setVerificationBeanList(verificationBeans);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "closeVerifyCode", requestJson);
        safetyCenterInteractor.closeSecurityVerify(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableCloseVerifyCode = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "closeVerifyCode", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.closeVerifyCodeSuccess(responseJson.getMessage());
                        } else {

                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2046) {
                                    //    {"success":false,"code":2046,"message":"Verify mail code fail."}
                                    view.closeVerifyCodeFailure(getString(R.string.verify_mail_code_fail));
                                } else if (code == MessageConstants.CODE_2047) {
                                    //{"success":false,"code":2047,"message":"Verify phone code fail."}
                                    view.closeVerifyCodeFailure(getString(R.string.verify_phone_code_fail));
                                } else if (code == MessageConstants.CODE_2045) {
                                    //  {"success":false,"code":2045,"message":"Authenticator verify fail."}
                                    view.closeVerifyCodeFailure(getString(R.string.google_authenticator_verify_fail));
                                } else if (code == MessageConstants.CODE_2010) {
                                    view.closeVerifyCodeFailure(getString(R.string.verify_code_fail));
                                } else if (code == MessageConstants.CODE_2025) {
                                    view.closeVerifyCodeFailure(getString(R.string.verify_code_expire));
                                } else {
                                    view.closeVerifyCodeFailure(getString(R.string.failure_to_close_verify_please_try_again));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.closeVerifyCodeFailure(getString(R.string.failure_to_close_verify_please_try_again));
                        disposeDisposable(disposableCloseVerifyCode);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableCloseVerifyCode);

                    }
                });
    }
}
