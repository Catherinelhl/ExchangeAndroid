package io.bcaas.exchange.http;


import io.bcaas.exchange.constants.APIURLConstants;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/20
 * <p>
 * Http请求网络的所有接口組裝
 */
public interface HttpApi {


    /**
     * 注册
     */
    @POST(APIURLConstants.API_REGISTER)
    Observable<ResponseJson> register(@Body RequestBody requestBody);

    /**
     * 验证账号是否注册
     */
    @POST(APIURLConstants.API_VERIFY_ACCOUNT)
    Observable<ResponseJson> verifyAccount(@Body RequestBody requestBody);

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
     * 忘记密码
     */
    @POST(APIURLConstants.API_FORGET_PASSWORD)
    Observable<ResponseJson> forgetPassword(@Body RequestBody requestBody);

    /**
     * 重设密码
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
    @POST(APIURLConstants.API_SECURITY_TWO_FACTOR_AUTH_VERIFY)
    Observable<ResponseJson> securityTwoFactorVerify(@Body RequestBody requestBody);


    /**
     * 取得帳戶所有幣種餘額
     */
    @POST(APIURLConstants.API_GET_ALL_BALANCE)
    Observable<ResponseJson> getAllBalance(@Body RequestBody requestBody);

    /**
     * 取得幣種的手續費
     */
    @POST(APIURLConstants.API_GET_CURRENCY_CHARGE)
    Observable<ResponseJson> getCurrencyCharge(@Body RequestBody requestBody);

    /**
     * 取得帳戶資訊
     */
    @POST(APIURLConstants.API_GET_ACCOUNT_SECURITY)
    Observable<ResponseJson> getAccountSecurity(@Body RequestBody requestBody);

    /**
     * 取得財務紀錄交易資訊
     */
    @POST(APIURLConstants.API_GET_RECORD)
    Observable<ResponseJson> getRecord(@Body RequestBody requestBody);

    /**
     * 验证验证码\关闭安全验证
     */
    @POST(APIURLConstants.API_CLOSE_SECURITY_VERIFY)
    Observable<ResponseJson> closeSecurityVerify(@Body RequestBody requestBody);


    /**
     * 設置資金密碼
     */
    @POST(APIURLConstants.API_SECURITY_TX_PASSWORD)
    Observable<ResponseJson> securityTxPassword(@Body RequestBody requestBody);


    /**
     * 撤銷待出售訂單
     */
    @POST(APIURLConstants.API_CANCEL_ORDER)
    Observable<ResponseJson> cancelOrder(@Body RequestBody requestBody);

    /**
     * 出售
     */
    @POST(APIURLConstants.API_SELL)
    Observable<ResponseJson> sell(@Body RequestBody requestBody);

    /**
     * 查詢待出售訂單
     */
    @POST(APIURLConstants.API_GET_RECORD_LIST)
    Observable<ResponseJson> getOrderList(@Body RequestBody requestBody);

    /**
     * 購買
     */
    @POST(APIURLConstants.API_BUY)
    Observable<ResponseJson> buy(@Body RequestBody requestBody);


    /**
     * 转出
     */
    @POST(APIURLConstants.API_WITH_DRAW)
    Observable<ResponseJson> withDraw(@Body RequestBody requestBody);


    /**
     * 取得幣種名稱資訊
     */
    @POST(APIURLConstants.API_GET_COIN_NAME_LIST)
    Observable<ResponseJson> getCoinNameList(@Body RequestBody requestBody);

    /**
     * 取得幣種市值資訊
     */
    @POST(APIURLConstants.API_GET_COIN_MARKET_CAP)
    Observable<ResponseJson> getCoinMarketCap(@Body RequestBody requestBody);


    /**
     * 获取Android版本信息
     *
     * @param requestBody
     * @return
     */
    @POST(APIURLConstants.API_GET_ANDROID_VERSION_INFO)
    Observable<ResponseJson> getAndroidInfo(@Body RequestBody requestBody);


    /**
     * 新增會員支付方式
     *
     * @param requestBody
     * @return
     */
    @POST(APIURLConstants.API_ADD_PAY_WAY)
    Observable<ResponseJson> addPayWay(@Body RequestBody requestBody);

    /**
     * 修改會員支付信息
     *
     * @param requestBody
     * @return
     */
    @POST(APIURLConstants.API_MODIFY_PAY_WAY)
    Observable<ResponseJson> modifyPayWay(@Body RequestBody requestBody);

    /**
     * 移除會員支付方式
     *
     * @param requestBody
     * @return
     */
    @POST(APIURLConstants.API_REMOVE_PAY_WAY)
    Observable<ResponseJson> removePayWay(@Body RequestBody requestBody);

    /**
     * 取得會員支付方式
     *
     * @param requestBody
     * @return
     */
    @POST(APIURLConstants.API_GET_PAY_WAY)
    Observable<ResponseJson> getPayWay(@Body RequestBody requestBody);

    /**
     * 取得平台收款帳戶資訊
     *
     * @param requestBody
     * @return
     */
    @POST(APIURLConstants.API_GET_BANK_INFO)
    Observable<ResponseJson> getBankInfo(@Body RequestBody requestBody);

    /**
     * 转入虛擬幣
     *
     * @param requestBody
     * @return
     */
    @POST(APIURLConstants.API_RECHARGE_VIRTUAL_COIN)
    Observable<ResponseJson> rechargeVirtual(@Body RequestBody requestBody);

    /**
     * 回購虛擬幣
     *
     * @param requestBody
     * @return
     */
    @POST(APIURLConstants.API_CONVERT_COIN)
    Observable<ResponseJson> convertCoin(@Body RequestBody requestBody);

}
