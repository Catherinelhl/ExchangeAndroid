package io.bcaas.exchange.tools.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/31
 * 工具類：用來管理當前啟動的Activity
 */
public class ActivityTool {
    private static String TAG = ActivityTool.class.getSimpleName();

    private static ActivityTool activityTool;
    private Map<String, Activity> activityMap = new HashMap<>();//用來存儲加入的activity

    public static ActivityTool getInstance() {
        if (activityTool == null) {
            activityTool = new ActivityTool();

        }
        return activityTool;
    }

    /**
     * 保存指定key值的activity（activity启动时调用）
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        String key = String.valueOf(System.currentTimeMillis());
        if (activityMap.get(key) == null) {
            activityMap.put(key, activity);
        }
    }

    /**
     * 移除指定key值的activity （activity关闭时调用）
     */
    public void removeActivity(Activity activity) {
        String key = String.valueOf(System.currentTimeMillis());
        if (activity != null) {
            if (activity.isDestroyed() || activity.isFinishing()) {
                activityMap.remove(key);
                return;
            }
            activity.finish();
            activityMap.remove(key);
        }
    }

    /**
     * 移除所有的Activity
     */
    public void removeAllActivity() {
        for (Map.Entry<String, Activity> entry : activityMap.entrySet()) {
            Activity activity = entry.getValue();
            finishAty(activity);
        }
    }

    /**
     * 关闭指定Activity
     *
     * @param aty
     */
    private void finishAty(Activity aty) {
        if (aty != null && !aty.isFinishing()) {
            aty.finish();
        }
    }

    /**
     * 推出当前应用
     */
    public void exit() {
//        MobclickAgent.onKillProcess(context());
        killProcess();
        System.exit(0);
    }

    /**
     * 杀掉所有进程
     */
    public void killProcess() {
        removeAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 检测某Activity是否在当前Task的栈顶
     * appointClassName：指定类名称
     */
    public static boolean isTopActivity(String appointClassName, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        String topClassName = null;
        if (null != runningTaskInfo) {
            topClassName = (runningTaskInfo.get(0).topActivity.getShortClassName()).toString();
        }
        if (StringTool.isEmpty(topClassName)) {
            return false;
        }
        LogTool.e(TAG, topClassName + "类存在于栈顶；指定类：" + appointClassName + "   返回Boolean值：" + topClassName.contains(appointClassName));
        return topClassName.contains(appointClassName);
    }

    /**
     * 删除所有的activity，除了当前这一个
     *
     * @param className 当前需要保留的Activity
     */
    public void removeAllActivityExceptIt(String className) {
        for (Map.Entry<String, Activity> entry : activityMap.entrySet()) {
            Activity activity = entry.getValue();
            if (!StringTool.equals(className, activity.getClass().getSimpleName())) {
                finishAty(activity);
            }
        }
    }
}
