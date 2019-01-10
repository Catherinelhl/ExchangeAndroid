package io.bcaas.exchange.constants;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/3
 * <p>
 * 所有API URL
 */
public class APIURLConstants {

    /*会员注册*/
    public static final String API_REGISTER = "member/registerAccount";
    //    /*驗證帳號是否註冊*/
//    public static final String API_VERIFY_ACCOUNT = "member/verifyAccount";
    /*登入*/
    public static final String API_LOGIN = "member/login";
    /*登出*/
    public static final String API_LOGOUT = "member/logout";
    /*忘记密码*/
    public static final String API_FORGET_PASSWORD = "member/forgotPassword";
    /*重设密码*/
    public static final String API_RESET_PASSWORD = "member/resetPassword";


//    /*BTC 取得餘額*/
//    public static final String API_BTC_GETBALANCE = "btc/getBalance";
//    /*BTC 取得交易的hash*/
//    public static final String API_BTC_GET_TX_HASH = "btc/getTxHash";
//    /*BTC 發送交易*/
//    public static final String API_BTC_PUSH_TX = "btc/pushTx";
//    /*BTC 取得交易狀態*/
//    public static final String API_BTC_GET_TX_STATUS = "btc/getTxStatus";
//
//
//    /*ETH 取得餘額*/
//    public static final String API_ETH_GETBALANCE = "eth/getBalance";
//    /*ETH 發送交易*/
//    public static final String API_ETH_PUSH_TX = "btc/pushTx";
//    /*ETH 取得交易狀態*/
//    public static final String API_ETH_GET_TX_STATUS = "btc/getTxStatus";


    /*取得虛擬貨幣美元現值*/
    public static final String API_CURRENCY_GET_USD_PRICE = "currencyList/getUSDPrice";
    /*取得帳戶所有幣種餘額*/
    public static final String API_GET_ALL_BALANCE = "memberKey/getAllBalance";
    /*取得財務紀錄交易資訊*/
    public static final String API_GET_RECORD = "memberOrder/getRecord";


    /*撤銷待出售訂單*/
    public static final String API_CANCEL_ORDER = "memberOrder/cancel";
    /*出售*/
    public static final String API_SELL = "memberOrder/sell";
    /*查詢待出售訂單*/
    public static final String API_GET_RECORD_LIST = "memberOrder/getOrderList";
    /*購買*/
    public static final String API_BUY = "memberOrder/buy";


    /*產生圖形驗證圖*/
    public static final String API_IMAGE_VERIFY_CREATE_URL = "verification/create";
    /*2FA 雙因素驗證*/
    public static final String API_AUTHENTICATOR_URL = "verification/getAuthenticatorUrl";
    /*發送Email驗證*/
    public static final String API_SEND_MAIL_VERIFY_CODE = "verification/sendMailVerifyCode";
    /*發送Phone驗證*/
    public static final String API_SEND_PHONE_VERIFY_CODE = "verification/sendPhoneVerifyCode";
    /*驗證雙因素驗證碼*/
    public static final String API_VERIFY_AUTHENTICATOR_CODE = "verification/verifyAuthenticatorCode";


    /*更改E-mail驗證狀態*/
    public static final String API_SECURITY_EMAIL = "member/securityEmail";
    /*更改手機驗證狀態*/
    public static final String API_SECURITY_PHONE = "member/securityPhone";
    /*更改Google驗證狀態*/
    public static final String API_SECURITY_TWO_FACTOR_AUTH_VERIFY = "member/securityTwoFactorAuthVerify";


    /*取得帳戶資訊*/
    public static final String API_GET_ACCOUNT_SECURITY = "member/getSecurity";
    /*验证验证码\关闭安全验证*/
    public static final String API_CLOSE_SECURITY_VERIFY = "verification/verifyCode";
    /*設置資金密碼*/
    public static final String API_SECURITY_TX_PASSWORD = "member/securityTxPassword";


}
