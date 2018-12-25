package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface RegisterContract {
    interface View {
        void registerSuccess(String info);

        void registerFailure(String info);

        //获取email验证码成功
        void getEmailVerifySuccess(String info);

        //获取email验证码失败
        void getEmailVerifyFailure(String info);
    }

    interface Presenter {
        void register(String memberId, String password, String realIp);

        void emailVerify(String memberId, String languageCode, String mail);
    }
}
