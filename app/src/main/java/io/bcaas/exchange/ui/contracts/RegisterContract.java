package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface RegisterContract {
    interface View {
        void registerSuccess(String info);

        void registerFailure(String info);

    }

    interface Presenter {
        void register(String memberId, String password, String realIp);

    }
}
