package io.bcaas.exchange.ui.contracts;


import io.bcaas.exchange.base.BaseContract;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 *  界面与数据相互作用的协议类：登录
 */
public interface LoginContract {
    interface View extends BaseContract.View {
        void loginSuccess(String info);

        void loginFailure(String info);

        //图形验证码错误
        void ImageVerifyCodeError(String info);

    }

    interface Presenter {
        void login(String memberId, String password, String realIp);
    }
}
