package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.event.LogoutEvent;
import io.bcaas.exchange.tools.otto.OttoTool;
import io.bcaas.exchange.ui.contracts.BaseContract;
import io.bcaas.exchange.view.dialog.LoadingDialog;
import io.bcaas.exchange.vo.ResponseJson;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/16
 */
public abstract class BaseLinearLayout extends LinearLayout implements BaseContract.View {

    protected Context context;
    private View view;
    private LoadingDialog loadingDialog;

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
        if (code == MessageConstants.CODE_2019) {
            //    {"success":false,"code":2019,"message":"AccessToken expire."}
            OttoTool.getInstance().post(new LogoutEvent());
            return true;
        }
        return false;
    }
}
