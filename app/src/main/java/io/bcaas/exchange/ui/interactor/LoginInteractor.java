package io.bcaas.exchange.ui.interactor;

import io.bcaas.exchange.http.HttpApi;
import io.bcaas.exchange.http.retrofit.RetrofitFactory;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 用户登录相关的网络连接器
 */
public class LoginInteractor {

    /**
     * 注册
     *
     * @param body
     */
    public Observable<ResponseJson> register(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.register(body);
    }

    /**
     * 登入
     *
     * @param body
     */
    public Observable<ResponseJson> login(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.login("", body);
    }

    /**
     * 登出
     *
     * @param body
     */
    public Observable<ResponseJson> logout(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.logout(body);
    }

    /**
     * 邮箱验证
     */
    public Observable<ResponseJson> emailVerify(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.emailVerify(body);
    }
}
