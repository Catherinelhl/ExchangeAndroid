package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.WithDrawContract;
import io.bcaas.exchange.ui.interactor.TxInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.security.NoSuchAlgorithmException;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * <p>
 * 提现
 */
public class WithDrawPresenterImp extends AccountSecurityPresenterImp
        implements WithDrawContract.Presenter {

    private String TAG = WithDrawPresenterImp.class.getSimpleName();
    private WithDrawContract.View view;
    private TxInteractor txInteractor;
    private Disposable disposableWithDraw;

    public WithDrawPresenterImp(WithDrawContract.View view) {
        super(view);
        this.view = view;
        txInteractor = new TxInteractor();
    }

    @Override
    public void withDraw(String txPassword, RequestJson requestJson) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableWithDraw);
        //显示加载框
        view.showLoading();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        try {
            memberVO.setTxPassword(Sha256Tool.doubleSha256ToString(txPassword));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        requestJson.setMemberVO(memberVO);

        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "withDraw:", requestJson);
        txInteractor.withDraw(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableWithDraw = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "withDraw:", responseJson);

                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.withDrawSuccess(responseJson.getMessage());
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                //判断当前是否是地址错误
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_4001
                                        || code == MessageConstants.CODE_5001
                                        || code == MessageConstants.CODE_3000
                                        || code == MessageConstants.CODE_3004) {
                                    //地址格式错误
                                    view.withDrawFailure(getString(R.string.address_format_error));
                                } else if (code == MessageConstants.CODE_2046) {
                                    //    {"success":false,"code":2046,"message":"Verify mail code fail."}
                                    view.withDrawFailure(getString(R.string.verify_mail_code_fail));
                                } else if (code == MessageConstants.CODE_2047) {
                                    //{"success":false,"code":2047,"message":"Verify phone code fail."}
                                    view.withDrawFailure(getString(R.string.verify_phone_code_fail));
                                } else if (code == MessageConstants.CODE_2045) {
                                    //  {"success":false,"code":2045,"message":"Authenticator verify fail."}
                                    view.withDrawFailure(getString(R.string.google_authenticator_verify_fail));
                                } else {
                                    view.withDrawFailure(getString(R.string.failure_to_withdraw_please_try_again));
                                }
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.withDrawFailure(getString(R.string.failure_to_withdraw_please_try_again));
                        disposeDisposable(disposableWithDraw);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableWithDraw);
                    }
                });
    }
}
