package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/2
 * 「设置资金密码」
 */
public interface FundPasswordContract {

    interface View {

        //设置资金密码成功
        void setFundPasswordSuccess();

        //设置资金密码失败
        void setFundPasswordFailure();
    }

    interface Presenter {
        void setFundPassword(String password);
    }
}
