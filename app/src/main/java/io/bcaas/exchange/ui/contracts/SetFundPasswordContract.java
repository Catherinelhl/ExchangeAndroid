package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/2
 *  界面与数据相互作用的协议类：「设置资金密码」
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
