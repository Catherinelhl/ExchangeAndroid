package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * 购买
 */
public interface BuyContract {

    interface View extends BaseContract {
        void buyFailure(String info);
        void buySelfError();
        void buySuccess(String info);
    }

    interface Presenter {
        void buy(String txPassword,long memberOderUid,String verifyCode);
    }
}
