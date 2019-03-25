package io.bcaas.exchange.ui.presenter;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/20 11:30
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.presenter
+--------------+---------------------------------
+ description  +  
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BasePresenterImp;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.PayWayManagerContract;
import io.bcaas.exchange.ui.interactor.PaymentManagerInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class PaymentManagerPresenterImp extends BasePresenterImp implements PayWayManagerContract.Presenter {
    private String TAG = PaymentManagerPresenterImp.class.getSimpleName();
    private PayWayManagerContract.View view;
    private PaymentManagerInteractor paymentManagerInteractor;
    private Disposable disposable;

    public PaymentManagerPresenterImp(PayWayManagerContract.View view) {
        super();
        this.view = view;
        this.paymentManagerInteractor = new PaymentManagerInteractor();
    }

    @Override
    public void addPayWay(String type, MemberPayInfoVO memberPayInfoVO, String txPassword) {
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

        requestJson.setMemberPayInfoVO(memberPayInfoVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "addPayWay:", requestJson);

        paymentManagerInteractor.addPayWay(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        if (responseJson != null) {
                            GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "addPayWay:", responseJson);
                            if (responseJson.isSuccess()) {
                                view.responseSuccess(responseJson.getMessage(),type);
                            } else {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2015) {
                                    view.responseFailed(getString(R.string.fund_password_is_wrong), type);
                                }
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void modifyPayWay(String type, MemberPayInfoVO memberPayInfoVO) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        requestJson.setMemberPayInfoVO(memberPayInfoVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "modifyPayWay:", requestJson);

        paymentManagerInteractor.modifyPayWay(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "modifyPayWay:", responseJson);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void removePayWay(String type, MemberPayInfoVO memberPayInfoVO) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);
        requestJson.setMemberPayInfoVO(memberPayInfoVO);

        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "removePayWay:", requestJson);

        paymentManagerInteractor.removePayWay(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "removePayWay:", responseJson);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getBankInfo(String type) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "getBankInfo:", requestJson);

        paymentManagerInteractor.getBankInfo(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getBankInfo:", responseJson);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getPayWay(String type) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "getPayWay:", requestJson);
        disposeDisposable(disposable);
        paymentManagerInteractor.getPayWay(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getPayWay:", responseJson);
                        if (responseJson != null) {
                            if (responseJson.isSuccess()) {
                                List<MemberPayInfoVO> memberPayInfoVOList = responseJson.getMemberPayInfoVOList();
                                if (ListTool.isEmpty(memberPayInfoVOList)) {
                                    view.noData();
                                } else {
                                    view.responseSuccess(memberPayInfoVOList, type);

                                }
                            } else {
                                int code = responseJson.getCode();
                                view.responseFailed(responseJson.getMessage(), type);
                            }

                        } else {
                            view.noData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.responseFailed(e.getMessage(), type);
                        disposeDisposable(disposable);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposable);
                    }
                });
    }

    @Override
    public void rechargeVirtualCoin(String type, String currencyUID, String amount, String mark) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        MemberOrderVO memberOrderVO = new MemberOrderVO();
        CurrencyListVO currencyListVO = new CurrencyListVO();
        currencyListVO.setCurrencyUid(currencyUID);
        memberOrderVO.setCurrencyListVO(currencyListVO);
        memberOrderVO.setAmount(amount);
        memberOrderVO.setMark(mark);
        requestJson.setMemberOrderVO(memberOrderVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "rechargeVirtualCoin:", requestJson);

        paymentManagerInteractor.rechargeVirtual(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "rechargeVirtualCoin:", responseJson);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void convertCoin(String type, String currencyUID, String amount, String txPassword) {
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
        CurrencyListVO currencyListVO = new CurrencyListVO();
        currencyListVO.setCurrencyUid(currencyUID);
        memberOrderVO.setCurrencyListVO(currencyListVO);
        memberOrderVO.setAmount(amount);
        requestJson.setMemberOrderVO(memberOrderVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "convertCoin:", requestJson);

        paymentManagerInteractor.convertCoin(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "convertCoin:", responseJson);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
