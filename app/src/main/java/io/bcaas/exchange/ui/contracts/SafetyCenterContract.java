package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.vo.MemberVO;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 「安全中心」
 */
public interface SafetyCenterContract {
    interface View {
        void logoutSuccess(String info);

        void logoutFailure(String info);

        void getAccountSecuritySuccess(MemberVO memberVO);

        void getAccountSecurityFailure(String info);
    }

    interface Presenter {
        //登出
        void logout(String memberId);

        //取得帳戶資訊
        void getAccountSecurity();
    }
}
