package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.BuildConfig;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.regex.RegexTool;
import io.bcaas.exchange.ui.contracts.RegisterContract;
import io.bcaas.exchange.ui.presenter.RegisterPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/17
 * 注册页面
 */
public class RegisterActivity extends BaseActivity implements RegisterContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_account)
    EditTextWithAction etAccount;
    @BindView(R.id.etwa_password)
    EditTextWithAction etPassword;
    @BindView(R.id.etwa_password_confirm)
    EditTextWithAction etPasswordConfirm;
    @BindView(R.id.etwa_email_code)
    EditTextWithAction etEmailCode;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_login_now)
    TextView tvLoginNow;

    private RegisterContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.register_title);
        //设置账号只能输入邮箱类型
        etAccount.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etEmailCode.setFrom(Constants.EditTextFrom.EMAIL_CODE);

    }

    @Override
    public void initData() {
        presenter = new RegisterPresenterImp(this);
    }

    @Override
    public void initListener() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multipleClickToDo(2) && BuildConfig.DEBUG) {
                    //TODO 注册的快捷方式
                    etAccount.setContent(Constants.User.MEMBER_ID);
                    etPassword.setContent(Constants.User.MEMBER_PASSWORD);
                    etPasswordConfirm.setContent(Constants.User.MEMBER_PASSWORD);

                }
            }
        });
        RxView.clicks(ibBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        setResult(true);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(tvLoginNow).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        setResult(false);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btnRegister).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断当前账号是否输入
                        String userAccount = etAccount.getContent();
                        if (StringTool.isEmpty(userAccount)) {
                            showToast(getString(R.string.please_input_account));
                            return;
                        }
                        //2：是否输入正确的邮箱格式
                        if (!RegexTool.isRightEmail(userAccount)) {
                            showToast(getString(R.string.please_input_right_email));
                            return;
                        }
                        //3：判断当前的登录密码是否输入
                        String password = etPassword.getContent();
                        if (StringTool.isEmpty(password)) {
                            showToast(getString(R.string.please_input_password));
                            return;
                        }
                        //4：判断当前的确认密码是否输入
                        String passwordConfirmStr = etPasswordConfirm.getContent();
                        if (StringTool.isEmpty(passwordConfirmStr)) {
                            showToast(getString(R.string.please_input_confirm_password));
                            return;
                        }
                        //5：且是否和登录密码匹配
                        if (!StringTool.equals(password, passwordConfirmStr)) {
                            showToast(getString(R.string.password_does_not_match));
                            return;
                        }
                        //6：判断当前的邮箱验证码是否输入
                        String verifyCode = etEmailCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast(getString(R.string.please_input_verify_code_first));
                            return;
                        }
                        //7：开始请求
                        presenter.register(userAccount, password, verifyCode);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void registerSuccess(String info) {
        setResult(false);
    }

    @Override
    public void registerFailure(String info) {
        showToast(info);
    }

}
