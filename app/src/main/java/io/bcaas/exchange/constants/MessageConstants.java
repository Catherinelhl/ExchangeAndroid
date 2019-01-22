package io.bcaas.exchange.constants;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/3
 */
public class MessageConstants {

    public static final int CODE_404 = 404; // Failure

    public static final int CODE_2028 = 2028; //LanguageCode format invalid.
    public static final String CODE_2028_MSG = "LanguageCode format invalid."; // LanguageCode format invalid.


    //字节码格式
    public static final String CHARSET_FORMAT = "UTF-8";
    public static final String EMPTY = "";
    public static final String DEFAULT_NEXT_OBJECT_ID = "0";//默认的nextObjectId
    public static final String NULL = "(null)";
    public static String HTTP_CONTENT_ENCODING = "Content-Encoding";
    public static final String ENCODE_INGORE_CASE = "identity";//http設置encode忽略
    public static final String CONNECT_EXCEPTION = "connect exception,need switch server...";

    public static final String SCREEN_WIDTH = "screen width:";
    public static final String SCREEN_HEIGHT = "screen height:";
    public static final String DEVICE_INFO = "Devices info:";


    public static final boolean STATUS_SUCCESS = true;
    public static final boolean STATUS_FAILURE = false;

    public static final int CODE_200 = 200; // Success
    public static final int CODE_400 = 400; // Failure

    // Common
    public static final String SUCCESS_REGEX = "Regex Success.";

