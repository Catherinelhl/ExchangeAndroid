package io.bcaas.exchange.ui.interactor;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/20 14:08
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.interactor
+--------------+---------------------------------
+ description  +  支付管理API请求转接
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

import io.bcaas.exchange.http.HttpApi;
import io.bcaas.exchange.http.retrofit.RetrofitFactory;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;

public class PaymentManagerInteractor {

    /**
     * 新增會員支付方式
     *
     * @param body
     */
    public Observable<ResponseJson> addPayWay(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.addPayWay(body);
    }


    /**
     * 修改會員支付信息
     *
     * @param body
     */
    public Observable<ResponseJson> modifyPayWay(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.modifyPayWay(body);
    }

    /**
     * 移除會員支付方式
     *
     * @param body
     */
    public Observable<ResponseJson> removePayWay(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.removePayWay(body);
    }

    /**
     * 取得會員支付方式
     *
     * @param body
     */
    public Observable<ResponseJson> getPayWay(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.getPayWay(body);
    }

    /**
     * 取得平台收款帳戶資訊
     *
     * @param body
     */
    public Observable<ResponseJson> getBankInfo(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.getBankInfo(body);
    }

    /**
     * 充值虛擬幣
     *
     * @param body
     */
    public Observable<ResponseJson> rechargeVirtual(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.rechargeVirtual(body);
    }

    /**
     * 回購虛擬幣
     *
     * @param body
     */
    public Observable<ResponseJson> convertCoin(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.convertCoin(body);
    }
}
