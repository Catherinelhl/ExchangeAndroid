package io.bcaas.exchange.ui.contracts;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/20 11:31
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.contracts
+--------------+---------------------------------
+ description  +  支付管理
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

import io.bcaas.exchange.base.BaseContract;
import io.bcaas.exchange.vo.MemberPayInfoVO;

public interface PayWayManagerContract {


    interface View extends BaseContract.View {
        <T> void responseSuccess(T message, String type);

        void responseFailed(String message, String type);
    }

    interface Presenter extends BaseContract.Presenter {
        void addPayWay(String type, MemberPayInfoVO memberPayInfoVO, String txPassword);

        void modifyPayWay(String type, MemberPayInfoVO memberPayInfoVO);

        void removePayWay(String type, MemberPayInfoVO memberPayInfoVO);

        void getBankInfo(String type);

        void getPayWay(String type);

        void rechargeVirtualCoin(String type, String currencyUID, String amount, String mark,String imageCode);

        void convertCoin(String type, String currencyUID, String amount, String txPassword);


    }
}
