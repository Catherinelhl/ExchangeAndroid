package io.bcaas.exchange.ui.interactor;

import io.bcaas.exchange.http.HttpApi;
import io.bcaas.exchange.http.retrofit.RetrofitFactory;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 数据与请求相互作用类：「获取账户资讯」
 */
public class AccountSecurityInteractor {

    /**
     * 取得帳戶資訊
     *
     * @param body
     */
    public Observable<ResponseJson> getAccountSecurity(RequestBody body) {
        return RetrofitFactory.getAPIInstance().create(HttpApi.class).getAccountSecurity(body);
    }

}