    public static final int CODE_2000 = 2000;
    public static final String ERROR_VO_ISNULL_TYPE = "VO is null or type error.";
    public static final int CODE_2001 = 2001;
    public static final String ERROR_LOST_PARAMETERS = "Lost parameters.";
    public static final int CODE_2002 = 2002;
    public static final String ERROR_PARAMETER_FORMAT = "Parameter foramt error.";
    public static final int CODE_2003 = 2003;
    public static final String ERROR_JSON_DECODE = "JSON decode error.";
    public static final int CODE_2004 = 2004;
    public static final String ERROR_NEXT_PAGE_EMPTY = "Next page is empty.";
    public static final int CODE_2005 = 2005;
    public static final String ERROR_EMAIL_NOT_REGISTER = "Email not register.";
    public static final int CODE_2006 = 2006;
    public static final String ERROR_EMAIL_ALREADY_REGISTER = "Email already register.";
    public static final int CODE_2007 = 2007;
    public static final String ERROR_ACCOUNT_FORMAT_INVALID = "Account format invalid.";
    public static final int CODE_2008 = 2008;
    public static final String ERROR_PASSWORD_FORMAT_INVALID = "Password format invalid.";
    public static final int CODE_2010 = 2010;
    public static final String ERROR_VERIFY_CODE_FAIL = "Verify code fail.";
    public static final int CODE_2012 = 2012;
    public static final String ERROR_PHONE_FORMAT_INVALID = "Phone format invalid.";
    public static final int CODE_2014 = 2014;
    public static final String ERROR_IP_ADDRESS_INVALID = "IP address invalid.";
    public static final int CODE_2015 = 2015;
    public static final String ERROR_CURRENT_PASSWORD_IS_WRONG = "Current password is not correct.";
    public static final int CODE_2016 = 2016;
    public static final String ERROR_ACCOUNT_NOT_YET_LOGIN = "Account not yet login.";
    public static final int CODE_2018 = 2018;
    public static final String ERROR_ACCOUNT_HAS_BEEN_DEACTIVATED = "Account has been deactivated.";
    public static final int CODE_2019 = 2019;
    public static final String ERROR_ACCESSTOKEN_EXPIRE = "AccessToken expire.";
    public static final int CODE_2020 = 2020;
    public static final String SUCCESS_ACCESSTOKEN_VERIFY = "AccessToken verify success.";
    public static final int CODE_2022 = 2022;
    public static final String ERROR_TXPASSWORD_SAME_PASSWORD = "TxPassword is the same as the account password.";
    public static final int CODE_2025 = 2025;
    public static final String ERROR_VERIFY_CODE_EXPIRE = "Verify code expire.";
    public static final int CODE_2026 = 2026;
    public static final String ERROR_ORDER_TYPE = "Order type format invalid.";
    public static final int CODE_2027 = 2027;
    public static final String ERROR_NEXTOBJECTID_FORMAT_INVALID = "NextObjectId format invalid.";
    public static final int CODE_2029 = 2029;
    public static final String ERROR_PASSWORD_NEWPASSWORD_NOT_CONSISTENT = "Password & NewPassword not consistent.";
    public static final int CODE_2030 = 2030;
    public static final String ERROR_NEW_PASSWORD_SAME_AS_CURRENT = "New password is same as current password.";
    public static final int CODE_2031 = 2031;
    public static final String ERROR_ORDER_UID_NOT_EXIST = "Member order uid not exist.";
    public static final int CODE_2032 = 2032;
    public static final String ERROR_ORDER_TYPE_OR_STATUS_ERROR = "Order type or status error.";
    public static final int CODE_2033 = 2033;
    public static final String ERROR_ENNAME_FORMAT_INVALID = "EnName format invalid.";
    public static final int CODE_2034 = 2034;
    public static final String ERROR_CNNAME_FORMAT_INVALID = "CnName format invalid.";
    public static final int CODE_2035 = 2035;
    public static final String ERROR_WITHDRAWCHARGE_FORMAT_INVALID = "WithdrawCharge format invalid.";
    public static final int CODE_2036 = 2036;
    public static final String ERROR_GASFREECHANGE_FORMAT_INVALID = "GasFeeCharge format invalid.";
    public static final int CODE_2037 = 2037;
    public static final String ERROR_RATE_FORMAT_INVALID = "Rate format invalid.";
    public static final int CODE_2038 = 2038;
    public static final String ERROR_CURRENCY_ALREADY_REGISTERED = "Currency already registered.";
    public static final int CODE_2039 = 2039;
    public static final String ERROR_BUYCHARGE_FORMAT_INVALID = "BuyCharge format invalid.";
    public static final int CODE_2040 = 2040;
    public static final String ERROR_SELLCHARGE_FORMAT_INVALID = "SellCharge format invalid.";
    public static final int CODE_2041 = 2041;
    public static final String ERROR_UID_FORMAT_INVALID = "Uid format invalid.";
    public static final int CODE_2045 = 2045;
    public static final String ERROR_AUTHENTICATOR_VERIFY_FAIL = "Authenticator verify fail.";
    public static final int CODE_2057 = 2057;
    public static final String ERROR_ILLEGAL_REQUEST = "Illegal request.";
    public static final int CODE_2055 = 2055;
    public static final String ERROR_INVALID_ORDER_INFORMATION = "Invalid order information.";
    //BTC
    public static final int CODE_3000 = 3000;
    public static final String ERROR_SEND_WALLET_ADDRESS_INVALID = "Send wallet address invalid.";
    public static final int CODE_3004 = 3004;
    public static final String ERROR_BTC_WALLETADDRESS_FORMAT_INVALID = "WalletAddress format invalid.";
    // ETH
    public static final int CODE_4000 = 4000;
    public static final String ERROR_ETH_CONNECT = "Connection web3j error.";
    public static final int CODE_4001 = 4001;
    public static final String ERROR_WALLETADDRESS_FORMAT_INVALID = "WalletAddress format invalid.";
    public static final int CODE_4002 = 4002;
    public static final String ERROR_TXHASH_FORMAT_INVALID = "Txhash format invalid.";
    public static final int CODE_4003 = 4003;
    public static final String ERROR_PRIVATEKEY_FORMAT_INVALID = "PrivateKey format invalid.";
    public static final int CODE_4004 = 4004;
    public static final String ERROR_ETH_AMOUNT_FORMAT_INVALID = "Amount format invalid.";
    public static final int CODE_4005 = 4005;
    public static final String ERROR_ETH_GET_TRANSACTION_COUNT = "ETH get transaction count fail.";
    public static final int CODE_4006 = 4006;
    public static final String ERROR_ETH_SIGN_MESSAGE = "ETH sign message fail.";
    public static final int CODE_4007 = 4007;
    public static final String ERROR_ETH_SEND_TRANSACTION = "ETH send transaction fail.";
    public static final int CODE_4008 = 4008;
    public static final String ETH_GET_TRANSACTION_FAIL = "ETH get transaction info fail.";
    public static final int CODE_4009 = 4009;
    public static final String ETH_TXSTATUS_FAIL = "ETH tx status fail.";
    public static final int CODE_4010 = 4010;
    public static final String ETH_TXSTATUS_PROCESSING = "ETH tx status processing.";
    public static final int CODE_4011 = 4011;
    public static final String ETH_TXSTATUS_COMPLETED = "ETH tx status completed.";

