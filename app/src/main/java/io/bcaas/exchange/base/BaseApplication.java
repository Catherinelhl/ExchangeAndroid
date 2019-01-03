package io.bcaas.exchange.base;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.squareup.otto.Subscribe;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.event.NetStateChangeEvent;
import io.bcaas.exchange.receiver.NetStateReceiver;
import io.bcaas.exchange.tools.*;
import io.bcaas.exchange.tools.device.DeviceTool;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/3
 * 當前APP's Application,一些整個APP需要公用的常數、變量、SP相關的存儲統一在此類。也包括獲取當前設備的一些尺寸以及硬件信息
 */
public class BaseApplication extends MultiDexApplication {
    public static String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication instance;

    /*屏幕的寬*/
    protected static int screenWidth;
    /*屏幕的高*/
    protected static int screenHeight;
    /*SP存儲工具類*/
    private static PreferenceTool preferenceTool;
    /*判断当前程序是否真的有网*/
    private static boolean realNet = true;
    /*当前的语言环境,默认是英文*/
    private static boolean isZH;
    /*存储当前的Token*/
    private static String token;
    /*得到当前是否设置了资金密码*/
    private static boolean setFundPassword;
    /*当前的语言环境*/
    private static String currentLanguage;
    /*是否是手机版*/
    private static boolean isPhone;
    /*存储当前所有币种余额信息*/
    private static List<MemberKeyVO> memberKeyVOList;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化SP
        preferenceTool = PreferenceTool.getInstance(context());
        getScreenMeasure();
        registerNetStateReceiver();
        ServerTool.initServerData();

    }

    public static Context context() {
        return instance.getApplicationContext();
    }


    public static boolean isIsPhone() {
        return isPhone;
    }

    public static void setIsPhone(boolean isPhone) {
        BaseApplication.isPhone = isPhone;
    }

    public static void setAccessToken(String accessToken) {
        token = accessToken;
    }

    public static String getToken() {
        return token;
    }

    public static boolean isSetFundPassword() {
        return setFundPassword;
    }

    public static void setFundPassword(boolean setFundPassword) {
        BaseApplication.setFundPassword = setFundPassword;
    }

    public static String getCurrentLanguage() {
        //3:匹配當前的語言獲取，返回APP裡面識別的TAG
        if (StringTool.equals(currentLanguage, Constants.ValueMaps.SC)) {
//            return currentString;
            return "zh-cn";
        } else if (StringTool.equals(currentLanguage, Constants.ValueMaps.TC)) {
//            return currentString;
            return "zh-tw";
        } else {
//            return Constants.ValueMaps.EN;
            return "en-us";

        }
    }

    public static void setCurrentLanguage(String currentLanguage) {
        BaseApplication.currentLanguage = currentLanguage;
    }

    public static String getMemberId() {
        // TODO: 2019/1/3 暂时这样写，到时候应该存储起来
        return Constants.User.MEMBER_ID;
    }

    public static List<MemberKeyVO> getMemberKeyVOList() {
        return memberKeyVOList;
    }

    public static void setMemberKeyVOList(List<MemberKeyVO> memberKeyVOList) {
        BaseApplication.memberKeyVOList = memberKeyVOList;
    }

    /*注册网络变化的监听*/
    private void registerNetStateReceiver() {
        NetStateReceiver netStateReceiver = new NetStateReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(netStateReceiver, intentFilter);
    }

    /*得到当前屏幕的尺寸*/
    private void getScreenMeasure() {
        DisplayMetrics displayMetrics = getDisplayMetrics();
        if (displayMetrics != null) {
            screenWidth = displayMetrics.widthPixels;
            screenHeight = displayMetrics.heightPixels;
            // 屏幕密度（1.0 / 1.5 / 2.0）
            float density = displayMetrics.density;
            // 屏幕密度DPI（160 / 240 / 320）
            int densityDpi = displayMetrics.densityDpi;
            String info = " 设备型号: " + android.os.Build.MODEL
                    + ",\nSDK版本:" + android.os.Build.VERSION.SDK
                    + ",\n系统版本:" + android.os.Build.VERSION.RELEASE + "\n "
                    + MessageConstants.SCREEN_WIDTH + screenWidth
                    + "\n " + MessageConstants.SCREEN_HEIGHT + screenHeight
                    + "\n屏幕密度:  " + density
                    + "\n屏幕密度DPI: " + densityDpi;
            LogTool.d(TAG, MessageConstants.DEVICE_INFO + info);
        }
        setIsPhone(DeviceTool.checkIsPhone(context()));
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context().getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics;
        } else {
            return null;
        }
    }

    /**
     * 從SP裡面獲取數據
     *
     * @param key
     * @return
     */
    public static String getStringFromSP(String key) {
        if (preferenceTool == null) {
            preferenceTool = PreferenceTool.getInstance(context());
        }
        return preferenceTool.getString(key);
    }

    public static Boolean getBooleanFromSP(String key) {
        if (preferenceTool == null) {
            preferenceTool = PreferenceTool.getInstance(context());
        }
        return preferenceTool.getBoolean(key);
    }


    /**
     * 往SP裡面存儲數據
     *
     * @param key
     * @param value
     */
    public static void setStringToSP(String key, String value) {
        if (preferenceTool == null) {
            preferenceTool = PreferenceTool.getInstance(context());
        }
        preferenceTool.saveString(key, value);
    }

    public static void setBooleanToSP(String key, Boolean value) {
        if (preferenceTool == null) {
            preferenceTool = PreferenceTool.getInstance(context());
        }
        preferenceTool.saveBoolean(key, value);
    }


    /*检测当前网络是否是真的*/
    public static boolean isRealNet() {
        return realNet;
    }

    @Subscribe
    public void netChanged(NetStateChangeEvent stateChangeEvent) {
        if (stateChangeEvent.isConnect()) {
            setRealNet(true);
        } else {
            setRealNet(false);
        }
    }

    public static void setRealNet(boolean realNet) {
        BaseApplication.realNet = realNet;
    }

    /**
     * 返回当前的token是否为空
     *
     * @return
     */
    public static boolean tokenIsNull() {
        String accessToken = getStringFromSP(Constants.Preference.ACCESS_TOKEN);
        return StringTool.isEmpty(accessToken);
    }
}
