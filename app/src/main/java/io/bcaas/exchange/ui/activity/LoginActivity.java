package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.app.VersionTool;
import io.bcaas.exchange.ui.contracts.LoginContract;
import io.bcaas.exchange.ui.presenter.LoginPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/14
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {
    @BindView(R.id.etwa_amount)
    EditTextWithAction acount;
    @BindView(R.id.etwa_password)
    EditTextWithAction password;
    @BindView(R.id.etwa_image_code)
    EditTextWithAction etImageCode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.tv_register_now)
    TextView tvRegisterNow;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    private LoginContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        //设置账号只能输入邮箱类型
        acount.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        setAppVersion();

    }

    /**
     * APP当前的版本信息
     */
    private void setAppVersion() {
        tvVersion.setText(String.format(getString(R.string.format_ssd), getString(R.string.version), VersionTool.getVersionName(this), VersionTool.getVersionCode(this)));
    }

    @Override
    public void initData() {
        presenter = new LoginPresenterImp(this);
    }

    @Override
    public void initListener() {
        tvVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        acount.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {
                String passwordStr = password.getContent();
                if (StringTool.notEmpty(passwordStr)) {
                    if (StringTool.equals(passwordStr, content)) {
                        hideSoftKeyboard();
                    }
                }
            }

            @Override
            public void onAction(String from) {

            }
        }, Constants.EditTextFrom.LOGIN_AMOUNT);
        password.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {
                String amountStr = acount.getContent();
                if (StringTool.notEmpty(amountStr)) {
                    if (StringTool.equals(amountStr, content)) {
                        hideSoftKeyboard();
                    }
                }
            }

            @Override
            public void onAction(String from) {

            }
        }, Constants.EditTextFrom.LOGIN_PASSWORD);
        RxView.clicks(tvRegisterNow).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, RegisterActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.REGISTER_CODE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(tvForgetPassword).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, ForgetPasswordActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.RESET_PASSWORD_CODE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btnLogin).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        String memberID = Constants.User.MEMBER_ID;
                        String password = Constants.User.MEMBER_PASSWORD;
                        String verifyCode = etImageCode.getContent();
                        //1：判断账号非空
                        if (StringTool.isEmpty(memberID)) {
                            showToast(getString(R.string.input_account));
                            return;
                        }
                        //2：判断密码非空
                        if (StringTool.isEmpty(password)) {
                            showToast(getString(R.string.input_password));
                            return;
                        }
                        //3：判断验证码非空
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast(getString(R.string.input_graphic_verify_code));
                            return;
                        }
                        presenter.login(memberID, password, verifyCode);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.RequestCode.REGISTER_CODE: //注册页面返回
                LogTool.d(TAG, "register return");
                //清空当前界面信息
                if (etImageCode!=null){
                    etImageCode.setContent(MessageConstants.EMPTY);
                }
                // 重新刷新登录界面的验证码
                etImageCode.requestImageVerifyCode();
                break;
            case Constants.RequestCode.RESET_PASSWORD_CODE://重置密码页面返回
                LogTool.d(TAG, "reset password");
                // 重新刷新登录界面的验证码
                etImageCode.requestImageVerifyCode();
                break;


        }
    }

    @Override
    public void loginSuccess(String info) {
        hideSoftKeyboard();
        intentToActivity(MainActivity.class, true);

    }

    @Override
    public void loginFailure(String info) {
        hideSoftKeyboard();
        showToast(info);
    }

}
