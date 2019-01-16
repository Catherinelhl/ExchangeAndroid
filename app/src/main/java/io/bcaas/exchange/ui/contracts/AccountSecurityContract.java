package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.vo.MemberVO;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 「获取账户信息」
 */
public interface AccountSecurityContract {
    interface View extends BaseContract.View{

        void getAccountSecuritySuccess(MemberVO memberVO);

        void getAccountSecurityFailure(String info);

    }

    interface Presenter {

        //取得帳戶資訊
        void getAccountSecurity();
    }
}
