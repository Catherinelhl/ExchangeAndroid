package io.bcaas.exchange.constants;

import static io.bcaas.exchange.constants.Constants.Http.HTTP_COLON;
import static io.bcaas.exchange.constants.Constants.Http.HTTP_PREFIX;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/3
 * 常量类：APP里会用到的常量字符数据
 */
public class Constants {

    /* 定时器类型*/
    public enum TimerType {
        BrandTimer
    }

    /* 时间*/
    public class Time {
        public static final int sleep800 = 800;
        public static final int sleep1000 = 1000;
        public static final int sleep2000 = 2000;
        public static final int sleep10000 = 10000;
        public static final int sleep60 = 60;//60s
        public static final int LONG_TIME_OUT = 30;//设置超时时间
        public static final int STAY_BRAND_ACTIVITY = 2;//如果当前不用编辑页面，停留在页面的时间1s


    }

    public class Color {
        //二維碼渲染的前景色
        public static final int foregroundOfQRCode = 0xff000000;
        //二維碼渲染的背景色
        public static final int backgroundOfQRCode = 0x00000000;
    }

    public class ValueMaps {
        public static final int PASSWORD_MIN_LENGTH = 8;// 输入密码的最小长度
        public static final String THREE_STAR = "***";
        public static final String SC = "SC";//中文（简体）
        public static final String CN = "CN";//中文
        public static final String TC = "TC";//中文（繁體）
        public static final String TW = "TW";//中文（繁體）
        public static final String EN = "EN";//英文
        public static final String ZH_CN = "zh-cn";//中文（简体）
        public static final String ZH_TW = "zh-tw";//繁中
        public static final String EN_US = "zh-cn";//英文
        public static final String LOCAL_DEFAULT_IP = "";
        public static final String ALL_FOR_SALE_ORDER_LIST = "-1";//查询所有的订单信息
        public static final String DEFAULT_BALANCE = "0.0000000000";//设置默认金额显示
    }

    /*请求code*/
    public static class RequestCode {
        public static final int REGISTER_CODE = 0x1;//跳转注册页面的code码
        public static final int RESET_PASSWORD_CODE = 0x2;//跳转重置密码页面的code码
        public static final int BUY_DETAIL_CODE = 0x3;//跳转购买详情页面的code码
        public static final int SELL_DETAIL_CODE = 0x4;//跳转售出详情页面的code码
        public static final int ALL_FUND_CODE = 0x5;//跳转到我的资产页面的code码
        public static final int TURN_OUT = 0x6;//跳转到我的转出页面的code码
        public static final int TURN_IN = 0x7;//跳转到我的转入页面的code码
        public static final int RECHARGE = 0x19;//跳转到我的充值页面的code码
        public static final int PAYMENT_MANAGEMENT = 0x20;//跳转到我的支付管理页面的code码
        public static final int IDENTITY_AUTHENTICATION = 0x21;//跳转到我的身份认证页面的code码
        public static final int SAFETY = 0x8;//跳转到安全中心页面的code码
        public static final int MODIFY_LOGIN_PASSWORD = 0x9;//跳转到「修改登录密码」页面的code码
        public static final int MODIFY_FUND_PASSWORD = 0x10;//跳转到「修改登录密码」页面的code码
        public static final int FUND_PASSWORD = 0x11;//跳转到资金密码页面的code码
        public static final int EMAIL_VERIFY = 0x12;//跳转到「邮箱验证」页面的code码
        public static final int PHONE_VERIFY = 0x13;//跳转到「手机验证」页面的code码
        public static final int GOOGLE_VERIFY = 0x14;//跳转到「google验证」页面的code码
        public static final int REQUEST_CODE_CAMERA_SCAN = 0x15;//跳转到「调用扫描」的界面
        public static final int REQUEST_CODE_CAMERA_OK = 0x16;//跳转到「调用扫描」的界面
        public static final int WIDTH_DRAW_DETAIL = 0x17;//跳转到「转出详情」的界面
        public static final int COUNTRY_CODE = 0x18;//跳转到「选择城市区号」的界面
        public static final int INPUT_PASSWORD_CODE = 0x22;//跳转到「输入密码」的界面
        public static final int ADD_PAYMENT_CODE = 0x23;//跳转到「添加支付方式」的界面

    }

    public static final String SPLICE_CONVERTER(String ip, int port) {
        return HTTP_PREFIX + ip + HTTP_COLON + port;
    }

