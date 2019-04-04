package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.BuyContract;
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
 * 数据交互实现类：购买
 */
public class BuyPresenterImp extends AccountSecurityPresenterImp implements BuyContract.Presenter {

    private String TAG = BuyPresenterImp.class.getSimpleName();
    private BuyContract.View view;
    private TxInteractor txInteractor;
    private Disposable disposableBuy;

    public BuyPresenterImp(BuyContract.View view) {
        super(view);
        this.view = view;
        txInteractor = new TxInteractor();
    }

    @Override
    public void buy(String txPassword, long memberOderUid, String verifyCode) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableBuy);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
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

        MemberOrderVO memberOrderVO = new MemberOrderVO();
        memberOrderVO.setMemberOrderUid(memberOderUid);
        requestJson.setMemberOrderVO(memberOrderVO);

        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setVerifyCode(verifyCode);
        requestJson.setVerificationBean(verificationBean);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "buy", requestJson);
        txInteractor.buy(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableBuy = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        LogTool.d(TAG, responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.buySuccess(responseJson.getMessage());
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2057) {
                                    //  {"success":false,"code":2057,"message":"Illegal request."}
                                    view.buySelfError();
                                } else if (code == MessageConstants.CODE_2015) {
                                    view.buyFailure(getString(R.string.fund_password_is_wrong));
                                } else if (code == MessageConstants.CODE_2055) {
                                    //{"success":false,"code":2055,"message":"Invalid order information."}
                                    view.invalidBuyOrder(getString(R.string.invalid_order_information));
                                } else if (code == MessageConstants.CODE_2066) {
                                    //    {"success":false,"code":2066,"message":"Insufficient balances."}
                                    view.buyFailure(getString(R.string.no_enough_balance));
                                } else if (code == MessageConstants.CODE_2026) {
                                    view.buyFailure(getString(R.string.order_type_invalid));

                                } else if (code == MessageConstants.CODE_2032) {
                                    view.buyFailure(getString(R.string.order_type_error));

                                } else if (code == MessageConstants.CODE_2058) {
                                    view.buyFailure(getString(R.string.no_enough_balance));

                                } else if (code == MessageConstants.CODE_2059) {
                                    view.buyFailure(getString(R.string.balance_and_account_compare_failure));
                                } else {
                                    view.buyFailure(getString(R.string.failure_to_buy_please_try_again));

                                }
                            }


                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.buyFailure(getString(R.string.failure_to_buy_please_try_again));
                        disposeDisposable(disposableBuy);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableBuy);
                    }
                });
    }

    @Override
    public void cancelSubscribe() {
        disposeDisposable(disposableBuy);
    }
}
