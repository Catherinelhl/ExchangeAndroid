package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.vo.CurrencyListVO;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/11
 */
public interface GetCurrencyChargeContract {
    interface View {
        void getCurrencyChargeFailure(String info);

        void getCurrencyChargeSuccess(CurrencyListVO currencyListVO);
    }

    interface Presenter {
        void getCurrencyCharge(String currencyUid);
    }
}
