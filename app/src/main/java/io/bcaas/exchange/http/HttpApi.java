package io.bcaas.exchange.http;


import io.bcaas.exchange.constants.APIURLConstants;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
     * 取得虛擬貨幣美元現值
     */
    @POST(APIURLConstants.API_CURRENCY_GET_USD_PRICE)
    Observable<ResponseJson> getCurrencyUSDPrice(@Body RequestBody requestBody);

    /**
     * Email验证
     */
    @POST(APIURLConstants.API_EMAIL_VERIFY)
    Observable<ResponseJson> emailVerify(@Body RequestBody requestBody);

    /**
     * 產生圖形驗證圖
     */
    @POST(APIURLConstants.API_IMAGE_VERIFY_CREATE_URL)
    Observable<ResponseJson> imageVerifyCreate(@Body RequestBody requestBody);

}
