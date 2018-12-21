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
    public static final String API_EMAIL_VERIFY = "member/securityEmail";
    /*手機驗證*/
    public static final String API_PHONE_VERIFY = "member/securityPhone";
    /*Google驗證*/
    public static final String API_GOOGLE_VERIFY = "member/securityTwoFactorAuthVerify";
    /*取得帳戶資訊*/
    public static final String API_GET_USER_INFO = "member/getMemberSecurity";

}
