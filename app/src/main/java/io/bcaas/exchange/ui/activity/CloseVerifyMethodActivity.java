package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.CloseVerifyCodeContract;
import io.bcaas.exchange.ui.contracts.PhoneVerifyContract;
import io.bcaas.exchange.ui.presenter.CloseVerifyPresenterImp;
import io.bcaas.exchange.ui.presenter.PhoneVerifyPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.vo.MemberVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * Activity：「关闭某个验证方式」
 * <p>
 * 1：进入界面获取当前的用户资讯，根据用户打开的验证方式来判断界面具体显示情况
 */
public class CloseVerifyMethodActivity extends BaseActivity
        implements CloseVerifyCodeContract.View, PhoneVerifyContract.View {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;

    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.tv_email_key)
    TextView tvEmailKey;
    @BindView(R.id.tv_email_value)
    TextView tvEmailValue;
    @BindView(R.id.tv_phone_key)
    TextView tvPhoneKey;
    @BindView(R.id.tv_phone_value)
    TextView tvPhoneValue;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_close_verify_method)
    LinearLayout llCloseVerifyMethod;
    @BindView(R.id.tv_google_verify_key)
    TextView tvGoogleVerifyKey;
    @BindView(R.id.etwa_email_verify_code)
    EditTextWithAction etEmailVerifyCode;
    @BindView(R.id.etwa_message_verify_code)
    EditTextWithAction etMessageVerifyCode;
    @BindView(R.id.etwa_google_verify_code)
    EditTextWithAction etGoogleVerifyCode;

    private CloseVerifyCodeContract.Presenter presenter;
    private PhoneVerifyContract.Presenter phoneVerifyPresenter;
    private String from;//标记当前是从那种验证方式跳入

    @Override
    public int getContentView() {
        return R.layout.activity_close_verify_method;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        from = bundle.getString(Constants.KeyMaps.From);
    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.close_verify);
        etEmailVerifyCode.setFrom(Constants.EditTextFrom.EMAIL_CODE);
        etMessageVerifyCode.setFrom(Constants.EditTextFrom.PHONE_CODE);
    }

    @Override
    public void initData() {
        getAccountSecuritySuccess(BaseApplication.getMemberVO());
        presenter = new CloseVerifyPresenterImp(this);
        phoneVerifyPresenter = new PhoneVerifyPresenterImp(this);
        presenter.getAccountSecurity();
    }

    @Override
    public void initListener() {
        hideSoftKeyBoardByTouchView(llCloseVerifyMethod);
        etEmailVerifyCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {

            }
        });
        etMessageVerifyCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {

                //得到当前用户的手机号码
                MemberVO memberVO = BaseApplication.getMemberVO();
                if (memberVO == null) {
                    showToast(getString(R.string.get_data_failure));
                    return;
                }
                String phone = memberVO.getPhone();
                if (StringTool.isEmpty(phone)) {
                    showToast(getString(R.string.please_input_phone_number));
                    return;
                }
                phoneVerifyPresenter.getPhoneCode(phone, getCurrentLanguage());
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
                        hideSoftKeyboard();
                        List<VerificationBean> verificationBeans = new ArrayList<>();
                        //1：判断邮箱验证码输入非空
                        if (etEmailVerifyCode.getVisibility() == View.VISIBLE) {
                            String emailVerifyCode = etEmailVerifyCode.getContent();
                            if (StringTool.isEmpty(emailVerifyCode)) {
                                showToast(getString(R.string.please_input_email_verify_code));
                                return;
                            } else {
                                VerificationBean verificationBean = new VerificationBean();
                                verificationBean.setCloseType(getCloseType(from, Constants.VerifyType.EMAIL));
                                verificationBean.setType(Constants.VerifyType.EMAIL);
                                verificationBean.setMail(BaseApplication.getMemberID());
                                verificationBean.setVerifyCode(emailVerifyCode);
                                verificationBeans.add(verificationBean);

                            }
                        }
                        //2：判断确认短信验证码非空
                        if (etMessageVerifyCode.getVisibility() == View.VISIBLE) {
                            String messageVerifyCode = etMessageVerifyCode.getContent();
                            if (StringTool.isEmpty(messageVerifyCode)) {
                                showToast(getString(R.string.please_input_message_code));
                                return;
                            } else {
                                MemberVO memberVO = BaseApplication.getMemberVO();
                                if (memberVO == null) {
                                    showToast(getString(R.string.get_data_failure));
                                    return;
                                }
                                VerificationBean verificationBean = new VerificationBean();
                                verificationBean.setCloseType(getCloseType(from, Constants.VerifyType.PHONE));
                                verificationBean.setType(Constants.VerifyType.PHONE);
                                verificationBean.setPhone(memberVO.getPhone());
                                verificationBean.setVerifyCode(messageVerifyCode);
                                verificationBeans.add(verificationBean);
                            }
                        }
                        //3：判断是否有google验证
                        if (etGoogleVerifyCode.getVisibility() == View.VISIBLE) {
                            String googleVerify = etGoogleVerifyCode.getContent();
                            if (StringTool.isEmpty(googleVerify)) {
                                showToast(getString(R.string.please_set_google_verify));
                                return;
                            } else {
                                VerificationBean verificationBean = new VerificationBean();
                                verificationBean.setCloseType(getCloseType(from, Constants.VerifyType.GOOGLE));
                                verificationBean.setType(Constants.VerifyType.GOOGLE);
                                verificationBean.setVerifyCode(googleVerify);
                                verificationBeans.add(verificationBean);
                            }
                        }
                        //4:请求网络关闭当前需要关闭的验证方式
                        presenter.closeVerifyCode(verificationBeans);

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
        setResult(false);
    }

    @Override
    public void closeVerifyCodeSuccess(String info) {
        showToast(info);
        setResult(false);
    }

    @Override
    public void closeVerifyCodeFailure(String info) {
        showToast(info);
    }

    /**
     * 根据传入的from & type来得到当前是否需要关闭
     *
     * @param from
     * @param type
     * @return
     */
    private String getCloseType(String from, String type) {
        if (StringTool.equals(from, type)) {
            return "1";
        }
        return "0";
    }

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {
        if (memberVO == null) {
            return;
        }
        String email = memberVO.getMemberId();
        String phone = memberVO.getPhone();
        if (StringTool.notEmpty(email) && tvEmailValue != null) {
            tvEmailValue.setText(email);
        }
        if (StringTool.notEmpty(phone)
                && tvPhoneValue != null) {
            tvPhoneValue.setText(phone);
        }
        boolean openEmailVerify = memberVO.getEmailVerify() == Constants.Status.OPEN;
        boolean openPhoneVerify = memberVO.getPhoneVerify() == Constants.Status.OPEN && StringTool.notEmpty(phone);
        boolean openTwoFactorAuthVerify = memberVO.getTwoFactorAuthVerify() == Constants.Status.OPEN;
        //判断是否开启「邮箱验证」
        llEmail.setVisibility(openEmailVerify ? View.VISIBLE : View.GONE);
        etEmailVerifyCode.setVisibility(openEmailVerify ? View.VISIBLE : View.GONE);

        //判断是否开启「手机验证」
        llPhone.setVisibility(openPhoneVerify ? View.VISIBLE : View.GONE);
        etMessageVerifyCode.setVisibility(openPhoneVerify ? View.VISIBLE : View.GONE);

        //判断是否开启「google验证」
        tvGoogleVerifyKey.setVisibility(openTwoFactorAuthVerify ? View.VISIBLE : View.GONE);
        etGoogleVerifyCode.setVisibility(openTwoFactorAuthVerify ? View.VISIBLE : View.GONE);

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
}
