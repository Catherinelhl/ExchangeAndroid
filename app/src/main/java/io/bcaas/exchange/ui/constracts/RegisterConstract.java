package io.bcaas.exchange.ui.constracts;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface RegisterConstract {
    interface View {
        void registerSuccess(String info);

        void registerFailure(String info);
    }

    interface Presenter {
        void register(String memberId, String password, String realIp);
    }
}
