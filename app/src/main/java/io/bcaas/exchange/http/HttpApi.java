package io.bcaas.exchange.http;


/**
 * @author catherine.brainwilliam
 * @since 2018/8/20
 * <p>
 * Http请求网络的所有接口組裝
 */
public interface HttpApi {

//    /************** SFN ***************/
//    // 登入  @FormUrlEncoded
//    @POST(APIURLConstants.API_SFN_WALLET_LOGIN)
//    Call<ResponseJson> login(@Body RequestBody requestBody);
//
//    /*SFN：登出*/
//    @POST(APIURLConstants.API_SFN_WALLET_LOGOUT)
//    Call<ResponseJson> logout(@Body RequestBody requestBody);
//
//    /*SFN：验证当前token是否过期*/
//    @POST(APIURLConstants.API_SFN_WALLET_VERIFY)
//    Call<ResponseJson> verify(@Body RequestBody requestBody);
//
//    /*SFN：當錢包與AuthNode無法通信時調用,取得新的AuthNode IP資訊*/
//    @POST(APIURLConstants.API_SFN_WALLET_RESETAUTHNODEINFO)
//    Call<ResponseJson> resetAuthNodeInfo(@Body RequestBody requestBody);
//
//
//    /************** SAN ***************/
//    /*AN："取最新的區塊 & wallet餘額"*/
//    /* 每次发送之前需要请求*/
//    @POST(APIURLConstants.API_SAN_WALLET_GETLATESTBLOCKANDBALANCE)
//    Call<ResponseJson> getLastedBlockAndBalance(@Body RequestBody requestBody);
//
//    /*AN："取得未簽章R區塊的Send區塊 & 取最新的R區塊 & wallet餘額"*/
//    /*由TCP和服务器建立长连接，进行定时的拉取数据*/
//    @POST(APIURLConstants.API_SAN_WALLET_GETWALLETWAITINGTORECEIVEBLOCK)
//    Call<ResponseJson> getWalletWaitingToReceiveBlock(@Body RequestBody requestBody);
//
//    /*单独获取余额*/
//    @POST(APIURLConstants.API_SAN_WALLET_GETBALANCE)
//    Call<ResponseJson> getBalance(@Body RequestBody requestBody);
//
//    /*AN：TC Send*/
//    @POST(APIURLConstants.API_SAN_WALLET_TRANSACTIONCHAIN_SEND)
//    Call<ResponseJson> send(@Body RequestBody requestBody);
//
//    /*AN：TC receiver*/
//    @POST(APIURLConstants.API_SAN_WALLET_TRANSACTIONCHAIN_RECEIVE)
//    Call<ResponseJson> receive(@Body RequestBody requestBody);
//
//    /*AN：獲取最新的更換委託人區塊*/
//    @POST(APIURLConstants.API_SAN_WALLET_GETLATESTCHANGEBLOCK)
//    Call<ResponseJson> getLatestChangeBlock(@Body RequestBody requestBody);
//
//    /*AN：TC change*/
//    @POST(APIURLConstants.API_SAN_WALLET_CHANGE)
//    Call<ResponseJson> change(@Body RequestBody requestBody);
//
//
//    /************** API ***************/
//
//    /*獲取幣種清單 API*/
//    @POST(APIURLConstants.API_GET_BLOCK_SERVICE_LIST)
//    Call<ResponseJson> getBlockServiceList(@Body RequestBody requestBody);
//
//    /*獲取已完成交易 API*/
//    @POST(APIURLConstants.API_ACCOUNT_DONE_TC)
//    Call<ResponseJson> getAccountDoneTC(@Body RequestBody requestBody);
//
//
//    /*获取当前Wallet的外网IP*/
//    @POST(APIURLConstants.API_GET_MY_IP_INFO)
//    Call<ResponseJson> getMyIpInfo(@Body RequestBody requestBody);
//
//    /************** UPDATE ***************/
//    /*检查更新Android版本信息*/
//    @POST(APIURLConstants.API_GET_ANDROID_VERSION_INFO)
//    Call<ResponseJson> getAndroidVersionInfo(@Body RequestBody requestBody);
}
