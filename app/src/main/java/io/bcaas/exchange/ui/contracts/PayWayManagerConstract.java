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

public interface PayWayManagerConstract {


    interface View extends BaseContract.View {
        void addPayWaySuccess(String message);

        void addPayWayFailed(String message);

        void modifyPayWaySuccess(String message);

        void modifyPayWayFailed(String message);

        void removePayWaySuccess(String message);

        void removePayWayFailed(String message);

        void getPayWaySuccess(String message);

        void getPayWayFailed(String message);

        void getBankInfoSuccess(String message);

        void getBankInfoFailed(String message);

        void rechargeVirtualCoinSuccess(String message);

        void rechargeVirtualCoinFailed(String message);

        void convertCoinSuccess(String message);

        void convertCoinFailed(String message);

    }

    interface Presenter extends BaseContract.Presenter {
        void addPayWay(MemberPayInfoVO memberPayInfoVO);

        void modifyPayWay(MemberPayInfoVO memberPayInfoVO);

        void removePayWay(MemberPayInfoVO memberPayInfoVO);

        void getBankInfo();

        void getPayWay();

        void rechargeVirtualCoin(String currencyUID, String amount, String mark);

        void convertCoin(String currencyUID, String amount, String txPassword);


    }
}
