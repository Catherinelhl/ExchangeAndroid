package io.bcaas.exchange.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.manager.SoftKeyBroadManager;
import io.bcaas.exchange.tools.OttoTool;
import io.bcaas.exchange.view.dialog.BcaasDialog;
import io.bcaas.exchange.view.dialog.BcaasLoadingDialog;
import io.bcaas.exchange.view.dialog.BcaasSingleDialog;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/4
 * 所有Phone's Activity 的基類
 */
public abstract class BaseActivity extends AppCompatActivity {
    private String TAG = BaseActivity.class.getSimpleName();
    private Unbinder unbinder;
    protected Context context;
    protected Activity activity;
    /*双按钮弹框*/
    private BcaasDialog bcaasDialog;
    /*单按钮弹框*/
    private BcaasSingleDialog bcaasSingleDialog;
    private BcaasLoadingDialog bcaasLoadingDialog;
    /*键盘输入管理*/
    private InputMethodManager inputMethodManager;
    /*存儲當前點擊返回按鍵的時間，用於提示連續點擊兩次才能退出*/
    private long lastClickBackTime = 0L;
    /*软键盘管理*/
    protected SoftKeyBroadManager softKeyBroadManager;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        activity = this;
        context = getApplicationContext();
        unbinder = ButterKnife.bind(this);
        OttoTool.getInstance().register(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initData();
        initListener();
        checkNetState();
    }

    public abstract int getContentView();

    public abstract void getArgs(Bundle bundle);

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();


    /**
     * 从当前页面跳转到另一个页面
     *
     * @param classTo
     */
    public void intentToActivity(Class classTo) {
        intentToActivity(null, classTo);
    }

    /**
     * @param finishFrom 是否关闭上一个activity，默认是不关闭 false
     */
    public void intentToActivity(Class classTo, boolean finishFrom) {
        intentToActivity(null, classTo, finishFrom);
    }

    /**
     * @param bundle 存储当前页面需要传递的数据
     */
    public void intentToActivity(Bundle bundle, Class classTo) {
        intentToActivity(bundle, classTo, false);
    }

    public void intentToActivity(Bundle bundle, Class classTo, Boolean finishFrom) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(this, classTo);
        startActivity(intent);
        if (finishFrom) {
            this.finish();
        }
    }

    /**
     * 获取当前手机的网络状态
     *
     * @return
     */
    private boolean checkNetState() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                BaseApplication.setRealNet(true);
                return true;
            } else {
                BaseApplication.setRealNet(false);
                showToast(getResources().getString(R.string.network_not_reachable));
                return false;
            }
        }
        return false;

    }


    public void showToast(String toastInfo) {
        showToast(toastInfo, Constants.ValueMaps.TOAST_SHORT);
    }

    /**
     * @param toastInfo    提示信息
     * @param durationMode 提示展示时间长短的模式
     */
    public void showToast(String toastInfo, int durationMode) {
        Message message = new Message();
        message.obj = toastInfo;
        message.what = durationMode;//0：short；1：Long

    }

    /*隱藏當前軟鍵盤*/
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
