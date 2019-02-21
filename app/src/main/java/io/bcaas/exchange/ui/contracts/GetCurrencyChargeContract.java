package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;
import io.bcaas.exchange.vo.CurrencyListVO;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/11
 *  界面与数据相互作用的协议类：获取当前币种的手续费
 */
public interface GetCurrencyChargeContract {
    interface View extends BaseContract.View{
        void getCurrencyChargeFailure(String info);

        void getCurrencyChargeSuccess(CurrencyListVO currencyListVO);
    }

    interface Presenter {
        void getCurrencyCharge(String currencyUid);
    }
}
