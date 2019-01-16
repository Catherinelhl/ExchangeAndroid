package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.SetFundPasswordContract;
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
 * @since 2019/1/2
 * 「设置资金密码」
 */
public class SetFundPasswordPresenterImp implements SetFundPasswordContract.Presenter {
    private String TAG = SetFundPasswordPresenterImp.class.getSimpleName();
    private SetFundPasswordContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;

    public SetFundPasswordPresenterImp(SetFundPasswordContract.View view) {
        super();
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();
    }

    /**
     * 设置当前的资金密码
     *
     * @param password
     */
    @Override
    public void securityTxPassword(String password) {
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
            memberVO.setTxPassword(Sha256Tool.doubleSha256ToString(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "securityTxPassword", requestJson);
        safetyCenterInteractor.securityTxPassword(GsonTool.beanToRequestBody(requestJson))
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
                            view.securityTxPasswordFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.securityTxPasswordSuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
                            view.httpException(responseJson);
                            //{"success":false,"code":2022,"message":"TxPassword is the same as the account password."}
                            view.securityTxPasswordFailure(responseJson.getMessage());

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.securityTxPasswordFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });

    }
}
