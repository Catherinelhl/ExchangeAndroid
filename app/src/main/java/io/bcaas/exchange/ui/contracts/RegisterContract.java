package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 *
 *  界面与数据相互作用的协议类：注册
 */
public interface RegisterContract {
    interface View extends BaseContract.View {
        void registerSuccess(String info);

        void registerFailure(String info);

    }

    interface Presenter {
        void register(String memberId, String password, String realIp);

    }
}
