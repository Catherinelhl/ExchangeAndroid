package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.vo.RequestJson;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 *  界面与数据相互作用的协议类：提现
 */
public interface WithDrawContract {
    interface View extends AccountSecurityContract.View {
        void withDrawFailure(String info);

        void withDrawSuccess(String info);
    }

    interface Presenter extends AccountSecurityContract.Presenter{
        /**
         * 提现
         *
         * @param txPassword    交易密码
         */
        void withDraw(String txPassword, RequestJson requestJson);
    }
}
