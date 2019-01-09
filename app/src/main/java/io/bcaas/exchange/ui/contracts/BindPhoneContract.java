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
        void bindPhoneSuccess(String info);

        void bindPhoneFailure(String info);

        void getCountryCodeFailure();
        void getCountryCodeSuccess(List<CountryCodeBean.CountryCode> countryCodes);
    }

    interface Presenter {
        void phoneVerify(String phone, String languageCode);

        void getCountryCode(String Language);
    }
}
