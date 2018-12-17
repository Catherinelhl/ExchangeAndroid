package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.view.editview.EditTextWithAction;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/14
 */
public class LoginActivity extends BaseActivity implements EditTextWatcherListener {
    @BindView(R.id.etwa_ammount)
    EditTextWithAction etwaAmmount;
    @BindView(R.id.etwa_password)
    EditTextWithAction etwaPassword;
    @BindView(R.id.btn_unlock_wallet)
    Button btnUnlockWallet;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_register_now)
    TextView tvRegisterNow;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        etwaAmmount.setEditTextWatcherListener(this);
        etwaPassword.setEditTextWatcherListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    @Override
    public void onComplete(String password) {
    }
}
