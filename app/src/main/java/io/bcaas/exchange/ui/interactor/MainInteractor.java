package io.bcaas.exchange.ui.interactor;

import io.bcaas.exchange.http.HttpApi;
import io.bcaas.exchange.http.retrofit.RetrofitFactory;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 数据与请求相互作用类：首页/进入APP会需要的用户基本信息
 */
public class MainInteractor {

    /**
     * 取得虛擬貨幣美元現值
     *
     * @param body
     */
    public Observable<ResponseJson> getCurrencyUSDPrice(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.getCurrencyUSDPrice(body);
    }

    /**
     * 取得帳戶所有幣種餘額
     *
     * @param body
     */
    public Observable<ResponseJson> getAllBalance(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.getAllBalance(body);
    }

    /**
     * 取得幣種名稱資訊
     *
     * @param body
     */
    public Observable<ResponseJson> getCoinNameList(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.getCoinNameList(body);
    }

    /**
     * 取得幣種市值資訊
     *
     * @param body
     */
    public Observable<ResponseJson> getCoinMarketCap(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.getCoinMarketCap(body);
    }

}
