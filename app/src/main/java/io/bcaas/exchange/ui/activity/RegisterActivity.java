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
    @BindView(R.id.etwa_amount)
    EditTextWithAction amount;
    @BindView(R.id.etwa_password)
    EditTextWithAction etwaPassword;
    @BindView(R.id.etwa_password_confirm)
    EditTextWithAction passwordConfirm;
    @BindView(R.id.etwa_email_code)
    EditTextWithAction emailCode;
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
        amount.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

    }

    @Override
    public void initData() {
        presenter = new RegisterPresenterImp(this);
    }

    @Override
    public void initListener() {
        RxView.clicks(ibBack).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
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
        RxView.clicks(tvLoginNow).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
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
        RxView.clicks(btnRegister).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        String memberID = Constants.User.MEMBER_ID;
                        //1：判断当前账号是否输入，是否输入正确
                        String userAmount = amount.getContent();
                        if (StringTool.isEmpty(userAmount)) {
                            userAmount = memberID;
                        }
                        if (StringTool.isEmpty(userAmount)) {
                            showToast("请输入用户名");
                            return;
                        }
                        if (!RegexTool.isRightEmail(userAmount)) {
                            showToast("请输入正确的邮箱格式");
                            return;
                        }
                        String password = etwaPassword.getContent();
                        if (StringTool.isEmpty(password)) {
                            password = Constants.User.MEMBER_PASSWORD;
                        }
                        //2：判断当前的登录密码是否输入
                        if (StringTool.isEmpty(password)) {
                            showToast("请输入密码");
                            return;
                        }
                        //3：判断当前的确认密码是否输入，且是否和登录密码匹配
                        String passwordConfirmStr = passwordConfirm.getContent();
                        if (StringTool.isEmpty(passwordConfirmStr)) {
                            passwordConfirmStr = Constants.User.MEMBER_PASSWORD;
                        }
                        if (StringTool.isEmpty(passwordConfirmStr)) {
                            showToast("请输入确认密码");
                            return;
                        }
                        if (!StringTool.equals(password, passwordConfirmStr)) {
                            showToast("两次密码输入不一致");
                            return;
                        }
                        //4：判断当前的邮箱验证码是否输入
                        String verifyCode = emailCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast("请先输入验证码");
                            return;
                        }
                        //5：开始请求
                        presenter.register(memberID, password, verifyCode);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        emailCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
            }
        }, Constants.EditTextFrom.REGISTER_EMAIL_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void registerSuccess(String info) {
        finish();
    }

    @Override
    public void registerFailure(String info) {
        showToast(info);
    }

}
