package io.bcaas.exchange.ui.contracts;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/22 16:42
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.contracts
+--------------+---------------------------------
+ description  +  充值
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

import io.bcaas.exchange.base.BaseContract;
import io.bcaas.exchange.vo.MemberPayInfoVO;

import java.util.List;

public interface RechargeContract {

    interface View extends BaseContract.View {
        void getPayWaySuccess(List<MemberPayInfoVO> memberPayInfoVOList);

        void getPayWayFailed(String message);
    }

    interface Presenter extends BaseContract.Presenter {
        void getPayWay();
    }
}
