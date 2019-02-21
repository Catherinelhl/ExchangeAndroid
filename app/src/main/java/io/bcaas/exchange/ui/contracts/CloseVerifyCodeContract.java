package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.vo.MemberVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/8
 *  界面与数据相互作用的协议类：「关闭验证」
 */
public interface CloseVerifyCodeContract {

    interface View extends AccountSecurityContract.View {
        void closeVerifyCodeSuccess(String info);

        void closeVerifyCodeFailure(String info);

    }

    interface Presenter extends AccountSecurityContract.Presenter {
        //关闭验证方式
        void closeVerifyCode(List<VerificationBean> verificationBeans);
    }
}
