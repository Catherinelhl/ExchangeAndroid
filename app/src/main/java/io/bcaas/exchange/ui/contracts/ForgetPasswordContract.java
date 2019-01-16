package io.bcaas.exchange.ui.contracts;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * 忘记密码
 */
public interface ForgetPasswordContract {

    interface View extends BaseContract.View{
        void forgetPasswordFailure(String info);

        void forgetPasswordSuccess(String info);
    }

    interface Presenter {
        void forgetPassword(String password, String verifyCode);
    }
}
