package io.bcaas.exchange.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.event.NetStateChangeEvent;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.otto.OttoTool;

import java.time.temporal.TemporalAccessor;

/**
 * @author catherine.brainwilliam
 * 廣播：監聽系統網絡變化然後進行廣播
 */
public class NetStateReceiver extends BroadcastReceiver {
    private String TAG = NetStateReceiver.class.getSimpleName();
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public NetStateReceiver() {
        OttoTool.getInstance().register(BaseApplication.context());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
                LogTool.d(TAG, "netChanged");
                OttoTool.getInstance().post(new NetStateChangeEvent(false));
            } else {
                LogTool.d(TAG, "netChanged");
                OttoTool.getInstance().post(new NetStateChangeEvent(true));
            }
        }
    }

    /**
     if (info == null || !info.isConnectedOrConnecting()) {
     setThreadCount(DEFAULT_THREAD_COUNT);
     return;
     }
     switch (info.getType()) {
     case ConnectivityManager.TYPE_WIFI:
     case ConnectivityManager.TYPE_WIMAX:
     case ConnectivityManager.TYPE_ETHERNET:
     setThreadCount(4);
     break;
     case ConnectivityManager.TYPE_MOBILE:
     switch (info.getSubtype()) {
     case TelephonyManager.NETWORK_TYPE_LTE:  // 4G
     case TelephonyManager.NETWORK_TYPE_HSPAP:
     case TelephonyManager.NETWORK_TYPE_EHRPD:
     setThreadCount(3);
     break;
     case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
     case TelephonyManager.NETWORK_TYPE_CDMA:
     case TelephonyManager.NETWORK_TYPE_EVDO_0:
     case TelephonyManager.NETWORK_TYPE_EVDO_A:
     case TelephonyManager.NETWORK_TYPE_EVDO_B:
     setThreadCount(2);
     break;
     case TelephonyManager.NETWORK_TYPE_GPRS: // 2G
     case TelephonyManager.NETWORK_TYPE_EDGE:
     setThreadCount(1);
     break;
     default:
     setThreadCount(DEFAULT_THREAD_COUNT);
     }
     break;
     default:
     setThreadCount(DEFAULT_THREAD_COUNT);
     }
     */
}
