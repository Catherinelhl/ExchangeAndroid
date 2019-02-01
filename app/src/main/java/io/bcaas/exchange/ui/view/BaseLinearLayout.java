package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.event.LogoutEvent;
import io.bcaas.exchange.listener.HideSoftKeyBoardListener;
import io.bcaas.exchange.manager.SoftKeyBroadManager;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.otto.OttoTool;
import io.bcaas.exchange.ui.contracts.BaseContract;
import io.bcaas.exchange.view.dialog.LoadingDialog;
import io.bcaas.exchange.vo.ResponseJson;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/16
 * <p>
 * 继承LinearLayout的View的基类
 */
public abstract class BaseLinearLayout extends LinearLayout implements BaseContract.View {

    private String TAG = BaseLinearLayout.class.getSimpleName();
    protected Context context;
    private View view;
    private LoadingDialog loadingDialog;
    /*软键盘管理*/
    protected SoftKeyBroadManager softKeyBroadManager;

    //隐藏当前软键盘的监听
    protected HideSoftKeyBoardListener hideSoftKeyBoardListener;

    public BaseLinearLayout(Context context) {
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(setContentView(), this, true);
        ButterKnife.bind(view);
        initView();
        initListener();
        //注册otto事件
        OttoTool.getInstance().register(this);

    }

    public void setHideSoftKeyBoardListener(HideSoftKeyBoardListener hideSoftKeyBoardListener) {
        this.hideSoftKeyBoardListener = hideSoftKeyBoardListener;
    }

    protected abstract int setContentView();

    protected abstract void initView();

    protected abstract void initListener();

    @Override
    public void showLoading() {
        if (context == null) {
            return;
        }
        hideLoading();
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setProgressBarColor(getResources().getColor(R.color.yellow_FFA73B));
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (context == null) {
            return;
        }
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog.cancel();
            loadingDialog = null;

        }
    }

    @Override
    public void noNetWork() {
        Toast.makeText(context, getResources().getString(R.string.network_not_reachable), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean httpExceptionDisposed(ResponseJson responseJson) {
        if (responseJson == null) {
            return false;
        }
        int code = responseJson.getCode();
        //判断是否是Token过期，弹出提示重新登录，然后跳转界面
        if (code == MessageConstants.CODE_2019
                || code == MessageConstants.CODE_2016
                || code == MessageConstants.CODE_2018) {
            //    {"success":false,"code":2019,"message":"AccessToken expire."}
            OttoTool.getInstance().post(new LogoutEvent());
            return true;
        } else if (code == MessageConstants.CODE_2005) {
            LogoutEvent logoutEvent = new LogoutEvent();
            logoutEvent.setInfo(context.getString(R.string.please_register_email_first));
            OttoTool.getInstance().post(logoutEvent);
            return true;
        }
        return false;
    }

    @Override
    public void noData() {
        LogTool.e(TAG, context.getResources().getString(R.string.no_data));
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
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (hideSoftKeyBoardListener != null) {
                    hideSoftKeyBoardListener.hideSoftKeyBoard();
                }
                return false;
            }
        });


    }

}
