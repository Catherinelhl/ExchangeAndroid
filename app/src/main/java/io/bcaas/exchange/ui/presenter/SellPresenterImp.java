package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.SellContract;
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
 * 数据交互实现类：出售
 */
public class SellPresenterImp extends AccountSecurityPresenterImp
        implements SellContract.Presenter {
    private String TAG = SellPresenterImp.class.getSimpleName();

    private SellContract.View view;
    private TxInteractor txInteractor;
    private Disposable disposableSell;

    public SellPresenterImp(SellContract.View view) {
        super(view);
        this.view = view;
        this.txInteractor = new TxInteractor();
    }


    /**
     * 售出
     *
     * @param currencyUid        卖出的币种
     * @param paymentCurrencyUid 买方支付的币种
     * @param amount             金额
     * @param unitPrice          单价
     * @param txPassword         资金密码
     * @param verifyCode         验证码
     */
    @Override
    public void sell(String currencyUid, String paymentCurrencyUid, String amount, String unitPrice, String txPassword, String verifyCode) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableSell);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        memberVO.setTwoFactorAuthSecret(BaseApplication.getTwoFactorAuthSecret());
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
        CurrencyListVO currencyListVO = new CurrencyListVO();
        currencyListVO.setCurrencyUid(currencyUid);
        CurrencyListVO currencyListVOPayment = new CurrencyListVO();
        currencyListVOPayment.setCurrencyUid(paymentCurrencyUid);
        memberOrderVO.setCurrencyListVO(currencyListVO);
        memberOrderVO.setPaymentCurrencyUid(currencyListVOPayment);
        memberOrderVO.setAmount(amount);
        memberOrderVO.setUnitPrice(unitPrice);
        requestJson.setMemberOrderVO(memberOrderVO);

        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setVerifyCode(verifyCode);
        requestJson.setVerificationBean(verificationBean);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "sell:", requestJson);
        txInteractor.sell(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSell = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "sell:", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.sellSuccess(responseJson.getMessage());
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2045) {
                                    // {"success":false,"code":2045,"message":"Authenticator verify fail."}
                                    view.sellFailure(getString(R.string.google_verify_fail));
                                } else if (code == MessageConstants.CODE_2015) {
                                    // {"success":false,"code":2015,"message":"Current password is wrong."}
                                    view.sellFailure(getString(R.string.fund_password_is_wrong));
                                } else if (code == MessageConstants.CODE_2066) {
                                    //    {"success":false,"code":2066,"message":"Insufficient balances."}
                                    view.sellFailure(getString(R.string.no_enough_balance));
                                } else if (code == MessageConstants.CODE_2041) {
                                    view.sellFailure(getString(R.string.data_format_exception));
                                } else {
                                    view.sellFailure(getString(R.string.failure_to_sell_out_please_try_again));
                                }
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.sellFailure(getString(R.string.failure_to_sell_out_please_try_again));
                        disposeDisposable(disposableSell);

                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableSell);

                    }
                });
    }
}
