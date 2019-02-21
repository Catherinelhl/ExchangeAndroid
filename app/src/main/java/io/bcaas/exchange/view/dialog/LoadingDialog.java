package io.bcaas.exchange.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.bcaas.exchange.BuildConfig;
import io.bcaas.exchange.R;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/11
 * <p>
 *  自定義Dialog：加载进度的显示框
 */
public class LoadingDialog extends Dialog implements DialogInterface {
    private String TAG = LoadingDialog.class.getSimpleName();
    Dialog dlg;
    Context context;
    //    @BindView(R.id.loadView)
//    LinearLayout loadView;
    @BindView(R.id.tip_text_view)
    TextView tipTextView;
    @BindView(R.id.pb_loading)
    ProgressBar progressBar;


    private View mDialogView;

    private boolean isCancelable = false;

    private String msg;
    Animation hyperspaceJumpAnimation;


    public LoadingDialog(Context context) {
        super(context, R.style.dialog_loading);
        init(context);

    }

    public LoadingDialog(Context context, String msg) {
        super(context, R.style.dialog_loading);
        init(context, msg);

    }

    public void setProgressBarColor(int color) {
        ColorStateList colorStateList = ColorStateList.valueOf(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setIndeterminateTintList(colorStateList);
        }
    }

    private void init(Context context) {
        dlg = this;
        this.context = context;
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        isCancelable = true;
        Runnable runnable = this::initView;
        runnable.run();
    }

    private void init(Context context, String msg) {
        dlg = this;
        this.context = context;
        this.msg = msg;
        hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        Runnable runnable = this::initView;
        runnable.run();
    }

    public void initView() {
        mDialogView = View.inflate(context, R.layout.layout_loading_dailog, null);
        setContentView(mDialogView);
        ButterKnife.bind(this, mDialogView);
//        loadView.setOnClickListener(null);
//        loadView.setClickable(false);
        isCancelable = false;
        mDialogView.setOnClickListener(null);
        if (!StringTool.isEmpty(msg)) {
            tipTextView.setVisibility(View.VISIBLE);
            tipTextView.setText(msg);// 设置加载信息
        } else {
            tipTextView.setVisibility(View.GONE);
        }

    }

    public void message(String msg) {
        if (!StringTool.isEmpty(msg)) {
            tipTextView.setVisibility(View.VISIBLE);
            tipTextView.setText(msg);// 设置加载信息
        } else {
            tipTextView.setVisibility(View.GONE);
        }
    }

    public LoadingDialog isCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelable = cancelable;
        if (BuildConfig.DEBUG)
            this.setCanceledOnTouchOutside(isCancelable);
        else
            this.setCanceledOnTouchOutside(cancelable);
        return this;
    }

    public LoadingDialog isCancelable(boolean cancelable) {
        this.isCancelable = cancelable;
        if (BuildConfig.DEBUG)
            this.setCancelable(isCancelable);
        else
            this.setCancelable(cancelable);
        return this;
    }


    @Override
    public void show() {
        if (isShowing()) {
            return;
        }
        super.show();
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
                LogTool.d(TAG, e.getMessage());
            }
        }
    }


}
