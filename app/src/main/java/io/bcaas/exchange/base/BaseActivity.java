package io.bcaas.exchange.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.obt.qrcode.activity.CaptureActivity;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.HideSoftKeyBoardListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.manager.DataGenerationManager;
import io.bcaas.exchange.manager.SoftKeyBroadManager;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.app.ActivityTool;
import io.bcaas.exchange.tools.app.PreferenceTool;
import io.bcaas.exchange.tools.otto.OttoTool;
import io.bcaas.exchange.tools.regex.RegexTool;
import io.bcaas.exchange.ui.activity.GoogleVerifyActivity;
import io.bcaas.exchange.ui.activity.LoginActivity;
import io.bcaas.exchange.ui.activity.SetFundPasswordActivity;
import io.bcaas.exchange.view.dialog.DoubleButtonDialog;
import io.bcaas.exchange.view.dialog.EditTextDialog;
import io.bcaas.exchange.view.dialog.LoadingDialog;
import io.bcaas.exchange.view.dialog.SingleButtonDialog;
import io.bcaas.exchange.view.pop.ListPop;
import io.bcaas.exchange.view.pop.StringListPop;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.ResponseJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/4
 * 所有Phone's Activity 的基類
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseContract.View {
    private Unbinder unbinder;
    protected Context context;
    protected Activity activity;
    /*双按钮弹框*/
    private DoubleButtonDialog doubleButtonDialog;
    /*双按钮带输入框的弹框*/
    private EditTextDialog editTextDialog;
    /*单按钮弹框*/
    private SingleButtonDialog singleDialog;
    private LoadingDialog loadingDialog;
    /*键盘输入管理*/
    private InputMethodManager inputMethodManager;
    /*存儲當前點擊返回按鍵的時間，用於提示連續點擊兩次才能退出*/
    private long lastClickBackTime = 0L;
    /*软键盘管理*/
    protected SoftKeyBroadManager softKeyBroadManager;
    protected String TAG;
    protected DataGenerationManager dataGenerationManager;
    /*显示地区号的Pop Window*/
    private ListPop listPop;
    /*显示银行卡*/
    private StringListPop stringListPop;
    /*字体*/
    protected Typeface tfRegular;
    protected Typeface tfLight;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgs(getIntent().getExtras());
        setContentView(getContentView());
        activity = this;
        TAG = activity.getClass().getSimpleName();
        //将当前的Activity加入管理器
        ActivityTool.getInstance().addActivity(this);
        //设置字体
        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        context = getApplicationContext();
        unbinder = ButterKnife.bind(this);
        //注册OTTO事件
        OttoTool.getInstance().register(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        dataGenerationManager = new DataGenerationManager();

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

    /**
     * 通过传入的View 来隐藏当前的软键盘
     *
     * @param view
     */
    protected void hideSoftKeyBoardByTouchView(View view) {
        if (view == null) {
            return;
        }
        view.setOnTouchListener((v, event) -> {
            hideSoftKeyboard();
            return false;
        });


    }


    public void showToast(String toastInfo) {
        showToast(toastInfo, false);
    }

    public void showToast(String toastInfo, boolean isShort) {
        runOnUiThread(() -> {
            Toast toast = Toast.makeText(activity, "", isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
            /*解决小米手机toast自带包名的问题*/
            toast.setText(toastInfo);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });
    }

    /*隱藏當前軟鍵盤*/
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //''zh-cn_CountryCde.json" : 英文
    //"zh-tw_CountryCde" : 繁中
    //"zh-cn" : 簡中
    /*獲取當前語言環境*/
    protected String getCurrentLanguage() {
        // 1：檢查應用是否已經有用戶自己存儲的語言種類
        String currentString = PreferenceTool.getInstance().getString(Constants.Preference.LANGUAGE_TYPE);
        if (StringTool.isEmpty(currentString)) {
            //2:當前的選中為空，那麼就默認讀取當前系統的語言環境
            Locale locale = getResources().getConfiguration().locale;
            //locale.getLanguage();//zh  是中國
            currentString = locale.getCountry();//CN-簡體中文，TW、HK-繁體中文
        }
        //3:匹配當前的語言獲取，返回APP裡面識別的TAG
        if (StringTool.equals(currentString, Constants.ValueMaps.SC)
                || StringTool.equals(currentString, Constants.ValueMaps.CN)) {
            return Constants.ValueMaps.ZH_CN;
        } else if (StringTool.equals(currentString, Constants.ValueMaps.TC)
                || StringTool.equals(currentString, Constants.ValueMaps.TW)) {
            return Constants.ValueMaps.ZH_TW;
        } else {
            return Constants.ValueMaps.EN_US;

        }
    }

    /**
     * 獲得照相機權限
     */
    protected void requestCameraPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {//这个说明系统版本在6.0之下，不需要动态获取权限
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //先判断有没有权限 ，没有就在这里进行权限的申请
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA}, Constants.RequestCode.REQUEST_CODE_CAMERA_OK);
            } else {
                //说明已经获取到摄像头权限了 想干嘛干嘛
                intentToCaptureActivity();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.RequestCode.REQUEST_CODE_CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //这里已经获取到了摄像头的权限，想干嘛干嘛了可以
                    intentToCaptureActivity();
                } else {
                    //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
                    showToast(getString(R.string.to_setting_grant_permission));
                }
                break;
        }
    }

    private void intentToCaptureActivity() {
        startActivityForResult(new Intent(this, CaptureActivity.class), Constants.RequestCode.REQUEST_CODE_CAMERA_SCAN);

    }


    /**
     * 关闭当前页面，返回上一个页面
     *
     * @param isBack
     */
    protected void setResult(boolean isBack) {
        hideSoftKeyboard();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.KeyMaps.From, isBack);
        intent.putExtras(bundle);
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * 为弹出的popWindow设置背景透明度
     *
     * @param bgAlpha
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }


    /**
     * 根据传入的参数来捕捉用户点击的此时
     *
     * @param times 次数
     * @return
     */
    private int clickTimes = 0;

    public boolean multipleClickToDo(int times) {
        if ((System.currentTimeMillis() - lastClickBackTime) > Constants.Time.sleep2000) {
            clickTimes = 1;
            lastClickBackTime = System.currentTimeMillis();
            return false;
        } else {
            clickTimes++;
            if (clickTimes == times) {
                lastClickBackTime = 0;
                return true;
            } else if (clickTimes < times) {
                lastClickBackTime = System.currentTimeMillis();
                return false;
            }
            clickTimes = 0;
            return false;
        }
    }

    /**
     * 显示当前所有的币种信息
     *
     * @param onItemSelectListener 通過傳入的回調來得到選擇的值
     */
    public void showCurrencyListPopWindow(CurrencyListVO currencyListVOFilter, OnItemSelectListener onItemSelectListener) {
        //1： 對當前pop window進行置空
        if (listPop != null) {
            listPop.dismiss();
            listPop = null;
        }
        //2：拿取当前的账户信息
        List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
        if (ListTool.isEmpty(memberKeyVOList)) {
            return;
        }
        //3：定义一个新的数组，用于存储待会过滤后的数据
        List<MemberKeyVO> memberKeyVOAfterFilter = new ArrayList<>();
        //4：判断当前传进来的token type是否为空
        if (currencyListVOFilter != null) {
            for (MemberKeyVO memberKeyVOTemp : memberKeyVOList) {
                if (memberKeyVOTemp != null) {
                    CurrencyListVO currencyListVO = memberKeyVOTemp.getCurrencyListVO();
                    //5：比较当前的数据，然后过滤掉当前显示的token type
                    if (currencyListVO != null) {
                        //6：比对两者的UID是否一样，如果一样，就需要过滤掉这个数据,否则添加进新数组里面
                        if (!StringTool.equals(currencyListVO.getCurrencyUid(),
                                currencyListVOFilter.getCurrencyUid())) {
                            memberKeyVOAfterFilter.add(memberKeyVOTemp);

                        }
                    }
                }
            }
        } else {
            memberKeyVOAfterFilter.addAll(memberKeyVOList);
        }
        //7：开始初始化pop
        listPop = new ListPop(context);
        listPop.addList(onItemSelectListener, memberKeyVOAfterFilter);
        listPop.setOnDismissListener(() -> setBackgroundAlpha(1f));
        //设置layout在PopupWindow中显示的位置
        listPop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        setBackgroundAlpha(0.7f);
    }

    public void showListPopWindow(List<String> strings, OnItemSelectListener onItemSelectListener) {
        //1： 對當前pop window進行置空
        if (stringListPop != null) {
            stringListPop.dismiss();
            stringListPop = null;
        }
        //2：开始初始化pop
        stringListPop = new StringListPop(context);
        stringListPop.addList(onItemSelectListener, strings);
        stringListPop.setOnDismissListener(() -> setBackgroundAlpha(1f));
        //设置layout在PopupWindow中显示的位置
        stringListPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        setBackgroundAlpha(0.7f);
    }


    public boolean checkActivityState() {
        return activity != null && !activity.isFinishing();
    }

    @Override
    public void showLoading() {
        if (!checkActivityState()) {
            return;
        }
        hideLoading();
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setProgressBarColor(getResources().getColor(R.color.button_color));
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog.cancel();
            loadingDialog = null;

        }
    }

    @Override
    public void noNetWork() {
        showToast(getResources().getString(R.string.network_not_reachable));

    }

    @Override
    public void noData() {
        LogTool.d(TAG, getString(R.string.no_data));

    }

    /**
     * 跳转到登录页面
     */
    private void intentToLoginActivity() {
        intentToActivity(LoginActivity.class, true);

    }

    /**
     * 显示双按钮对话框
     *
     * @param message
     * @param listener
     */
    public void showDoubleButtonDialog(String message, DoubleButtonDialog.ConfirmClickListener listener) {
        this.showDoubleButtonDialog(getString(R.string.warning), getString(R.string.cancel), getString(R.string.confirm), message, listener);
    }

    /**
     * 显示双按钮对话框
     *
     * @param left
     * @param right
     * @param message
     * @param listener
     */
    public void showDoubleButtonDialog(String left, String right, String message, DoubleButtonDialog.ConfirmClickListener listener) {
        this.showDoubleButtonDialog(getString(R.string.warning), left, right, message, listener);
    }

    /**
     * 显示双按钮对话框
     *
     * @param title
     * @param left
     * @param right
     * @param message
     * @param listener
     */
    public void showDoubleButtonDialog(String title, String left, String right, String message, DoubleButtonDialog.ConfirmClickListener listener) {
        if (doubleButtonDialog == null) {
            doubleButtonDialog = new DoubleButtonDialog(this);
        }
        /*设置弹框点击周围不予消失*/
        doubleButtonDialog.setCanceledOnTouchOutside(false);
        /*设置弹框背景*/
        doubleButtonDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white));
        doubleButtonDialog.setLeftText(left)
                .setRightText(right)
                .setContent(message)
                .setTitle(title)
                .setOnConfirmClickListener(new DoubleButtonDialog.ConfirmClickListener() {
                    @Override
                    public void sure() {
                        listener.sure();
                        doubleButtonDialog.dismiss();
                    }

                    @Override
                    public void cancel() {
                        listener.cancel();
                        doubleButtonDialog.dismiss();
                    }
                }).show();
    }

    /**
     * 显示带输入框的双按钮对话框
     *
     * @param message
     * @param listener
     */
    public void showEditTextDialog(String title, String message, EditTextDialog.ConfirmClickListener listener) {
        if (editTextDialog == null) {
            editTextDialog = new EditTextDialog(this);
        }
        /*设置弹框点击周围不予消失*/
        editTextDialog.setCanceledOnTouchOutside(false);
        /*设置弹框背景*/
        editTextDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white));
        editTextDialog.setLeftText(getString(R.string.cancel))
                .setRightText(getString(R.string.sure))
                .setContent(message)
                .setTitle(title)
                .setOnConfirmClickListener(new EditTextDialog.ConfirmClickListener() {
                    @Override
                    public void sure(String name) {
                        //step 1:判断当前是否输入内容
                        if (StringTool.isEmpty(name)) {
                            showToast(getString(R.string.please_input_identity_name));
                            return;
                        }
                        //step 2：判断当前输入的内容是否满足用户名需求
                        if (!RegexTool.isIdentityName(name)) {
                            showToast(getString(R.string.identity_name_wrong_regex));
                            return;
                        }
                        listener.sure(name);
                        editTextDialog.dismiss();
                    }

                    @Override
                    public void cancel() {
                        listener.cancel();
                        editTextDialog.dismiss();
                    }
                }).show();
    }

    public void showSingleDialog(String message, SingleButtonDialog.ConfirmClickListener listener) {
        this.showSingleDialog(getResources().getString(R.string.warning), message, listener);
    }

    /**
     * 显示单按钮对话框
     *
     * @param title
     * @param message
     * @param listener
     */
    public void showSingleDialog(String title, String message, SingleButtonDialog.ConfirmClickListener listener) {
        if (!checkActivityState()) {
            return;
        }
        if (singleDialog == null) {
            singleDialog = new SingleButtonDialog(this);
        }
        /*设置弹框点击周围不予消失*/
        singleDialog.setCanceledOnTouchOutside(false);
        singleDialog.setCancelable(false);
        /*设置弹框背景*/
        singleDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_white));
        singleDialog.setContent(message)
                .setTitle(title)
                .setOnConfirmClickListener(() -> {
                    listener.sure();
                    singleDialog.dismiss();
                }).show();
    }

    /**
     * 取消双按钮弹框
     */
    private void dismissDoubleButtonDialog() {
        if (doubleButtonDialog != null) {
            doubleButtonDialog.dismiss();
            doubleButtonDialog.cancel();
            doubleButtonDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        dismissDoubleButtonDialog();
    }

    @Override
    public boolean httpExceptionDisposed(ResponseJson responseJson) {
        if (responseJson == null) {
            return true;
        }
        int code = responseJson.getCode();
        //判断是否是Token过期，弹出提示重新登录，然后跳转界面
        if (code == MessageConstants.CODE_2019
                || code == MessageConstants.CODE_2016
                || code == MessageConstants.CODE_2018
                || code == MessageConstants.CODE_2043) {
            showLogoutDialog();
            return true;
        } else if (code == MessageConstants.CODE_2005) {
            showLogoutDialog(getString(R.string.please_register_email_first));
            return true;
        }
        return false;
    }

    /**
     * 显示登出的弹框
     */
    public void showLogoutDialog() {
        showLogoutDialog(getString(R.string.please_login_again));
    }

    /**
     * 显示登出的弹框
     */
    public void showLogoutDialog(String info) {
        //清空当前的token
        BaseApplication.clearTokenAndMemberId();
        //    {"success":false,"code":2019,"message":"AccessToken expire."}
        showSingleDialog(getString(R.string.warning),
                info, () -> {
                    //跳转到登录
                    intentToLoginActivity();
                });
    }

    /**
     * 跳转到设置google验证密码的页面
     */
    protected void intentToGoogleVerifyActivity(Activity activity) {
        //跳转到google验证
        Intent intent = new Intent();
        intent.setClass(activity, GoogleVerifyActivity.class);
        startActivityForResult(intent, Constants.RequestCode.GOOGLE_VERIFY);
    }

    /**
     * 跳转到设置资金密码的页面
     */
    protected void intentToSetFundPasswordActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, SetFundPasswordActivity.class);
        startActivityForResult(intent, Constants.RequestCode.FUND_PASSWORD);
    }


    protected OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            if (StringTool.notEmpty(from)) {
                //如果当前是资金密码，那么本地对用户是否设置了资金密码进行判断
                MemberVO memberVO = BaseApplication.getMemberVO();
                switch (from) {
                    case Constants.ActionFrom.GOOGLE_VERIFY:
                        // 如果当前有账户信息，那么本地替用户进行密码设置的判断
                        if (memberVO != null) {
                            //判断是否设置「资金密码」
                            int googleVerify = memberVO.getTwoFactorAuthVerify();
                            if (googleVerify == Constants.Status.OPEN) {
                                showToast(getString(R.string.have_google_verify));

                            } else {
                                intentToGoogleVerifyActivity(activity);
                            }
                        } else {
                            intentToGoogleVerifyActivity(activity);
                        }
                        break;
                    case Constants.ActionFrom.FUND_PASSWORD:
                        // 如果当前有账户信息，那么本地替用户进行密码设置的判断
                        if (memberVO != null) {
                            //判断是否设置「资金密码」
                            String txPasswordAttribute = memberVO.getTxPassword();
                            if (StringTool.equals(txPasswordAttribute, Constants.Status.NO_TX_PASSWORD)) {
                                intentToSetFundPasswordActivity(activity);
                            } else {
                                showToast(getString(R.string.have_set_fund_password));
                            }
                        } else {
                            intentToSetFundPasswordActivity(activity);
                        }
                        break;
                }
            }
        }
    };

    protected HideSoftKeyBoardListener hideSoftKeyBoardListener = new HideSoftKeyBoardListener() {
        @Override
        public void hideSoftKeyBoard() {
            hideSoftKeyboard();
        }
    };

}
