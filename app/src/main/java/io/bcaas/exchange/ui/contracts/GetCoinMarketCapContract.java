package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.base.BaseContract;
import io.bcaas.exchange.bean.CoinMarketCapBean;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/18
 *  界面与数据相互作用的协议类：取得币种以及相对应的币种市值信息
 */
public interface GetCoinMarketCapContract {
    interface View extends  BaseContract.View{
        void getCoinMarketCapSuccess(CoinMarketCapBean coinMarketCapBean);

        void getCoinMarketCapFailure(String info);
    }

    interface Presenter extends BaseContract.Presenter{

        void getCoinMarketCap(String coinName, long startTime, long endTime);
    }
}
