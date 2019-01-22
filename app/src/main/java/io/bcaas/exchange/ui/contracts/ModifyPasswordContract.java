package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * 修改密码
 */
public interface ModifyPasswordContract {

    interface View extends BaseContract.View{
        void resetPasswordFailure(String info);

        void resetPasswordSuccess(String info);
    }

    interface Presenter {
        void resetPassword(String password, String newPassword);
    }
}
