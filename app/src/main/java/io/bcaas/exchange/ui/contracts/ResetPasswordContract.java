package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * 重设密码
 */
public interface ResetPasswordContract {

    interface View {
        void resetPasswordFailure(String info);

        void resetPasswordSuccess(String info);
    }

    interface Presenter {
        void resetPassword(String password, String newPassword);
    }
}
