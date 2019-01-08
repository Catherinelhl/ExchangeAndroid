package io.bcaas.exchange.ui.interactor;

import io.bcaas.exchange.http.HttpApi;
import io.bcaas.exchange.http.retrofit.RetrofitFactory;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 */
public class VerifyCodeInteractor {

    /**
     * 邮箱验证
     */
    public Observable<ResponseJson> emailVerify(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.emailVerify(body);
    }

    /**
     * 获取图形验证码
     *
     * @return
     */
    public Observable<ResponseBody> getImageVerifyCode() {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.imageVerifyCreate();
    }

    /**
     * Phone验证码
     *
     * @return
     */
    public Observable<ResponseJson> phoneVerify(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.phoneVerify(body);
    }

    /**
     * 2FA 雙因素驗證
     *
     * @return
     */
    public Observable<ResponseJson> getAuthenticatorUrl(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.authenticatorVerify(body);
    }

    /**
     * 验证 雙因素驗證码
     *
     * @return
     */
    public Observable<ResponseJson> verifyAuthenticatorCode(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.verifyAuthenticatorCode(body);
    }
}