    /**
     * Http 相关参数/字段
     */
    public class Http {
        public static final String HTTP_PREFIX = "http://";
        public static final String HTTP_COLON = ":";
    }

    /**
     * 程序中需要用的到keymap传值
     */
    public class KeyMaps {
        public static final String From = "from";//来自
        public static final String COPY_ADDRESS = "address";
        public static final String COPY_ACCOUNT = "account";
        public static final String COPY_SECRET = "secret";
        public static final String RESULT = "result";//扫描二维码返回的结果
        public static final String BUY_DETAIL = "buyDetail";
        public static final String SELECT_COUNTRY_CODE = "selectCountryCode";//选择的城市号码

        public static final String SELL_DATA_BEAN = "sellDataBean";//卖出的数据类
        public static final String WITHDRAW_REQUEST_JSON = "withDrawRequestJson";//转出的数据类
    }

    /**
     * 服务器类型
     */
    public static class ServerType {
        public static final String INTERNATIONAL_SIT = "internationalSIT";

    }

    public class Preference {
        public static final String SP_EXCHANGE = "EXCHANGE";
        public static final String ACCESS_TOKEN = "accessToken";//token 信息
        public static final String LANGUAGE_TYPE = "languageType";//當前的語言環境
        public static final String MEMBER_ID = "memberId";//当前的账户信息
        public static final String COOKIE = "cookie";//缓存
    }

    public class From {
        public static final String ORDER_CANCEL_TRANSACTION = "orderCancelTransaction";//撤销订单
        public static final String ORDER_RECHARGE = "orderRecharge";//订单中的转入模块
        public static final String ORDER_WITHDRAW = "orderWithDraw";//订单中的转出模块
        public static final String SELL_VIEW = "sellView";//售出
        public static final String SELL_SELECT_CURRENCY = "sellSelectCurrency";//售出页面选择币种
        public static final String SELL_SELECTED_CURRENCY = "sellSelectedCurrency";//售出页面选择了币种
        public static final String SIDE_SLIP = "sideSlip";//侧滑栏
        public static final String SIDE_SLIP_RESET = "sideSlipReset";//侧滑栏重置数据
        public static final String WITHDRAW_SURE = "withDrawSure";//转出点击确定
        public static final String COUNTRY_CODE = "countryCode";//城市区号
        public static final String TURN_IN = "recharge";//转入
        public static final String TURN_OUT = "withDraw";// 转出
        public static final String BUY = "buy";// 购买
        public static final String LOGIN_PASSWORD = "loginPassword";//登录密码
        public static final String FUND_PASSWORD = "fundPassword";//资金密码
        public static final String MEMBER_PAY_INFO = "memberPayInfo";//会员支付信息
    }

    /**
     * 标识用于Edit的from
     */
    public class EditTextFrom {
        public static final String LOGIN_ACCOUNT = "loginAccount";//登录页面的账户输入框
        public static final String LOGIN_PASSWORD = "loginPassword";//登录页面的密码框
        public static final String EMAIL_CODE = "emailCode";//邮箱验证码
        public static final String REGISTER_VERIFY_EMAIL = "registerVerifyEmail";//注册页面验证邮箱然后发送邮箱验证码
        public static final String FORGET_VERIFY_EMAIL = "forgetVerifyEmail";//忘记密码页面验证邮箱然后发送邮箱验证码
        public static final String WITHDRAW_SCAN = "withDrawScan";//转出页面的扫描二维码
        public static final String PHONE_CODE = "phoneCode";//phone verify
        public static final String WITHDRAW_AMOUNT = "withDrawAmount";//转出金额
    }

    /**
     * 表示动作来向
     */
    public class ActionFrom {
        public static final String GOOGLE_VERIFY = "googleVerify";
        public static final String FUND_PASSWORD = "fundPassword";
        public static final String LOGIN = "login";
    }

    /**
     * 定义一下设置的类型
     */
    public enum SettingType {
        MY_ALL_FUND,//我的资产
        TURN_IN,//转入
        TURN_OUT,//转出
        RECHARGE,
        SAFETY,//安全
        PAYMENT_MANAGEMENT,//支付管理
        IDENTITY_AUTHENTICATION //  身份认证


    }

    /**
     * 用户的信息
     */
    public class User {
        public static final String MEMBER_ID = "Catherineliu@Bcaas.io";
        public static final String MEMBER_PASSWORD = "aaaaaaa1";
    }

    public class TabLayout {
        public static final String SLIDING_TAB_INDICATOR = "slidingTabIndicator";
    }

