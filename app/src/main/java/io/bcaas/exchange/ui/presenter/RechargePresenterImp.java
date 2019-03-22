package io.bcaas.exchange.ui.presenter;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/22 16:46
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.presenter
+--------------+---------------------------------
+ description  +     充值查询逻辑处理
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
import io.bcaas.exchange.ui.contracts.RechargeContract;
import io.bcaas.exchange.ui.interactor.PaymentManagerInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class RechargePresenterImp extends BasePresenterImp implements RechargeContract.Presenter {

    private String TAG = RechargePresenterImp.class.getSimpleName();
    private RechargeContract.View view;
    private PaymentManagerInteractor interactor;
    private Disposable disposable;

    public RechargePresenterImp(RechargeContract.View view) {
        super();
        this.view = view;
        this.interactor = new PaymentManagerInteractor();
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
        disposeDisposable(disposable);
        interactor.getPayWay(GsonTool.beanToRequestBody(requestJson))
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
                                    view.getPayWaySuccess(memberPayInfoVOList);

                                }
                            } else {
                                int code = responseJson.getCode();
                                view.getPayWayFailed(responseJson.getMessage());
                            }

                        } else {
                            view.noData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        view.getPayWayFailed(e.getMessage());
                        disposeDisposable(disposable);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposable);
                    }
                });
    }
}
