package io.bcaas.exchange.http;


import io.bcaas.exchange.constants.APIURLConstants;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/20
 * <p>
 * Http请求网络的所有接口組裝
 */
public interface HttpApi {


    /**
     * 登入
     */
    @POST(APIURLConstants.API_REGISTER)
    Observable<ResponseJson> register(@Body RequestBody requestBody);

    /**
     * 登入  @FormUrlEncoded
     */
    @POST(APIURLConstants.API_LOGIN)
    Observable<ResponseJson> login(@Body RequestBody requestBody);

    /**
     * 登出
     */
    @POST(APIURLConstants.API_LOGOUT)
    Observable<ResponseJson> logout(@Body RequestBody requestBody);
    /**
     *重置密码
     */
    @POST(APIURLConstants.API_RESET_PASSWORD)
    Observable<ResponseJson> resetPassword(@Body RequestBody requestBody);





    /**
     * 取得虛擬貨幣美元現值
     */
    @POST(APIURLConstants.API_CURRENCY_GET_USD_PRICE)
    Observable<ResponseJson> getCurrencyUSDPrice(@Body RequestBody requestBody);

    /**
     * Email验证
     */
    @POST(APIURLConstants.API_SEND_MAIL_VERIFY_CODE)
    Observable<ResponseJson> emailVerify(@Body RequestBody requestBody);

    /**
     * Phone验证
     */
    @POST(APIURLConstants.API_SEND_PHONE_VERIFY_CODE)
    Observable<ResponseJson> phoneVerify(@Body RequestBody requestBody);


    /**
     * 2FA 雙因素驗證
     */
    @POST(APIURLConstants.API_AUTHENTICATOR_URL)
    Observable<ResponseJson> authenticatorVerify(@Body RequestBody requestBody);

    /**
     * 2FA 雙因素驗證生成图片
     */
    @GET
    Observable<ResponseBody> authenticatorVerifyCreateImage(@Url String url);


    /**
     * 驗證雙因素驗證碼
     */
    @POST(APIURLConstants.API_VERIFY_AUTHENTICATOR_CODE)
    Observable<ResponseJson> verifyAuthenticatorCode(@Body RequestBody requestBody);

    /**
     * 產生圖形驗證圖
     */
    @GET(APIURLConstants.API_IMAGE_VERIFY_CREATE_URL)
    Observable<ResponseBody> imageVerifyCreate();


    /**
     * 更改E-mail驗證狀態
     */
    @POST(APIURLConstants.API_SECURITY_EMAIL)
    Observable<ResponseJson> securityEmail(@Body RequestBody requestBody);

    /**
     * 更改手機驗證狀態
     */
    @POST(APIURLConstants.API_SECURITY_PHONE)
    Observable<ResponseJson> securityPhone(@Body RequestBody requestBody);

    /**
     * 更改Google驗證狀態
     */
    @POST(APIURLConstants.API_SECURITY_TWO_FACTORAUTH_VERIFY)
    Observable<ResponseJson> securityTwoFactorVerify(@Body RequestBody requestBody);


    /**
     * 取得帳戶所有幣種餘額
     */
    @POST(APIURLConstants.API_GET_ALL_BALANCE)
    Observable<ResponseJson> getAllBalance(@Body RequestBody requestBody);

    /**
     * 取得帳戶資訊
     */
    @POST(APIURLConstants.API_GET_ACCOUNT_SECURITY)
    Observable<ResponseJson> getAccountSecurity(@Body RequestBody requestBody);

    /**
     * 验证验证码\关闭安全验证
     */
    @POST(APIURLConstants.API_CLOSE_SECURITY_VERIFY)
    Observable<ResponseJson> closeSecurityVerify(@Body RequestBody requestBody);


}
