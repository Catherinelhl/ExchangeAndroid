package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.WithDrawContract;
import io.bcaas.exchange.ui.presenter.WithDrawPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/4
 * 「提现详情，输入密码」
 */
public class WithDrawDetailActivity extends BaseActivity implements WithDrawContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_fund_password)
    EditTextWithAction etFundPassword;
    @BindView(R.id.tv_email_key)
    TextView tvEmailKey;
    @BindView(R.id.tv_email_value)
    TextView tvEmailValue;
    @BindView(R.id.etwa_email_verify_code)
    EditTextWithAction etEmailVerifyCode;
    @BindView(R.id.tv_phone_key)
    TextView tvPhoneKey;
    @BindView(R.id.tv_phone_value)
    TextView tvPhoneValue;
    @BindView(R.id.etwa_message_verify_code)
    EditTextWithAction etMessageVerifyCode;
    @BindView(R.id.tv_google_verify_key)
    TextView tvGoogleVerifyKey;
    @BindView(R.id.etwa_google_verify_code)
    EditTextWithAction etGoogleVerifyCode;
    @BindView(R.id.tv_start_immediate)
    TextView tvStartImmediate;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_google_verify_tips)
    LinearLayout llGoogleVerifyTips;

    private WithDrawContract.Presenter presenter;
    private RequestJson requestJson;

    @Override
    public int getContentView() {
        return R.layout.activity_withdraw_detail;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        requestJson = (RequestJson) bundle.getSerializable(Constants.KeyMaps.WITHDRAW_REQUEST_JSON);

    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.with_draw);
        ibBack.setVisibility(View.VISIBLE);

        etMessageVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
        etEmailVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
        etGoogleVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));

        MemberVO memberVO = BaseApplication.getMemberVO();
        getAccountSecuritySuccess(memberVO);

    }

    @Override
    public void initData() {
        presenter = new WithDrawPresenterImp(this);
        // 获取账户资讯
        presenter.getAccountSecurity();
    }

    @Override
    public void initListener() {

        Disposable subscribe = RxView.clicks(ibBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        setResult(true);
                    }
                });
        Disposable subscribeStartImmediate = RxView.clicks(tvStartImmediate).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //跳转到google验证
                        Intent intent = new Intent();
                        intent.setClass(WithDrawDetailActivity.this, GoogleVerifyActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.GOOGLE_VERIFY);
                    }
                });
        Disposable subscribeSure = RxView.clicks(btnSure).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //1:判断当前是否输入了资金密码
                        String txPassword = etFundPassword.getContent();
                        if (StringTool.isEmpty(txPassword)) {
                            showToast(getString(R.string.please_input_fund_password));
                            return;
                        }
                        if (etEmailVerifyCode.getVisibility() == View.VISIBLE) {
                            //2：判断当前是否输入了邮箱验证码
                            String emailVerifyCode = etEmailVerifyCode.getContent();
                            if (StringTool.isEmpty(emailVerifyCode)) {
                                showToast(getString(R.string.please_input_email_verify_code));
                                return;
                            }
                        }
                        if (etMessageVerifyCode.getVisibility() == View.VISIBLE) {

                            //3:判断当前是否输入手机验证码
                            String phoneVerifyCode = etMessageVerifyCode.getContent();
                            if (StringTool.isEmpty(phoneVerifyCode)) {
                                showToast(getString(R.string.please_input_phone_verify_code));
                                return;
                            }
                        }
                        if (etGoogleVerifyCode.getVisibility() == View.VISIBLE) {

                            //4：判断当前是否输入google验证码
                            String googleVerifyCode = etGoogleVerifyCode.getContent();
                            if (StringTool.isEmpty(googleVerifyCode)) {
                                showToast(getString(R.string.please_input_google_verify_code));
                                return;
                            }
                        }
                        //3：请求接口提现
                        presenter.withDraw(txPassword, requestJson);
                    }
                });
    }

    @Override
    public void withDrawFailure(String info) {
        showToast(info);
    }

    @Override
    public void withDrawSuccess(String info) {
        setResult(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.GOOGLE_VERIFY:
                    break;
            }
        }
    }

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {
        //根据当前取到的用户信息，判断显示密码验证情况
        if (memberVO != null) {
            //1：判断当前是否开启邮箱验证
            int emailVerify = memberVO.getEmailVerify();
            String email = memberVO.getMemberId();
            if (emailVerify == Constants.Status.OPEN) {
                tvEmailValue.setText(email);
                llEmail.setVisibility(View.VISIBLE);
                etEmailVerifyCode.setVisibility(View.VISIBLE);
            } else {
                etEmailVerifyCode.setVisibility(View.GONE);
                llEmail.setVisibility(View.GONE);
            }
            //2：判断当前是否开启电话验证
            int phoneVerify = memberVO.getPhoneVerify();
            String phone = memberVO.getPhone();
            if (phoneVerify == Constants.Status.OPEN) {
                tvPhoneValue.setText(phone);
                llPhone.setVisibility(View.VISIBLE);
                etMessageVerifyCode.setVisibility(View.VISIBLE);
            } else {
                llPhone.setVisibility(View.GONE);
                etMessageVerifyCode.setVisibility(View.GONE);
            }

            //3：判断当前是否开启google验证
            int twoFactorAuthVerify = memberVO.getTwoFactorAuthVerify();
        }
    }

    @Override
    public void getAccountSecurityFailure(String info) {
        showToast(info);

    }

}
