package io.bcaas.exchange.ui.interactor;

import io.bcaas.exchange.http.HttpApi;
import io.bcaas.exchange.http.retrofit.RetrofitFactory;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 交易相关
 */
public class TxInteractor {

    /**
     * 撤銷待出售訂單
     *
     * @param body
     */
    public Observable<ResponseJson> cancelOrder(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.cancelOrder(body);
    }

    /**
     * 取得財務紀錄交易資訊
     *
     * @param body
     */
    public Observable<ResponseJson> getRecord(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.getRecord(body);
    }
    /**
     * 出售
     *
     * @param body
     */
    public Observable<ResponseJson> sell(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.sell(body);
    }

    /**
     * 查詢待出售訂單
     *
     * @param body
     */
    public Observable<ResponseJson> getOrderList(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.getOrderList(body);
    }

    /**
     * 購買
     *
     * @param body
     */
    public Observable<ResponseJson> buy(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.buy(body);
    }

    /**
     *  提现
     *
     * @param body
     */
    public Observable<ResponseJson> withDraw(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.withDraw(body);
    }

}
