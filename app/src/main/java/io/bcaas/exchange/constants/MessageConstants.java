package io.bcaas.exchange.constants;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/3
 */
public class MessageConstants {

    public static final int CODE_200 = 200; // Success
    public static final int CODE_400 = 400; // Failure
    public static final int CODE_404 = 404; // Failure
    public static final int CODE_2006 = 2006; // Failure
    public static final String CODE_2006_MSG = "Email already register."; // Failure

    public static final int CODE_2028 = 2028; //LanguageCode format invalid.
    public static final String CODE_2028_MSG = "LanguageCode format invalid."; // LanguageCode format invalid.

    public static final int CODE_2019 = 2019; //AccessToken expire.
    public static final String CODE_2019_MSG = "AccessToken expire."; //AccessToken expire.


    //字节码格式
    public static final String CHARSET_FORMAT = "UTF-8";
    public static final String EMPTY = "";
    public static String HTTP_CONTENT_ENCODING = "Content-Encoding";
    public static final String ENCODE_INGORE_CASE = "identity";//http設置encode忽略
    public static final String CONNECT_EXCEPTION = "connect exception,need switch server...";

    public static final String SCREEN_WIDTH = "screen width:";
    public static final String SCREEN_HEIGHT = "screen height:";
    public static final String DEVICE_INFO = "Devices info:";


}
