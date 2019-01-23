package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.BuildConfig;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.regex.RegexTool;
import io.bcaas.exchange.ui.contracts.RegisterContract;
import io.bcaas.exchange.ui.presenter.RegisterPresenterImp;
import io.bcaas.exchange.view.dialog.SingleButtonDialog;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.view.textview.AppendStringLayout;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/17
 * 注册页面
 */
public class RegisterActivity extends BaseActivity
        implements RegisterContract.View {
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
    @BindView(R.id.asp_login)
    AppendStringLayout aspLogin;
    @BindView(R.id.asp_user_agreement)
    AppendStringLayout aspUserAgreement;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;

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
        etEmailCode.setFrom(Constants.EditTextFrom.REGISTER_VERIFY_EMAIL);
        aspLogin.setOnItemSelectListener(onItemSelectListener, Constants.ActionFrom.LOGIN);
    }

    @Override
    public void initData() {
        presenter = new RegisterPresenterImp(this);
    }

    @Override
    public void initListener() {
        cbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
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
        RxView.clicks(btnRegister).throttleFirst(Constants.Time.sleep1000, TimeUnit.MILLISECONDS)
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

                        //4：判断密码是否输入8位,是否符合密码输入规则
                        if (!RegexTool.isValidatePassword(password)) {
                            showToast(getResources().getString(R.string.password_rule_of_length));
                            return;
                        }
                        //5：判断当前的确认密码是否输入
                        String passwordConfirmStr = etPasswordConfirm.getContent();
                        if (StringTool.isEmpty(passwordConfirmStr)) {
                            showToast(getString(R.string.please_input_confirm_password));
                            return;
                        }
                        //6：且是否和登录密码匹配
                        if (!StringTool.equals(password, passwordConfirmStr)) {
                            showToast(getString(R.string.password_does_not_match));
                            return;
                        }
                        //7：判断当前的邮箱验证码是否输入
                        String verifyCode = etEmailCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast(getString(R.string.please_input_verify_code_first));
                            return;
                        }
                        //8：判断用户是否阅读用户协议
                        if (!cbAgreement.isChecked()) {
                            showToast(getString(R.string.please_read_and_agree_user_agreement));
                            return;
                        }
                        //9：开始请求
                        presenter.register(userAccount, password, verifyCode);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogTool.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        etEmailCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
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
                //检测当前的邮箱是否已经注册
                etEmailCode.verifyAccount(userAccount);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void registerSuccess(String info) {
        // 弹框提示用户注册成功
        showSingleDialog(getString(R.string.congratulations_to_register_success), new SingleButtonDialog.ConfirmClickListener() {
            @Override
            public void sure() {
                setResult(false);
            }
        });
    }

    @Override
    public void registerFailure(String info) {
        showToast(info);
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            if (StringTool.notEmpty(from)) {
                switch (from) {
                    case Constants.ActionFrom.LOGIN:
                        setResult(false);
                        break;
                }
            }
        }
    };

}
