package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.ForgetPasswordContract;
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
 * 忘记密码
 */
public class ForgetPasswordPresenterImp implements ForgetPasswordContract.Presenter {
    private String TAG = ForgetPasswordPresenterImp.class.getSimpleName();

    private ForgetPasswordContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;

    public ForgetPasswordPresenterImp(ForgetPasswordContract.View view) {
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
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        try {
            memberVO.setPassword(Sha256Tool.doubleSha256ToString(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        requestJson.setMemberVO(memberVO);

        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setVerifyCode(verifyCode);
        requestJson.setVerificationBean(verificationBean);

        LogTool.d(TAG, requestJson);
        safetyCenterInteractor.forgetPassword(GsonTool.beanToRequestBody(requestJson))
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
                            view.forgetPasswordFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.forgetPasswordSuccess(MessageConstants.EMPTY);
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                view.forgetPasswordFailure(responseJson.getMessage());
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.forgetPasswordFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }
}
