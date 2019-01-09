package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.bean.VerificationBean;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/9
 * Google验证获取URL
 */
public interface GoogleContract {
    interface View {
        void getAuthenticatorUrlSuccess(VerificationBean verificationBean);

        void getAuthenticatorUrlFailure(String info);

        void securityGoogleAuthenticatorSuccess(String info);

        void securityGoogleAuthenticatorFailure(String info);
    }

    interface Presenter {
        void getAuthenticatorUrl();

        void securityGoogleAuthenticator(String verifyCode);
    }
}
