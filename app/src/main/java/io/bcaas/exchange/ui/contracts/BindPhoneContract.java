package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/8
 * 绑定手机
 */
public interface BindPhoneContract {

    interface View {
        void bindPhoneSuccess(String info);

        void bindPhoneFailure(String info);
    }

    interface Presenter {
        void phoneVerify(String phone, String languageCode);
    }
}
