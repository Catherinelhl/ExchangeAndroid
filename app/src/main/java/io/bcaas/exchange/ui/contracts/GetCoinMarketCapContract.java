package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.bean.CoinMarketCapBean;
import io.bcaas.exchange.vo.CurrencyListVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/18
 * 取得币种以及相对应的币种市值信息
 */
public interface GetCoinMarketCapContract {
    interface View {
        void getCoinNameListSuccess(List<CurrencyListVO> currencyListVOList);

        void getCoinNameListFailure(String info);


        void getCoinMarketCapSuccess(CoinMarketCapBean coinMarketCapBean);

        void getCoinMarketCapFailure(String info);
    }

    interface Presenter {

        void getCoinNameList();

        void getCoinMarketCap(String coinName, long startTime, long endTime);
    }
}
