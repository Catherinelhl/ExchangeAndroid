package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.ResetPasswordContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
import io.bcaas.exchange.vo.LoginInfoVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * <p>
 * 重设密码
 */
public class ResetPasswordPresenterImp implements ResetPasswordContract.Presenter {
    private String TAG = ResetPasswordPresenterImp.class.getSimpleName();

    private ResetPasswordContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;

    public ResetPasswordPresenterImp(ResetPasswordContract.View view) {
        super();
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();

    }

    @Override
    public void resetPassword(String password, String newPassword) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        memberVO.setPassword(password);
        memberVO.setNewPassword(newPassword);
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setMemberVO(memberVO);
        requestJson.setLoginInfoVO(loginInfoVO);
        LogTool.d(TAG, requestJson);
        safetyCenterInteractor.resetPassword(GsonTool.beanToRequestBody(requestJson))
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
                            view.resetPasswordFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            VerificationBean verificationBean = responseJson.getVerificationBean();
                            if (verificationBean == null) {
                                view.resetPasswordFailure(MessageConstants.EMPTY);
                            } else {
                                view.resetPasswordSuccess(MessageConstants.EMPTY);
                            }
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2019) {
                                //    {"success":false,"code":2019,"message":"AccessToken expire."}
                                view.resetPasswordFailure(responseJson.getMessage());
                            } else {
                                view.resetPasswordFailure(MessageConstants.EMPTY);

                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.resetPasswordFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
