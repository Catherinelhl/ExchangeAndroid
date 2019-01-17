package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.bean.ExchangeBean;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/17
 * 获得当前汇率
 */
public interface GetCurrencyUSDPriceContract {

    interface View extends BaseContract.View {
        void getCurrencyUSDPriceSuccess(ExchangeBean exchangeBean);

        void getCurrencyUSDPriceFailure(String info);
    }

    interface Presenter extends BaseContract.Presenter {
        void getCurrencyUSDPrice(ExchangeBean exchangeBean);
    }
}
