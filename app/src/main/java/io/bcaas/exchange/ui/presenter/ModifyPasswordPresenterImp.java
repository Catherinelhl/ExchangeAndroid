package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.ModifyPasswordContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
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
 * @since 2019/1/10
 * <p>
 * 修改密码
 */
public class ModifyPasswordPresenterImp extends BasePresenterImp
        implements ModifyPasswordContract.Presenter {
    private String TAG = ModifyPasswordPresenterImp.class.getSimpleName();

    private ModifyPasswordContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;
    private Disposable disposableResetPassword;

    public ModifyPasswordPresenterImp(ModifyPasswordContract.View view) {
        super();
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();

    }

    @Override
    public void resetPassword(String password, String newPassword) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableResetPassword);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        try {
            memberVO.setPassword(Sha256Tool.doubleSha256ToString(password));
            memberVO.setNewPassword(Sha256Tool.doubleSha256ToString(newPassword));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "resetPassword:", requestJson);
        safetyCenterInteractor.resetPassword(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableResetPassword = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "resetPassword:", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.resetPasswordSuccess(responseJson.getMessage());
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2030) {
                                    //"New password is same as current password."
                                    view.resetPasswordFailure(getString(R.string.login_and_fund_password_not_consistent));
                                } else if (code == MessageConstants.CODE_2029) {
                                    view.resetPasswordFailure(getString(R.string.new_and_old_password_not_consistent));
                                } else {
                                    view.resetPasswordFailure(getString(R.string.failure_to_reset_password));
                                }
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.resetPasswordFailure(getString(R.string.failure_to_reset_password));
                        disposeDisposable(disposableResetPassword);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableResetPassword);

                    }
                });
    }
}
