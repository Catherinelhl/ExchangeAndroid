package io.bcaas.exchange.tools.device;

import android.content.Context;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.LogTool;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @projectName: BcaasAndroid
 * @packageName: io.bcaas.tools
 * @author: catherine
 * @time: 2018/9/12
 * <p>
 * 工具類：设备信息
 */
public class DeviceTool {
    private static String TAG = DeviceTool.class.getSimpleName();


    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    /**
     * 获取有限网IP
     *
     * @return
     */
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumeration = networkInterface
                        .getInetAddresses(); enumeration.hasMoreElements(); ) {
                    InetAddress inetAddress = enumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            LogTool.d(TAG, ex.getMessage());
        }
        return Constants.ValueMaps.LOCAL_DEFAULT_IP;

    }

    /**
     * 判断当前是否是TV
     * <p>
     * 电视和手机的差异：
     * 屏幕物理尺寸不同。
     * 布局尺寸不同。
     * SIM 卡的状态不同。
     * 电源接入的方式不同。
     */
    //检查当前屏幕尺寸,小于6.5认为是手机，否则是电视
    private static boolean checkScreenIsPhone() {
        DisplayMetrics displayMetrics = BaseApplication.getDisplayMetrics();
        if (displayMetrics != null) {
            double x = Math.pow(displayMetrics.widthPixels / displayMetrics.xdpi, 2);
            double y = Math.pow(displayMetrics.heightPixels / displayMetrics.ydpi, 2);
            LogTool.d(TAG, x);
            LogTool.d(TAG, y);
            //屏幕尺寸
            double screenInches = Math.sqrt(x + y);
            return screenInches < 6.5;
        }
        return false;
    }

    /**
     * 通过检查布局是否是手机
     *
     * @param context
     * @return
     */
    private static boolean checkLayoutIsPhone(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) <= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

    /**
     * 检查SIM信息来比对是否是TV
     *
     * @param context
     * @return
     */
    private static boolean checkSIMStatusIsPhone(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 检查当前是否是手机
     *
     * @param context
     * @return
     */
    public static boolean checkIsPhone(Context context) {
        return checkLayoutIsPhone(context) && checkSIMStatusIsPhone(context);
    }

    /**
     * 得到当前设备的model
     *
     * @return
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }


}
