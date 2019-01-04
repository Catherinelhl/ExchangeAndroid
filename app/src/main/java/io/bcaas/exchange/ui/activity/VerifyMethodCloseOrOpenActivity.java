package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 「开启或者关闭验证方式」
 */
public class VerifyMethodCloseOrOpenActivity extends BaseActivity {

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
    @BindView(R.id.etwa_email_verify_code)
    EditTextWithAction etwaEmailVerifyCode;
    @BindView(R.id.etwa_message_verify_code)
    EditTextWithAction etwaMessageVerifyCode;

    private String email, phone;

    @Override
    public int getContentView() {
        return R.layout.activity_verify_method_close_or_open;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }


    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText("关闭验证");
        if (StringTool.notEmpty(email) && tvEmailValue != null) {
            tvEmailValue.setText(email);
        }
        if (StringTool.notEmpty(phone) && tvPhoneValue != null) {
            tvPhoneValue.setText(phone);
        }
        etwaEmailVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
        etwaMessageVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));

    }

    @Override
    public void initData() {

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
        RxView.clicks(btnSure).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(Object o) {

                        //1：判断邮箱验证码输入非空
                        String password = etwaEmailVerifyCode.getContent();
                        if (StringTool.isEmpty(password)) {
                            showToast("请输入邮箱验证码");
                            return;
                        }
                        //2：判断确认短信验证码非空
                        String confirmPassword = etwaMessageVerifyCode.getContent();
                        if (StringTool.isEmpty(confirmPassword)) {
                            showToast("请输入短信验证码！");
                            return;
                        }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
