package io.bcaas.exchange.ui.interactor;

import io.bcaas.exchange.http.HttpApi;
import io.bcaas.exchange.http.retrofit.RetrofitFactory;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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

    /**
     * 忘记密码
     *
     * @param body
     */
    public Observable<ResponseJson> forgetPassword(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.forgetPassword(body);
    }

    /**
     * 重设密码
     *
     * @param body
     */
    public Observable<ResponseJson> resetPassword(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.resetPassword(body);
    }

    /**
     * 验证验证码\关闭安全验证
     *
     * @param body
     */
    public Observable<ResponseJson> closeSecurityVerify(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.closeSecurityVerify(body);
    }

    /**
     * 邮箱验证
     */
    public Observable<ResponseJson> emailVerify(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.emailVerify(body);
    }



    /**
     * 验证当前账户是否已经注册
     *
     * @param body
     */
    public Observable<ResponseJson> verifyAccount(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.verifyAccount(body);
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
     * 2FA 雙因素驗證图片生成
     *
     * @return
     */
    public Observable<ResponseBody> getAuthenticatorUrlCreateImage(String url) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.authenticatorVerifyCreateImage(url);
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


    /**
     * 更改E-mail驗證狀態
     *
     * @return
     */
    public Observable<ResponseJson> securityEmail(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.securityEmail(body);
    }


    /**
     * 更改手機驗證狀態
     *
     * @return
     */
    public Observable<ResponseJson> securityPhone(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.securityPhone(body);
    }


    /**
     * 更改Google驗證狀態
     *
     * @return
     */
    public Observable<ResponseJson> securityTwoFactorVerify(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.securityTwoFactorVerify(body);
    }


    /**
     * 設置資金密碼
     *
     * @return
     */
    public Observable<ResponseJson> securityTxPassword(RequestBody body) {
        HttpApi httpApi = RetrofitFactory.getAPIInstance().create(HttpApi.class);
        return httpApi.securityTxPassword(body);
    }
}
