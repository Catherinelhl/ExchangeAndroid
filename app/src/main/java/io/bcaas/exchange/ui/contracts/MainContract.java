package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.bean.ExchangeBean;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface MainContract {
    interface View {
        void getCurrencyUSDPriceSuccess(String info);

        void getCurrencyUSDPriceFailure(String info);


        //获取所有币种余额成功
        void getAllBalanceSuccess(List<MemberKeyVO> memberKeyVOList);

        //获取所有币种余额失败
        void getAllBalanceFailure(String info);
    }

    interface Presenter {
        void getCurrencyUSDPrice(String memberId, ExchangeBean exchangeBean);

        void getAllBalance();
    }
}
