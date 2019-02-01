package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 *  界面与数据相互作用的协议类：忘记而重设密码
 */
public interface ForgetToResetPasswordContract {

    interface View extends BaseContract.View{
        void forgetPasswordFailure(String info);

        void forgetPasswordSuccess(String info);
    }

    interface Presenter {
        void forgetPassword(String password, String verifyCode);
    }
}
