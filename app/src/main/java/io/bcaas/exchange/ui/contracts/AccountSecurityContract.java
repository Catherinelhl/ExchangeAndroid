package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;
import io.bcaas.exchange.vo.MemberVO;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 界面与数据相互作用的协议类：「获取账户信息」
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
