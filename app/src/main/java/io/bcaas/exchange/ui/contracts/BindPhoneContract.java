package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.bean.CountryCodeBean;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/8
 * 绑定手机
 */
public interface BindPhoneContract {

    interface View {
        void securityPhoneSuccess(String info);

        void securityPhoneFailure(String info);

        void getPhoneCodeSuccess();
        void getPhoneCodeFailure();

        void getCountryCodeFailure();
        void getCountryCodeSuccess(List<CountryCodeBean.CountryCode> countryCodes);
    }

    interface Presenter {
        void getPhoneCode(String phone, String languageCode);
        void securityPhone(String phone, String verifyCode);
        void getCountryCode(String Language);
    }
}
