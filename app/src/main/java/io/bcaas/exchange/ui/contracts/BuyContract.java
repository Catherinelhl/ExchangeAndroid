package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 *  界面与数据相互作用的协议类：购买
 */
public interface BuyContract {

    interface View extends AccountSecurityContract.View {
        void buyFailure(String info);

        void buySelfError();

        void invalidBuyOrder(String info);

        void buySuccess(String info);
    }

    interface Presenter extends AccountSecurityContract.Presenter{
        void buy(String txPassword, long memberOderUid, String verifyCode);
    }
}
