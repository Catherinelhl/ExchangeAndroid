package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/8
 *  界面与数据相互作用的协议类：手机验证
 */
public interface PhoneVerifyContract {

    interface View extends BaseContract.View {

        void getPhoneCodeSuccess(String info);

        void getPhoneCodeFailure(String info);

    }

    interface Presenter {
        void getPhoneCode(String phone, String languageCode);
    }
}
