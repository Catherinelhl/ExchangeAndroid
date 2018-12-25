package io.bcaas.exchange.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
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
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_amount)
    EditTextWithAction amount;
    @BindView(R.id.etwa_password)
    EditTextWithAction password;
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
    }

    @Override
    public void initData() {
        presenter = new RegisterPresenterImp(this);
    }

    @Override
    public void initListener() {
        RxView.clicks(ibBack).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
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
        RxView.clicks(tvLoginNow).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
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
        RxView.clicks(btnRegister).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        String memberID = Constants.User.MEMBER_ID;
                        String password = Constants.User.MEMBER_PASSWORD;
                        String verifyCode = emailCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast("请先输入验证码");
                            return;
                        }
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
            public void onSendAction(String from) {
                if (StringTool.equals(from, Constants.EditTextFrom.REGISTER_EMAIL_CODE)) {
                    //开始请求验证码数据  //getCurrentLanguage()
                    presenter.emailVerify(Constants.User.MEMBER_ID, "0", Constants.User.MEMBER_ID);
                }
            }
        }, Constants.EditTextFrom.REGISTER_EMAIL_CODE);
    }

    private void setResult(boolean isBack) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.KeyMaps.From, isBack);
        intent.putExtras(bundle);
        this.setResult(RESULT_OK, intent);
        this.finish();
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

    @Override
    public void getEmailVerifySuccess(String info) {
        LogTool.d(TAG, info);
    }

    @Override
    public void getEmailVerifyFailure(String info) {
        showToast(info);
    }
}
