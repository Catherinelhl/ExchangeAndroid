package io.bcaas.exchange.ui.activity;

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
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 「绑定手机」
 */
public class BindPhoneActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_message_code)
    EditTextWithAction etwaMessageCode;
    @BindView(R.id.etwa_phone_number)
    EditTextWithAction etwaPhoneNumber;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @Override
    public int getContentView() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.bind_phone);
        etwaMessageCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));

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

                        //1：判断手机号非空
                        String password = etwaPhoneNumber.getContent();
                        if (StringTool.isEmpty(password)) {
                            showToast("请输入手机号！");
                            return;
                        }
                        //2：判断验证码非空
                        String messageCode = etwaMessageCode.getContent();
                        if (StringTool.isEmpty(messageCode)) {
                            showToast("请输入验证码！");
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
}
