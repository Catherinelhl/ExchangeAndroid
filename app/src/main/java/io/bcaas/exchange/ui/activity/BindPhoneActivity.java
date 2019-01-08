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
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.BindPhoneContract;
import io.bcaas.exchange.ui.presenter.BindPhonePresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 「绑定手机」
 */
public class BindPhoneActivity extends BaseActivity implements BindPhoneContract.View {

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

    private BindPhoneContract.Presenter presenter;

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
        etwaMessageCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
                //1：判断手机号非空
                String phone = etwaPhoneNumber.getContent();
                if (StringTool.isEmpty(phone)) {
                    showToast(getString(R.string.please_input_phone_number));
                    return;
                }
                presenter.phoneVerify(phone, getCurrentLanguage());
            }
        }, Constants.EditTextFrom.PHONE);

    }

    @Override
    public void initData() {
        presenter = new BindPhonePresenterImp(this);

    }

    @Override
    public void initListener() {
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

                        //1：判断手机号非空
                        String phone = etwaPhoneNumber.getContent();
                        if (StringTool.isEmpty(phone)) {
                            showToast(getString(R.string.please_input_phone_number));
                            return;
                        }
                        //2：判断验证码非空
                        String messageCode = etwaMessageCode.getContent();
                        if (StringTool.isEmpty(messageCode)) {
                            showToast(getString(R.string.please_input_phone_verify_code));
                            return;
                        }
                        presenter.phoneVerify(phone, getCurrentLanguage());

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
    public void bindPhoneSuccess(String info) {
        //验证成功，返回到上一个页面，然后重置状态
        setResult(false);
    }

    @Override
    public void bindPhoneFailure(String info) {

        //验证失败，提示重新验证？
        showToast(info);
    }
}
