package io.bcaas.exchange.constants;

import static io.bcaas.exchange.constants.Constants.Http.HTTP_COLON;
import static io.bcaas.exchange.constants.Constants.Http.HTTP_PREFIX;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/3
 */
public class Constants {


    /**
     * 定时器类型
     */
    public enum TimerType {
        BrandTimer
    }

    /**
     * 时间
     */
    public class time {
        public static final int sleep100 = 100;
        public static final int sleep200 = 200;
        public static final int sleep300 = 300;
        public static final int sleep400 = 400;
        public static final int sleep500 = 500;
        public static final int sleep800 = 800;
        public static final int sleep1000 = 1000;
        public static final int sleep1500 = 1500;
        public static final int sleep2000 = 2000;
        public static final int sleep3000 = 3000;
        public static final int sleep4000 = 4000;
        public static final int sleep5000 = 5000;
        public static final int sleep10000 = 10000;
        public static final int sleep20000 = 20000;
        public static final int sleep30000 = 30000;
        public static final int sleep50000 = 50000;
        public static final int sleep500000 = 500000;
    }

    public class color {
        //二維碼渲染的前景色
        public static final int foregroundOfQRCode = 0xff000000;
        //二維碼渲染的背景色
        public static final int backgroundOfQRCode = 0x00000000;
    }

    public class ValueMaps {

        public static final int TOAST_SHORT = 0;
        public static final int TIME_OUT_TIME_LONG = 30;//设置超时时间
        //TCP  C-S 发送心跳信息间隔
        public static final long HEART_BEAT_TIME = 30;
        public static final int PASSWORD_MIN_LENGTH = 8;// 输入密码的最小长度
        public static final String FROM_BRAND = "brand";
        //打印当前设备的内存
        public static final long LOG_TIME = 1;
        public static final int STAY_BRAND_ACTIVITY_TIME = 1;//如果当前不用编辑页面，停留在页面的时间1s


        public static final String SC = "SC";//中文（简体）
        public static final String CN = "CN";//中文
        public static final String TC = "TC";//中文（繁體）
        public static final String TW = "TW";//中文（繁體）
        public static final String EN = "EN";//英文
        public static final String ZH_CN = "zh-cn";//中文（简体）
        public static final String ZH_TW = "zh-tw";//繁中
        public static final String EN_US = "en-us";//英文

        public static final String LOCAL_DEFAULT_IP = "";


    }

    /*请求code*/
    public static class RequestCode {
        public static final int REGISTER_CODE = 0x1;//跳转注册页面的code码
        public static final int RESET_PASSWORD_CODE = 0x2;//跳转重置密码页面的code码
        public static final int BUY_DETAIL_CODE = 0x3;//跳转购买详情页面的code码
        public static final int SELL_DETAIL_CODE = 0x4;//跳转售出详情页面的code码
        public static final int ALL_FUND_CODE = 0x5;//跳转到我的资产页面的code码
        public static final int WITH_DRAW = 0x6;//跳转到我的提现页面的code码
        public static final int RECHARGE = 0x7;//跳转到我的充值页面的code码
        public static final int SAFETY_CENTER = 0x8;//跳转到安全中心页面的code码
        public static final int MODIFY_LOGIN_PASSWORD = 0x9;//跳转到「修改登录密码」页面的code码
        public static final int FUND_PASSWORD = 0x10;//跳转到资金密码页面的code码
        public static final int EMAIL_VERIFY = 0x11;//跳转到「邮箱验证」页面的code码
        public static final int PHONE_VERIFY = 0x13;//跳转到「手机验证」页面的code码
        public static final int GOOGLE_VERIFY = 0x14;//跳转到「google验证」页面的code码
        public static final int REQUEST_CODE_CAMERA_SCAN = 0x15;//跳转到「调用扫描」的界面
        public static final int REQUEST_CODE_CAMERA_OK = 0x16;//跳转到「调用扫描」的界面

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
        public static final String RESULT = "result";//扫描二维码返回的结果
        public static final String BUY_DETAIL = "buyDetail";
    }

    /**
     * 服务器类型
     */
    public static class ServerType {
        public static final String INTERNATIONAL_SIT = "internationalSIT";
        public static final String INTERNATIONAL_UAT = "internationalUAT";
        public static final String INTERNATIONAL_PRD = "internationalPRD";

    }

    /**
     * 切换服务器类型显示名字
     */
    public static class ServerTypeName {
        public static final String INTERNATIONAL_SIT = "国际 SIT";
        public static final String INTERNATIONAL_UAT = "国际 UAT";
        public static final String INTERNATIONAL_PRD = "国际 PRD";

    }


    public class Preference {
        public static final String SP_EXCHANGE = "EXCHANGE";
        public static final String ACCESS_TOKEN = "accessToken";//token 信息
        public static final String LANGUAGE_TYPE = "languageType";//當前的語言環境
    }

    public class From {
        public static final String ORDER_TRANSACTION = "orderTransaction";//订单中的交易模块
        public static final String ORDER_RECHARGE = "orderRecharge";//订单中的充值模块
        public static final String ORDER_WITHDRAW = "orderWithDraw";//订单中的提现模块
        public static final String SELL_VIEW = "sellView";//售出
        public static final String SIDE_SLIP = "sideSlip";//侧滑栏
    }

    /**
     * 标识用于Edit的from
     */
    public class EditTextFrom {
        public static final String LOGIN_AMOUNT = "loginAmount";//登录页面的账户输入框
        public static final String LOGIN_PASSWORD = "loginPassword";//登录页面的密码框
        public static final String REGISTER_EMAIL_CODE = "registerEmailCode";//注册页面的邮箱验证码
        public static final String WITHDRAW_SCAN = "withDrawScan";//提现页面的扫描二维码
    }

    /**
     * 定义一下设置的类型
     */
    public enum SettingType {
        MY_ALL_FUND,//我的资产
        RECHARGE,//充值
        WITH_DRAW,//提现
        SAFETY_CENTER//安全中心
    }

    /**
     * 用户的信息
     */
    public class User {
        public static final String MEMBER_ID = "Catherineliu@Bcaas.io";
        public static final String MEMBER_PASSWORD = "aaaaaaa1";
        public static final String MEMBER_REALIP = "192.168.1.1";
    }
}
