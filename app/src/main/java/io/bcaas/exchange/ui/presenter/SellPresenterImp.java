package io.bcaas.exchange.ui.presenter;

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
 */
public class SellPresenterImp implements SellContract.Presenter {
    private String TAG = SellPresenterImp.class.getSimpleName();

    private SellContract.View view;
    private TxInteractor txInteractor;

    public SellPresenterImp(SellContract.View view) {
        super();
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
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
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


        LogTool.d(TAG, "sell:" + GsonTool.string(requestJson));
        txInteractor.sell(GsonTool.beanToRequestBody(requestJson))
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
                            view.sellFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.sellSuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
//                            if (code == MessageConstants.CODE_2019) {
                            //    {"success":false,"code":2019,"message":"AccessToken expire."}
                            view.sellFailure(responseJson.getMessage());

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.sellFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
