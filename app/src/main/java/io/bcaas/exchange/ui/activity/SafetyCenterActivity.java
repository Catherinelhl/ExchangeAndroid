package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.SafetyCenterContract;
import io.bcaas.exchange.ui.presenter.SafetyCenterPresenterImp;
import io.bcaas.exchange.view.viewGroup.SafetyCenterItemView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 「安全中心」
 */
public class SafetyCenterActivity extends BaseActivity implements SafetyCenterContract.View, OnItemSelectListener {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.sciv_login_password)
    SafetyCenterItemView scivLoginPassword;
    @BindView(R.id.sciv_fund_password)
    SafetyCenterItemView scivFundPassword;
    @BindView(R.id.sciv_email_verify)
    SafetyCenterItemView scivEmailVerify;
    @BindView(R.id.sciv_phone_verify)
    SafetyCenterItemView scivPhoneVerify;
    @BindView(R.id.sciv_google_verify)
    SafetyCenterItemView scivGoogleVerify;

    private SafetyCenterContract.Presenter presenter;


    @Override
    public int getContentView() {
        return R.layout.activity_sefety_center;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.safety_center);
        //登录密码
        scivLoginPassword.setTabStatusByText(false, "修改");
        scivLoginPassword.setOnItemSelectListener(this);
        //资金密码
        scivFundPassword.setTabStatusByText(false, "设置");
        scivFundPassword.setTabInfo("用于提币，交易的验证");
        scivFundPassword.setOnItemSelectListener(this);
        //邮箱验证
        scivEmailVerify.setTabStatusByText(false, "关闭");
        scivEmailVerify.setTabInfo(Constants.User.MEMBER_ID);
        scivEmailVerify.setOnItemSelectListener(this);

        //手机验证
        scivPhoneVerify.setTabStatusByText(false, "绑定");
        scivPhoneVerify.setTabInfo("用于提币,更改安全设置的验证");
        scivPhoneVerify.setOnItemSelectListener(this);

        //google验证
        scivGoogleVerify.setTabStatusByText(false, "设置");
        scivGoogleVerify.setTabInfo("用于提币,交易及更改安全设置的验证");
        scivGoogleVerify.setOnItemSelectListener(this);

    }


    @Override
    public void initData() {
        presenter = new SafetyCenterPresenterImp(this);

    }

    @Override
    public void initListener() {
        Disposable subscribe = RxView.clicks(ibBack).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        setResult(true);
                    }
                });
        RxView.clicks(btnLogout).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        presenter.logout(Constants.User.MEMBER_ID);
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
    public void logoutSuccess(String info) {
        intentToActivity(LoginActivity.class, true);
    }

    @Override
    public void logoutFailure(String info) {

    }

    @Override
    public <T> void onItemSelect(T type, String from) {

        Intent intent = new Intent();
        if (StringTool.equals(from, getString(R.string.login_password))) {
            // 登录密码
            intent.setClass(SafetyCenterActivity.this, ModifyLoginPasswordActivity.class);
            startActivityForResult(intent, Constants.RequestCode.MODIFY_LOGIN_PASSWORD);

        } else if (StringTool.equals(from, getString(R.string.fund_password))) {
            //资金密码
            intent.setClass(SafetyCenterActivity.this, SetFundPasswordActivity.class);
            startActivityForResult(intent, Constants.RequestCode.FUND_PASSWORD);

        } else if (StringTool.equals(from, getString(R.string.email_verify))) {
            //邮箱验证
            intent.setClass(SafetyCenterActivity.this, VerifyMethodCloseOrOpenActivity.class);
            startActivityForResult(intent, Constants.RequestCode.EMAIL_VERIFY);

        } else if (StringTool.equals(from, getString(R.string.phone_verify))) {
            //手机验证
            intent.setClass(SafetyCenterActivity.this, BindPhoneActivity.class);
            startActivityForResult(intent, Constants.RequestCode.PHONE_VERIFY);

        } else if (StringTool.equals(from, getString(R.string.google_verify))) {
            //Google验证
            intent.setClass(SafetyCenterActivity.this, GoogleVerifyActivity.class);
            startActivityForResult(intent, Constants.RequestCode.GOOGLE_VERIFY);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.MODIFY_LOGIN_PASSWORD:
                    break;
                case Constants.RequestCode.FUND_PASSWORD:
                    break;
                case Constants.RequestCode.EMAIL_VERIFY:
                    break;
                case Constants.RequestCode.PHONE_VERIFY:
                    break;
                case Constants.RequestCode.GOOGLE_VERIFY:
                    break;
            }
        }
    }
}