    /**
     * 表示一些状态信息
     */
    public class Status {
        //txPassword = 0，表示尚未設置資金密碼
        //txPassword = 1，表示尚已設置資金密碼
        public static final String NO_TX_PASSWORD = "0";
        public static final String HAS_TX_PASSWORD = "1";
        //各种验证就是int 类型：0：CLOSE；1：open
        public static final int CLOSE = 0;
        public static final int OPEN = 1;
        public static final int UN_BOUND = 2;
    }

    /**
     * 当前的验证方式
     */
    public class VerifyType {

        public static final String EMAIL = "0";//0:email
        public static final String PHONE = "1";//1:phone
        public static final String GOOGLE = "2";//2:双因素

    }

    /**
     * 存储路径名字
     */
    public class FilePath {
        public static final String COUNTRY_CODE = "country_code";
        public static final String ZH_CN_COUNTRY_CODE = "zh-cn_CountryCode";
        public static final String ZH_TW_COUNTRY_CODE = "zh-tw_CountryCode";
        public static final String EN_US_COUNTRY_CODE = "en-us_CountryCode";

    }

    /**
     * 订单类型
     */
    public class OrderType {
        public static final int TURN_IN = 0;//转入
        public static final int TURN_OUT = 1;//转出
        public static final int TX = 2;//交易
        public static final int RECHARGE = 4;//充值
        public static final int BUY_BACK = 5;//回购
        public static final int BUY = 2;// 买
        public static final int SELL = 3;// 卖
    }

    /**
     * cycle time to choose
     * <p>
     * coinName
     */
    public enum CycleTime {
        oneDay("1D"),
        sevenDay("7D"),
        oneMonth("1M"),
        threeMonth("3M"),
        oneYear("1Y"),
        YTD("YTD"),
        ALL("ALL");

        CycleTime(String s) {
            this.name = s;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class Pattern {
        public static final String EIGHT_DISPLAY = "#,##0.00000000";
        public static final String TEN_DISPLAY = "#,##0.0000000000";
        public static final String TWO_DISPLAY = "#,##0.00";

        public static final String EIGHT = "0.00000000";
        public static final String TEN = "0.0000000000";
        public static final String TWO = "0.00";
    }


    /**
     * 数字精度
     */
    public class DigitalPrecision {
        public static final String BTC = "0.00000001";//BTC的最小单位
        public static final String ETH = "0.0000000001";//ETH的最小单位
        public static final String BCC = "0.00000001";//BCC的最小单位
        public static final int LIMIT_EIGHT = 8;//BCC\BTC 的输入位数限制
        public static final int LIMIT_TWO = 2;//CNYC 的输入位数限制
        public static final int LIMIT_TEN = 10;//ETH的输入位数限制
    }

    public static final String HEADER_NAME_KEY = "cookie";

    // 只能充值SCS,currencyUid帶入“3"
    public static final String CURRENCY_TYPE_SCS = "3";

    public class PayWayUid {
        public static final int BANK = 0;//银行卡支付的UID

    }

    /**
     * 当前币种的UID
     */
    public class CurrencyUID {

        public static final String CNYC = "3";//七彩贝 CNYC

    }

    public class Payment {
        public static final String ADD_PAY_WAY = "addPayWay";
        public static final String GET_PAY_WAY = "getPayWay";
        public static final String REFRESH_GET_PAY_WAY = "refreshGetPayWay";
        public static final String MODIFY_PAY_WAY = "modifyPayWay";
        public static final String REMOVE_PAY_WAY = "removePayWay";
        public static final String CONVERT_COIN = "convertCoin";
        public static final String RECHARGE_VIRTUAL_COIN = "rechargeVirtualCoin";
        public static final String GET_BANK_INFO = "getBankInfo";
        public static final String IDENTITY_NAME_VERIFICATION = "identityNameVerification";
    }

    /**
     * 表示当前的充值数量
     */
    public enum RechargeNumber {
        ONE_HUNDRED("100", false, 1),
        FIVE_HUNDRED("500", false, 2),
        ONE_THOUSAND("1000", false, 3),
        CUSTOM_NUMBER("0", false, 4);
        private boolean isCheck;
        private int index;
        private String number;

        RechargeNumber(String number, boolean isCheck, int index) {
            this.isCheck = isCheck;
            this.index = index;
            this.number = number;
        }

        public boolean getIsCheck(int index) {
            for (RechargeNumber rechargeNumber : RechargeNumber.values()) {
                if (rechargeNumber.index == index) {
                    return rechargeNumber.isCheck;
                }
            }
            return false;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
