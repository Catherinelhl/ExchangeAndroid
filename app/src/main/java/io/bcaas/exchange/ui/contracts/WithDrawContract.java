package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.vo.MemberOrderVO;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * 提现
 */
public interface WithDrawContract {
    interface View extends BaseContract {
        void withDrawFailure(String info);

        void withDrawSuccess(String info);
    }

    interface Presenter {
        /**
         * 提现
         *
         * @param txPassword    交易密码
         * @param memberOrderVO 提现金额+提现备注
         * @param address       提现地址
         * @param currencyUid   提现币种
         */
        void withDraw(String txPassword, MemberOrderVO memberOrderVO, String address, String currencyUid);
    }
}
