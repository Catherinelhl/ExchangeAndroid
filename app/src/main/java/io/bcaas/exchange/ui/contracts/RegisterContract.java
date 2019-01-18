package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface RegisterContract {
    interface View extends BaseContract.View {
        void registerSuccess(String info);

        void registerFailure(String info);

        void verifyAccountFailure(String info);

        void verifyAccountSuccess(String info);

    }

    interface Presenter {
        /*验证当前账户是否已经注册过了*/
        void verifyAccount(String memberId);

        void register(String memberId, String password, String realIp);

    }
}
