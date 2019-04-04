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
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.Constants;
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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class PaymentManagerPresenterImp extends BasePresenterImp implements PayWayManagerContract.Presenter {
    private String TAG = PaymentManagerPresenterImp.class.getSimpleName();
    private PayWayManagerContract.View view;
    private PaymentManagerInteractor paymentManagerInteractor;
    private Disposable disposableGetPayWay, disposableAddPayWay,
            disposableModifyPayWay, disposableRemovePayWay, disposableGetBankInfo,
            disposableRecharge, disposableBuyBack, disposableVerification;
    private CompositeDisposable compositeDisposable;

    public PaymentManagerPresenterImp(PayWayManagerContract.View view) {
        super();
        this.view = view;
        this.paymentManagerInteractor = new PaymentManagerInteractor();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void addPayWay(String type, MemberPayInfoVO memberPayInfoVO, String txPassword) {
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        memberVO.setIsPayWayBind(1);
        try {
            memberVO.setTxPassword(Sha256Tool.doubleSha256ToString(txPassword));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        requestJson.setMemberVO(memberVO);


        disposeDisposable(disposableAddPayWay);
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
                        disposableAddPayWay = d;
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        view.hideLoading();
                        if (responseJson != null) {
                            GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "addPayWay:", responseJson);
                            if (responseJson.isSuccess()) {
                                view.responseSuccess(responseJson.getMessage(), type);
                            } else {
                                int code = responseJson.getCode();
                                if (!view.httpExceptionDisposed(responseJson)) {
                                    if (code == MessageConstants.CODE_2015) {
                                        view.responseFailed(getString(R.string.fund_password_is_wrong), type);
                                    } else {
                                        view.responseFailed(responseJson.getMessage(), type);
                                    }
                                }
                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        disposeDisposable(disposableAddPayWay);
                        view.hideLoading();
                        e.printStackTrace();
                        view.responseFailed(e.getMessage(), type);
                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableAddPayWay);
                        view.hideLoading();
                    }
                });
    }

    /**
     * 修改支付当时，暂且不用
     *
     * @param type
     * @param memberPayInfoVO
     */
    @Override
    public void modifyPayWay(String type, MemberPayInfoVO memberPayInfoVO) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);

        disposeDisposable(disposableModifyPayWay);
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
                        disposableModifyPayWay = d;
                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "modifyPayWay:", responseJson);
                        if (responseJson.isSuccess()) {
                            view.responseSuccess(responseJson.getMessage(), type);
                        } else {
                            int code = responseJson.getCode();
                            if (!view.httpExceptionDisposed(responseJson)) {
                                view.responseFailed(responseJson.getMessage(), type);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposeDisposable(disposableModifyPayWay);
                        e.printStackTrace();
                        view.responseFailed(e.getMessage(), type);
                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableModifyPayWay);

                    }
                });
    }

    @Override
    public void removePayWay(String type, MemberPayInfoVO memberPayInfoVO) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        memberVO.setIsPayWayBind(0);
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);
        requestJson.setMemberPayInfoVO(memberPayInfoVO);

        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "removePayWay:", requestJson);
        disposeDisposable(disposableRemovePayWay);
        paymentManagerInteractor.removePayWay(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableRemovePayWay = d;
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "removePayWay:", responseJson);
                        if (responseJson.isSuccess()) {
                            view.responseSuccess(responseJson.getMessage(), type);
                        } else {
                            int code = responseJson.getCode();
                            if (!view.httpExceptionDisposed(responseJson)) {
                                view.responseFailed(responseJson.getMessage(), type);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.responseFailed(e.getMessage(), type);
                        disposeDisposable(disposableRemovePayWay);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableRemovePayWay);
                    }
                });
    }

    @Override
    public void getBankInfo(String type) {
        disposeDisposable(disposableGetBankInfo);
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
                        disposableGetBankInfo = d;
                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getBankInfo:", responseJson);
                        if (responseJson.isSuccess()) {
                            view.responseSuccess(responseJson.getCenterInfoVO(), type);
                        } else {
                            int code = responseJson.getCode();
                            if (!view.httpExceptionDisposed(responseJson)) {
                                view.responseFailed(responseJson.getMessage(), type);
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.responseFailed(e.getMessage(), type);
                        disposeDisposable(disposableGetBankInfo);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableGetBankInfo);

                    }
                });
    }

    @Override
    public void getPayWay(String type) {
        disposeDisposable(disposableGetPayWay);
        view.showLoading();
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
                        disposableGetPayWay = d;
                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        view.hideLoading();
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
                                //{"success":false,"code":2016,"message":"Account not yet login."}
                                if (!view.httpExceptionDisposed(responseJson)) {
                                    view.responseFailed(responseJson.getMessage(), type);
                                }
                            }

                        } else {
                            view.noData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        e.printStackTrace();
                        view.responseFailed(e.getMessage(), type);
                        disposeDisposable(disposableGetPayWay);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableGetPayWay);
                        view.hideLoading();
                    }
                });
    }

    /**
     * 充值
     *
     * @param type
     * @param currencyUID 只能充值CNYC，currencyUID 带入"3"
     * @param amount      充值金额
     * @param mark        付款备注号；格式目前暂定为6为随机数字
     * @param imageCode   图形验证码
     */
    @Override
    public void rechargeVirtualCoin(String type,
                                    String currencyUID,
                                    String amount,
                                    String mark,
                                    String imageCode) {
        disposeDisposable(disposableRecharge);
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
        memberOrderVO.setAmount(amount);// 充值金额
        memberOrderVO.setMark(mark);//付款备注号
        requestJson.setMemberOrderVO(memberOrderVO);


        MemberPayInfoVO memberPayInfoVO = new MemberPayInfoVO();
        memberPayInfoVO.setPayWayUid(Constants.PayWayUid.BANK);//支付方式代号
        requestJson.setMemberPayInfoVO(memberPayInfoVO);

        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setVerifyCode(imageCode);//图形验证码
        requestJson.setVerificationBean(verificationBean);

        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "rechargeVirtualCoin:", requestJson);
        paymentManagerInteractor.rechargeVirtual(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableRecharge = d;
                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        if (responseJson != null) {
                            GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "rechargeVirtualCoin:", responseJson);
                            if (responseJson.isSuccess()) {
                                view.responseSuccess(responseJson.getMessage(), type);
                            } else {
                                int code = responseJson.getCode();
                                //    {"success":false,"code":2000,"message":"VO is null or type error."}
                                if (!view.httpExceptionDisposed(responseJson)) {
                                    view.responseFailed(responseJson.getMessage(), type);
                                }


                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.responseFailed(e.getMessage(), type);
                        disposeDisposable(disposableRecharge);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableRecharge);

                    }
                });
    }

    @Override
    public void convertCoin(String type, String currencyUID, String amount, String txPassword) {
        disposeDisposable(disposableBuyBack);
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
                        disposableBuyBack = d;
                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        view.hideLoading();
                        if (responseJson != null) {
                            GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "convertCoin:", responseJson);
                            if (responseJson.isSuccess()) {
                                view.responseSuccess(responseJson.getMessage(), type);
                            } else {
                                int code = responseJson.getCode();

                                //  {"code":2066,"message":"Insufficient balances.","size":0,"success":false}
                                // {"success":false,"code":2015,"message":"Current password is wrong."}
                                //    {"success":false,"code":2000,"message":"VO is null or type error."}
                                if (!view.httpExceptionDisposed(responseJson)) {
                                    if (code == MessageConstants.CODE_2066) {
                                        view.responseFailed(getString(R.string.no_enough_balance), type);
                                    } else if (code == MessageConstants.CODE_2015) {
                                        view.responseFailed(getString(R.string.current_password_is_wrong), type);
                                    } else if (code == MessageConstants.CODE_2000) {
                                        view.responseFailed(getString(R.string.data_format_exception), type);
                                    } else {
                                        view.responseFailed(responseJson.getMessage(), type);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        e.printStackTrace();
                        LogTool.e(TAG, e.getMessage());
                        view.responseFailed(e.getMessage(), type);
                        disposeDisposable(disposableBuyBack);
                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableBuyBack);
                    }
                });
    }

    @Override
    public void identityNameVerification(String identityName, String type) {
        disposeDisposable(disposableVerification);
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        memberVO.setIdentityName(identityName);
        requestJson.setMemberVO(memberVO);

        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "identityNameVerification:", requestJson);

        paymentManagerInteractor.identityNameVerification(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableVerification = d;
                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        view.hideLoading();
                        if (responseJson != null) {
                            GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "identityNameVerification:", responseJson);
                            if (responseJson.isSuccess()) {
                                view.responseSuccess(responseJson.getMessage(), type);
                            } else {
                                int code = responseJson.getCode();
                                //    {"success":false,"code":2097,"message":"Identity name verify has been done."}
                                // {"success":false,"code":2015,"message":"Current password is wrong."}
                                //    {"success":false,"code":2000,"message":"VO is null or type error."}
                                if (!view.httpExceptionDisposed(responseJson)) {
                                    view.responseFailed(responseJson.getMessage(), type);
                                }
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        e.printStackTrace();
                        LogTool.e(TAG, e.getMessage());
                        view.responseFailed(e.getMessage(), type);
                        disposeDisposable(disposableVerification);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableVerification);

                    }
                });
    }

    @Override
    public void cancelSubscribe() {
        // 清除所有还存在的dispose
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        disposeDisposable(disposableBuyBack);
        disposeDisposable(disposableRecharge);
        disposeDisposable(disposableGetBankInfo);
        disposeDisposable(disposableRemovePayWay);
        disposeDisposable(disposableModifyPayWay);
        disposeDisposable(disposableAddPayWay);
        disposeDisposable(disposableGetPayWay);
        disposeDisposable(disposableVerification);
    }
}
