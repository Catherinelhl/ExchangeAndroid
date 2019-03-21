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

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.contracts.PayWayManagerConstract;
import io.bcaas.exchange.ui.interactor.PaymentManagerInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class PaymentManagerPresenterImp implements PayWayManagerConstract.Presenter {
    private String TAG = PaymentManagerPresenterImp.class.getSimpleName();
    private PayWayManagerConstract.View view;
    private PaymentManagerInteractor paymentManagerInteractor;

    public PaymentManagerPresenterImp(PayWayManagerConstract.View view) {
        super();
        this.view = view;
        this.paymentManagerInteractor = new PaymentManagerInteractor();
    }

    @Override
    public void addPayWay(MemberPayInfoVO memberPayInfoVO, String txPassword) {
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
                            } else {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2015) {
//    {"success":false,"code":2015,"message":"Current password is wrong."}
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
    public void modifyPayWay(MemberPayInfoVO memberPayInfoVO) {
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
    public void removePayWay(MemberPayInfoVO memberPayInfoVO) {
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
    public void getBankInfo() {
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
    public void getPayWay() {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "getPayWay:", requestJson);

        paymentManagerInteractor.getPayWay(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getPayWay:", responseJson);
                        if (responseJson != null) {
                            if (responseJson.isSuccess()) {
                                List<MemberPayInfoVO> memberPayInfoVOList = responseJson.getMemberPayInfoVOList();
                                if (ListTool.isEmpty(memberPayInfoVOList)) {

                                } else {

                                }
                            } else {
                                int code = responseJson.getCode();
                            }

                        } else {

                        }
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
    public void rechargeVirtualCoin(String currencyUID, String amount, String mark) {
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
    public void convertCoin(String currencyUID, String amount, String txPassword) {
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
