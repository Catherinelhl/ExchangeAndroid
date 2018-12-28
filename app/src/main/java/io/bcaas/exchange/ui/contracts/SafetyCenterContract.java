package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 「安全中心」
 */
public interface SafetyCenterContract {
    interface View {
        void logoutSuccess(String info);

        void logoutFailure(String info);
    }

    interface Presenter {
        void logout(String memberId);
    }
}
