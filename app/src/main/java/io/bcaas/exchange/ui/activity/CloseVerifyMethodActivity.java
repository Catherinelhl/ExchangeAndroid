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
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.CloseVerifyCodeContract;
import io.bcaas.exchange.ui.presenter.CloseVerifyPresenterImp;
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
 * 「关闭某个验证方式」
 * <p>
 * 1：进入界面获取当前的用户资讯，根据用户打开的验证方式来判断界面具体显示情况
 */
public class CloseVerifyMethodActivity extends BaseActivity implements CloseVerifyCodeContract.View {

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
    @BindView(R.id.tv_google_verify_key)
    TextView tvGoogleVerifyKey;
    @BindView(R.id.etwa_email_verify_code)
    EditTextWithAction etwaEmailVerifyCode;
    @BindView(R.id.etwa_message_verify_code)
    EditTextWithAction etwaMessageVerifyCode;
    @BindView(R.id.etwa_google_verify_code)
    EditTextWithAction etwaGoogleVerifyCode;

    private CloseVerifyCodeContract.Presenter presenter;
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
        etwaEmailVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
        etwaMessageVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
        etwaGoogleVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));


    }

    @Override
    public void initData() {
        getAccountSecuritySuccess(BaseApplication.getMemberVO());
        presenter = new CloseVerifyPresenterImp(this);
        presenter.getAccountSecurity();
    }

    @Override
    public void initListener() {
        etwaEmailVerifyCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {

            }
        }, Constants.EditTextFrom.EMAIL);
        etwaMessageVerifyCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
            }

        }, Constants.EditTextFrom.PHONE);
        etwaGoogleVerifyCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {

            }
        }, Constants.EditTextFrom.GOOGLE);
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
                        List<VerificationBean> verificationBeans = new ArrayList<>();
                        //1：判断邮箱验证码输入非空
                        String emailVerifyCode = etwaEmailVerifyCode.getContent();
                        if (StringTool.isEmpty(emailVerifyCode)) {
                            showToast(getString(R.string.please_input_email_verify_code));
                            return;
                        } else {
                            VerificationBean verificationBean = new VerificationBean();
                            verificationBean.setCloseType(getCloseType(from, Constants.VerifyType.EMAIL));
                            verificationBean.setType(Constants.VerifyType.EMAIL);
                            verificationBean.setMail(BaseApplication.getMemberId());
                            verificationBean.setVerifyCode(emailVerifyCode);

                        }
                        //2：判断确认短信验证码非空
                        String messageVerifyCode = etwaMessageVerifyCode.getContent();
                        if (StringTool.isEmpty(messageVerifyCode)) {
                            showToast(getString(R.string.please_input_message_code));
                            return;
                        } else {
                            VerificationBean verificationBean = new VerificationBean();
                            verificationBean.setCloseType(getCloseType(from, Constants.VerifyType.PHONE));
                            verificationBean.setType(Constants.VerifyType.PHONE);
                            verificationBean.setMail(BaseApplication.getMemberId());
                            verificationBean.setVerifyCode(emailVerifyCode);
                        }
                        //3：判断是否有google验证
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

    }

    @Override
    public void closeVerifyCodeFailure(String info) {

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
        if (StringTool.notEmpty(phone) && tvPhoneValue != null) {
            tvPhoneValue.setText(phone);
        }
        int emailVerify = memberVO.getEmailVerify();
        int phoneVerify = memberVO.getPhoneVerify();
        int twoFactorAuthVerify = memberVO.getTwoFactorAuthVerify();
        //判断是否开启「邮箱验证」
        llEmail.setVisibility(emailVerify == Constants.Status.CLOSE ? View.GONE : View.VISIBLE);
        etwaEmailVerifyCode.setVisibility(emailVerify == Constants.Status.CLOSE ? View.GONE : View.VISIBLE);

        //判断是否开启「手机验证」
        llPhone.setVisibility(phoneVerify == Constants.Status.CLOSE ? View.GONE : View.VISIBLE);
        etwaMessageVerifyCode.setVisibility(phoneVerify == Constants.Status.CLOSE ? View.GONE : View.VISIBLE);

        //判断是否开启「google验证」
        tvGoogleVerifyKey.setVisibility(twoFactorAuthVerify == Constants.Status.CLOSE ? View.GONE : View.VISIBLE);
        etwaGoogleVerifyCode.setVisibility(twoFactorAuthVerify == Constants.Status.CLOSE ? View.GONE : View.VISIBLE);

    }

    @Override
    public void getAccountSecurityFailure(String info) {
        showToast(info);
    }
}
