package io.bcaas.exchange.ui.constracts;

import io.bcaas.exchange.vo.ExchangeBean;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface MainConstract {
    interface View {
        void getCurrencyUSDPriceSuccess(String info);

        void getCurrencyUSDPriceFailure(String info);
    }

    interface Presenter {
        void getCurrencyUSDPrice(String memberId, ExchangeBean exchangeBean);
    }
}
