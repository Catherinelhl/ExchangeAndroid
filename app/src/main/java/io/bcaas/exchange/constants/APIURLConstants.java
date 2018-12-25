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
    /*驗證帳號是否註冊*/
    public static final String API_VERIFY_ACCOUNT = "member/verifyAccount";
    /*登入*/
    public static final String API_LOGIN = "member/login";
    /*登出*/
    public static final String API_LOGOUT = "member/logout";


    /*BTC 取得餘額*/
    public static final String API_BTC_GETBALANCE = "btc/getBalance";
    /*BTC 取得交易的hash*/
    public static final String API_BTC_GET_TX_HASH= "btc/getTxHash";
    /*BTC 發送交易*/
    public static final String API_BTC_PUSH_TX = "btc/pushTx";
    /*BTC 取得交易狀態*/
    public static final String API_BTC_GET_TX_STATUS = "btc/getTxStatus";



    /*ETH 取得餘額*/
    public static final String API_ETH_GETBALANCE = "eth/getBalance";
    /*ETH 發送交易*/
    public static final String API_ETH_PUSH_TX = "btc/pushTx";
    /*ETH 取得交易狀態*/
    public static final String API_ETH_GET_TX_STATUS = "btc/getTxStatus";



    /*取得虛擬貨幣美元現值*/
    public static final String API_CURRENCY_GET_USD_PRICE = "currency/getUSDPrice";



    /*2FA 雙因素驗證*/
    public static final String API_AUTHENTICATOR_URL = "verification/getAuthenticatorUrl";
    /*發送Email驗證*/
    public static final String API_SEND_MAIL_CODE = "verification/sendMailCode";
    /*發送Phone驗證*/
    public static final String API_SEND_PHONE_CODE = "verification/sendPhoneCode";
    /*驗證雙因素驗證碼*/
    public static final String API_AUTHENTICATOR_CODE = "verification/verifyAuthenticatorCode";

    /*設置資金密碼*/
    public static final String API_SECURITY_TX_PASSWORD = "member/securityTxPassword";

    /*E-Mail驗證*/
    public static final String API_EMAIL_VERIFY = "verification/sendMailVerifyCode";
    /*手機驗證*/
    public static final String API_PHONE_VERIFY = "member/securityPhone";
    /*Google驗證*/
    public static final String API_GOOGLE_VERIFY = "member/securityTwoFactorAuthVerify";
    /*取得帳戶資訊*/
    public static final String API_GET_USER_INFO = "member/getMemberSecurity";

}
