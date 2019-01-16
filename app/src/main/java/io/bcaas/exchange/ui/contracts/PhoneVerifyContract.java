package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/8
 * 手机验证
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
