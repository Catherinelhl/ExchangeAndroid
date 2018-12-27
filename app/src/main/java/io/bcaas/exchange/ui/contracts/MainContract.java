package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.bean.ExchangeBean;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 */
public interface MainContract {
    interface View {
        void getCurrencyUSDPriceSuccess(String info);

        void getCurrencyUSDPriceFailure(String info);
    }

    interface Presenter {
        void getCurrencyUSDPrice(String memberId, ExchangeBean exchangeBean);
    }
}
