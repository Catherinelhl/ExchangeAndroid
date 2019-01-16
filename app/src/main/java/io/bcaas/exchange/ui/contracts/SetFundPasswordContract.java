package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/2
 * 「设置资金密码」
 */
public interface SetFundPasswordContract {

    interface View extends BaseContract.View {

        //设置资金密码成功
        void securityTxPasswordSuccess(String info);

        //设置资金密码失败
        void securityTxPasswordFailure(String info);
    }

    interface Presenter {
        void securityTxPassword(String password);
    }
}
