package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.vo.MemberVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/8
 * 「关闭验证」
 */
public interface CloseVerifyCodeContract {

    interface View {
        void closeVerifyCodeSuccess(String info);

        void closeVerifyCodeFailure(String info);

        void getAccountSecuritySuccess(MemberVO memberVO);

        void getAccountSecurityFailure(String info);
    }

    interface Presenter {
        //关闭验证方式
        void closeVerifyCode(List<VerificationBean> verificationBeans);
        //取得帳戶資訊
        void getAccountSecurity();
    }
}
