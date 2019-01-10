package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.bean.ExchangeBean;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.PaginationVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface MainContract {
    interface View extends AccountSecurityContract.View {
        void getCurrencyUSDPriceSuccess(ExchangeBean exchangeBean);

        void getCurrencyUSDPriceFailure(String info);


        //获取所有币种余额成功
        void getAllBalanceSuccess(List<MemberKeyVO> memberKeyVOList);

        //获取所有币种余额失败
        void getAllBalanceFailure(String info);

    }

    interface Presenter extends AccountSecurityContract.Presenter {
        void getCurrencyUSDPrice(ExchangeBean exchangeBean);

        void getAllBalance();

    }
}