    public static final int CODE_5001 = 5001;
    public static final String BCC_WALLET_ADDRESS_FORMAT_INVALID = "WalletAddress format invalid.";
    public static final String SUCCESS_GETTXSTATUS = "Get txStatus success.";
    public static final String FAILURE_GETTXSTATUS = "Get txStatus failure.";
    public static final String SUCCESS_PUSHTX = "PushTx success.";
    public static final String FAILURE_PUSHTX = "PushTx failure.";

    // API Type...
    // Version
    public static final String SUCCESS_ADDVERSIONINFO = "Add version info success.";
    public static final String FAILURE_ADDVERSIONINFO = "Add version info failure.";

    // Member
    public static final String SUCCESS_MEMBER_REGISTERACCOUNT = "Member register account success.";
    public static final String FAILURE_MEMBER_REGISTERACCOUNT = "Member register account failure.";
    public static final String SUCCESS_MEMBER_LOGIN = "Member login success.";
    public static final String FAILURE_MEMBER_LOGIN = "Member login failure.";
    public static final String SUCCESS_MEMBER_LOGOUT = "Member logout success.";
    public static final String FAILURE_MEMBER_LOGOUT = "Member logout failure.";
    public static final String SUCCESS_MEMBER_RESETPASSWORD = "Member reset password success.";
    public static final String FAILURE_MEMBER_RESETPASSWORD = "Member reset password failure.";
    public static final String SUCCESS_MEMBER_VERIFYACCOUNT = "Member verify account success.";
    public static final String FAILURE_MEMBER_VERIFYACCOUNT = "Member verify account failure.";
    public static final String SUCCESS_MEMBER_SECURITYEMAIL = "Member security email setting success.";
    public static final String FAILURE_MEMBER_SECURITYEMAIL = "Member security email setting failure.";
    public static final String SUCCESS_MEMBER_SECURITYPHONE = "Member security phone setting success.";
    public static final String FAILURE_MEMBER_SECURITYPHONE = "Member security phone setting failure.";
    public static final String SUCCESS_MEMBER_SECURITYTXPASSWORD = "Member security txPassword setting success.";
    public static final String FAILURE_MEMBER_SECURITYTXPASSWORD = "Member security txPassword setting failure.";
    public static final String SUCCESS_MEMBER_SECURITY2FA = "Member security two-factor authentication setting success.";
    public static final String FAILURE_MEMBER_SECURITY2FA = "Member security two-factor authentication setting failure.";
    public static final String SUCCESS_MEMBER_SECURITYINFO = "Member security info success.";
    public static final String FAILURE_MEMBER_SECURITYINFO = "Member security info failure.";
    public static final String SUCCESS_MEMBER_GETRECORD = "Member get record success.";
    public static final String FAILURE_MEMBER_GETRECORD = "Member get record failure.";


    // MemberOrder
    public static final String SUCCESS_MEMBERORDER_GETRECORD = "Member get record success.";
    public static final String FAILURE_MEMBERORDER_GETRECORD = "Member get record failure.";
    public static final String SUCCESS_MEMBERORDER_CANCEL = "Member order cancel success.";
    public static final String FAILURE_MEMBERORDER_CANCEL = "Member order cancel failure.";

    // MemberKey
    public static final String SUCCESS_MEMBERKEY_CREATEADDRESS = "MemberKey create address success.";
    public static final String FAILURE_MEMBERKEY_CREATEADDRESS = "MemberKey create address failure.";
    public static final String SUCCESS_MEMBERKEY_GETALLBALANCE = "MemberKey get all balance success.";
    public static final String FAILURE_MEMBERKEY_GETALLBALANCE = "MemberKey get all balance failure.";

    // CurrencyList
    public static final String SUCCESS_CURRENCYLIST_ADDNEWCURRENCY = "CurrencyList add new currency success.";
    public static final String FAILURE_CURRENCYLIST_ADDNEWCURRENCY = "CurrencyList add new currency failure.";


    public static final String NO_ENOUGH_BALANCE = "-1";
    public static final String AMOUNT_EXCEPTION_CODE = "-1";

    public class LogInfo {
        public static final String REQUEST_JSON = "【RequestJson】";
        public static final String RESPONSE_JSON = "【ResponseJson】";
    }
}
