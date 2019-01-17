package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
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
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.ForgetPasswordContract;
import io.bcaas.exchange.ui.presenter.ForgetPasswordPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/17
 * 忘记密码
 */
public class ForgetPasswordActivity extends BaseActivity implements ForgetPasswordContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_amount)
    EditTextWithAction etAccount;
    @BindView(R.id.etwa_password)
    EditTextWithAction etPassword;
    @BindView(R.id.etwa_password_confirm)
    EditTextWithAction etPasswordConfirm;
    @BindView(R.id.etwa_email_code)
    EditTextWithAction emailCode;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private ForgetPasswordContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.reset_password_title);
        emailCode.setFrom(Constants.EditTextFrom.EMAIL_CODE);

    }

    @Override
    public void initData() {
        presenter = new ForgetPasswordPresenterImp(this);


    }

    @Override
    public void initListener() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BuildConfig.DEBUG) {
                    if (multipleClickToDo(2)) {
                        if (etAccount != null) {
                            etAccount.setContent(Constants.User.MEMBER_ID);
                        }
                    }
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
        RxView.clicks(btnSure).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //1:判断当前邮箱的输入
                        String memberId = etAccount.getContent();
                        if (StringTool.isEmpty(memberId)) {
                            showToast(getString(R.string.please_input_account_info));
                            return;
                        }
                        //2：判断当前密码的输入
                        String password = etPassword.getContent();
                        if (StringTool.isEmpty(password)) {
                            showToast(getString(R.string.please_input_password));
                            return;
                        }
                        //3：判断当前密码的确认输入
                        String passwordConfirm = etPasswordConfirm.getContent();
                        if (StringTool.isEmpty(passwordConfirm)) {
                            showToast(getString(R.string.confirm_password));
                            return;
                        }
                        //4：判断前后密码是否一致
                        if (!StringTool.equals(password, passwordConfirm)) {
                            showToast(getString(R.string.password_does_not_match));
                            return;
                        }
                        //5：判断当前验证码的输入
                        String verifyCode = emailCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast(getString(R.string.please_input_verify_code));
                            return;
                        }
                        //6：发送请求
                        presenter.forgetPassword(password, verifyCode);
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
    public void forgetPasswordFailure(String info) {
        showToast(info);
    }

    @Override
    public void forgetPasswordSuccess(String info) {
        setResult(false);
    }
}
