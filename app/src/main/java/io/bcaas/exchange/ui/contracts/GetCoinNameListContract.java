package io.bcaas.exchange.ui.contracts;

import io.bcaas.exchange.vo.CurrencyListVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/21
 * 获取CoinName
 */
public interface GetCoinNameListContract {

    interface View extends BaseContract.View {

        void getCoinNameListSuccess(List<CurrencyListVO> currencyListVOList);

        void getCoinNameListFailure(String info);


    }

    interface Presenter extends BaseContract.Presenter {
        void getCoinNameList();
    }
}
