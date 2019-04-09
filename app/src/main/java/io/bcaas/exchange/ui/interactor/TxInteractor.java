package io.bcaas.exchange.ui.interactor;

import io.bcaas.exchange.http.HttpApi;
import io.bcaas.exchange.http.retrofit.RetrofitFactory;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 数据与请求相互作用类：交易相关
 */
public class TxInteractor {
    /**
     * 取得幣種的手續費
     *
     * @param body
     */
    public Observable<ResponseJson> getCurrencyCharge(RequestBody body) {
       return RetrofitFactory.getAPIInstance().create(HttpApi.class).getCurrencyCharge(body);
    }

    /**
     * 撤銷待出售訂單
     *
     * @param body
     */
    public Observable<ResponseJson> cancelOrder(RequestBody body) {
       return RetrofitFactory.getAPIInstance().create(HttpApi.class).cancelOrder(body);
    }

    /**
     * 取得財務紀錄交易資訊
     *
     * @param body
     */
    public Observable<ResponseJson> getRecord(RequestBody body) {
        return RetrofitFactory.getAPIInstance().create(HttpApi.class).getRecord(body);
    }

    /**
     * 出售
     *
     * @param body
     */
    public Observable<ResponseJson> sell(RequestBody body) {
        return RetrofitFactory.getAPIInstance().create(HttpApi.class).sell(body);
    }

    /**
     * 查詢待出售訂單
     *
     * @param body
     */
    public Observable<ResponseJson> getOrderList(RequestBody body) {
       return RetrofitFactory.getAPIInstance().create(HttpApi.class).getOrderList(body);
    }

    /**
     * 購買
     *
     * @param body
     */
    public Observable<ResponseJson> buy(RequestBody body) {
       return RetrofitFactory.getAPIInstance().create(HttpApi.class).buy(body);
    }

    /**
     * 转出
     *
     * @param body
     */
    public Observable<ResponseJson> withDraw(RequestBody body) {
        return RetrofitFactory.getAPIInstance().create(HttpApi.class).withDraw(body);
    }

}
