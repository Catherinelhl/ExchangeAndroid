package io.bcaas.exchange.ui.interactor;

import io.bcaas.exchange.http.HttpApi;
import io.bcaas.exchange.http.retrofit.RetrofitFactory;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 「安全中心」
 */
public class SafetyCenterInteractor {


    /**
     * 登出
     *
     * @param body
     */
    public Observable<ResponseJson> logout(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.logout(body);
    }
}
