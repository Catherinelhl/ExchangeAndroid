package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BasePresenterImp;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.app.PreferenceTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.ForgetToResetPasswordContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
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
 * @since 2019/1/10
 * <p>
 * 数据交互实现类：忘记而重设密码
 */
public class ForgetToResetPasswordPresenterImp extends BasePresenterImp
        implements ForgetToResetPasswordContract.Presenter {
    private String TAG = ForgetToResetPasswordPresenterImp.class.getSimpleName();

    private ForgetToResetPasswordContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;
    private Disposable disposableForgetPassword;

    public ForgetToResetPasswordPresenterImp(ForgetToResetPasswordContract.View view) {
        super();
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();

    }

    @Override
    public void forgetPassword(String password, String verifyCode) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableForgetPassword);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        String memberId = BaseApplication.getMemberID();
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
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "forgetPassword:", requestJson);
        safetyCenterInteractor.forgetPassword(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableForgetPassword = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "forgetPassword:", requestJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                           PreferenceTool.getInstance().saveString(Constants.Preference.MEMBER_ID, memberId);
                            //存储当前账号
                            BaseApplication.setMemberID(BaseApplication.getMemberID());
                            view.forgetPasswordSuccess(MessageConstants.EMPTY);
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2046
                                        || code == MessageConstants.CODE_2010) {
                                    //    {"success":false,"code":2046,"message":"Verify mail code fail."}
                                    view.forgetPasswordFailure(getString(R.string.verify_mail_code_fail));

                                } else if (code == MessageConstants.CODE_2067) {
                                    view.forgetPasswordFailure(getString(R.string.verify_code_format_invalid));
                                } else if (code == MessageConstants.CODE_2025
                                        || code == MessageConstants.CODE_2048) {
                                    view.forgetPasswordFailure(getString(R.string.verify_code_expire));
                                } else if (code == MessageConstants.CODE_2030) {
                                    //"New password is same as current password."
                                    view.forgetPasswordFailure(getString(R.string.login_and_fund_password_not_consistent));
                                } else if (code == MessageConstants.CODE_2029) {
                                    view.forgetPasswordFailure(getString(R.string.new_and_old_password_not_consistent));
                                } else {
                                    view.forgetPasswordFailure(getString(R.string.failure_to_reset_password));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.forgetPasswordFailure(getString(R.string.failure_to_reset_password));
                        disposeDisposable(disposableForgetPassword);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableForgetPassword);
                    }
                });
    }
}
