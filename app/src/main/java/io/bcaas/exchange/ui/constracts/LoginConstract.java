package io.bcaas.exchange.ui.constracts;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface LoginConstract {
    interface View {
        void loginSuccess(String info);

        void loginFailure(String info);
    }

    interface Presenter {
        void login(String memberId, String password, String realIp);
    }
}
