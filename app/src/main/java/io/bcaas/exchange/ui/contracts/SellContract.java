package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 *  界面与数据相互作用的协议类：出售
 */
public interface SellContract {
    interface View extends AccountSecurityContract.View{
        void sellFailure(String info);

        void sellSuccess(String info);

    }

    interface Presenter extends AccountSecurityContract.Presenter{
        /**
         * 售出
         *
         * @param currencyUid        卖出的币种
         * @param paymentCurrencyUid 买方支付的币种
         * @param amount             金额
         * @param unitPrice          单价
         * @param txPassword          填写的资金密码
         * @param verifyCode         验证码
         */
        void sell(String currencyUid, String paymentCurrencyUid, String amount, String unitPrice,String txPassword, String verifyCode);
    }
}
