package io.bcaas.exchange.ui.constracts;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface LogoutConstract {
    interface View {
        void logoutSuccess(String info);

        void logoutFailure(String info);
    }

    interface Presenter {
        void logout(String memberId);
    }
}
