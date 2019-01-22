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
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.PhoneVerifyContract;
import io.bcaas.exchange.ui.contracts.WithDrawContract;
import io.bcaas.exchange.ui.presenter.PhoneVerifyPresenterImp;
import io.bcaas.exchange.ui.presenter.WithDrawPresenterImp;
import io.bcaas.exchange.view.dialog.SingleButtonDialog;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.view.textview.AppendStringLayout;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/4
 * 「提现详情，输入密码」
 */
public class WithDrawDetailActivity extends BaseActivity implements WithDrawContract.View, PhoneVerifyContract.View {
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
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.asp_fund)
    AppendStringLayout aspFund;
    @BindView(R.id.asp_google)
    AppendStringLayout aspGoogle;

    private WithDrawContract.Presenter presenter;
    private PhoneVerifyContract.Presenter phoneVerifyPresenter;
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

        MemberVO memberVO = BaseApplication.getMemberVO();
        getAccountSecuritySuccess(memberVO);
        aspFund.setOnItemSelectListener(onItemSelectListener, Constants.ActionFrom.FUND_PASSWORD);
        aspGoogle.setOnItemSelectListener(onItemSelectListener, Constants.ActionFrom.GOOGLE_VERIFY);

    }

    @Override
    public void initData() {
        presenter = new WithDrawPresenterImp(this);
        phoneVerifyPresenter = new PhoneVerifyPresenterImp(this);
        // 获取账户资讯
        presenter.getAccountSecurity();
    }

    @Override
    public void initListener() {
        etEmailVerifyCode.setFrom(Constants.EditTextFrom.EMAIL_CODE);
        etMessageVerifyCode.setFrom(Constants.EditTextFrom.PHONE_CODE);
        etMessageVerifyCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
                //得到当前用户的手机号
                MemberVO memberVO = BaseApplication.getMemberVO();
                if (memberVO == null) {
                    return;
                }
                String phone = memberVO.getPhone();
                if (StringTool.isEmpty(phone)) {
                    return;
                }
                phoneVerifyPresenter.getPhoneCode(phone, getCurrentLanguage());
            }
        });
        Disposable subscribe = RxView.clicks(ibBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        setResult(true);
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
                        //定义VerificationBean数组用来存储接下来的各种验证验证码；最多有三个，至少有一个。
                        List<VerificationBean> verificationBeanList = new ArrayList<>();
                        if (etEmailVerifyCode.getVisibility() == View.VISIBLE) {
                            //2：判断当前是否输入了邮箱验证码
                            String emailVerifyCode = etEmailVerifyCode.getContent();
                            if (StringTool.isEmpty(emailVerifyCode)) {
                                showToast(getString(R.string.please_input_email_verify_code));
                                return;
                            }
                            VerificationBean verificationBean = new VerificationBean();
                            verificationBean.setType(Constants.VerifyType.EMAIL);
                            verificationBean.setVerifyCode(emailVerifyCode);
                            verificationBeanList.add(verificationBean);

                        }
                        if (etMessageVerifyCode.getVisibility() == View.VISIBLE) {

                            //3:判断当前是否输入手机验证码
                            String phoneVerifyCode = etMessageVerifyCode.getContent();
                            if (StringTool.isEmpty(phoneVerifyCode)) {
                                showToast(getString(R.string.please_input_phone_verify_code));
                                return;
                            }
                            VerificationBean verificationBean = new VerificationBean();
                            verificationBean.setType(Constants.VerifyType.PHONE);
                            verificationBean.setVerifyCode(phoneVerifyCode);
                            verificationBeanList.add(verificationBean);

                        }
                        if (etGoogleVerifyCode.getVisibility() == View.VISIBLE) {
                            //4：判断当前是否输入google验证码
                            String googleVerifyCode = etGoogleVerifyCode.getContent();
                            if (StringTool.isEmpty(googleVerifyCode)) {
                                showToast(getString(R.string.please_input_google_verify_code));
                                return;
                            }
                            VerificationBean verificationBean = new VerificationBean();
                            verificationBean.setType(Constants.VerifyType.GOOGLE);
                            verificationBean.setVerifyCode(googleVerifyCode);
                            verificationBeanList.add(verificationBean);
                        }

                        //3：判断当前是否设置资金密码
                        MemberVO memberVO = BaseApplication.getMemberVO();
                        // 如果当前有账户信息，那么本地替用户进行密码设置的判断
                        if (memberVO != null) {
                            //判断是否设置「资金密码」
                            String txPasswordAttribute = memberVO.getTxPassword();
                            if (StringTool.equals(txPasswordAttribute, Constants.Status.NO_TX_PASSWORD)) {
                                showToast(getString(R.string.no_fund_password_please_set_first));
                                return;
                            }
                            //4：判断当前是否设置google验证码
                            int googleVerifyAttribute = memberVO.getTwoFactorAuthVerify();
                            if (googleVerifyAttribute == Constants.Status.UN_BOUND) {
                                showToast(getString(R.string.no_google_verify_please_set_first));
                                return;
                            }
                        }
                        if (presenter != null) {
                            btnSure.setEnabled(false);
                            requestJson.setVerificationBeanList(verificationBeanList);
                            //4：请求接口提现
                            presenter.withDraw(txPassword, requestJson);
                        }
                    }
                });
    }

    @Override
    public void withDrawFailure(String info) {
        btnSure.setEnabled(true);
        // 弹框提示用户 提现 失败
        showSingleDialog(info, new SingleButtonDialog.ConfirmClickListener() {
            @Override
            public void sure() {
            }
        });
    }

    @Override
    public void withDrawSuccess(String info) {
        btnSure.setEnabled(true);
        // 弹框提示用户 提现成功
        showSingleDialog(getString(R.string.congratulations_to_withdraw_success), new SingleButtonDialog.ConfirmClickListener() {
            @Override
            public void sure() {
                setResult(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.GOOGLE_VERIFY:
                    break;
                case Constants.RequestCode.FUND_PASSWORD:
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
        }
    }

    @Override
    public void getAccountSecurityFailure(String info) {
        showToast(info);

    }

    @Override
    public void getPhoneCodeSuccess(String info) {

    }

    @Override
    public void getPhoneCodeFailure(String info) {
        showToast(info);
    }


    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            if (StringTool.notEmpty(from)) {
                switch (from) {
                    case Constants.ActionFrom.GOOGLE_VERIFY:
                        //跳转到google验证
                        Intent intent = new Intent();
                        intent.setClass(WithDrawDetailActivity.this, GoogleVerifyActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.GOOGLE_VERIFY);
                        break;
                    case Constants.ActionFrom.FUND_PASSWORD:
                        //如果当前是资金密码，那么本地对用户是否设置了资金密码进行判断
                        MemberVO memberVO = BaseApplication.getMemberVO();
                        // 如果当前有账户信息，那么本地替用户进行密码设置的判断
                        if (memberVO != null) {
                            //判断是否设置「资金密码」
                            String txPasswordAttribute = memberVO.getTxPassword();
                            if (StringTool.equals(txPasswordAttribute, Constants.Status.NO_TX_PASSWORD)) {
                                intentToSetFundPasswordActivity();
                            } else {
                                showToast(getString(R.string.have_set_fund_password));
                            }
                        } else {
                            intentToSetFundPasswordActivity();
                        }
                        break;
                }
            }
        }
    };

    /**
     * 跳转到设置资金密码的页面
     */
    private void intentToSetFundPasswordActivity() {
        Intent intent = new Intent();
        intent.setClass(this, SetFundPasswordActivity.class);
        startActivityForResult(intent, Constants.RequestCode.FUND_PASSWORD);
    }


}
