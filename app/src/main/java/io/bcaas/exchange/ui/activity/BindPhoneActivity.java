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
import io.bcaas.exchange.bean.CountryCodeBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.BindPhoneContract;
import io.bcaas.exchange.ui.presenter.BindPhonePresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;
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
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_message_code)
    EditTextWithAction etMessageCode;
    @BindView(R.id.etwa_phone_number)
    EditTextWithAction etPhoneNumber;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private BindPhoneContract.Presenter presenter;
    private List<CountryCodeBean.CountryCode> countryCodes;

    @Override
    public int getContentView() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        btnSure.setEnabled(false);
        countryCodes = new ArrayList<>();
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.bind_phone);
        etMessageCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
        etMessageCode.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
                //1：判断手机号非空
                String phone = etPhoneNumber.getContent();
                if (StringTool.isEmpty(phone)) {
                    showToast(getString(R.string.please_input_phone_number));
                    return;
                }
                String sendPhoneInfo = tvCode.getText() + phone;
                presenter.getPhoneCode(sendPhoneInfo, getCurrentLanguage());
            }
        }, Constants.EditTextFrom.PHONE_CODE);

    }

    @Override
    public void initData() {
        presenter = new BindPhonePresenterImp(this);
        presenter.getCountryCode(getCurrentLanguage());

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
                        String phone = etPhoneNumber.getContent();
                        if (StringTool.isEmpty(phone)) {
                            showToast(getString(R.string.please_input_phone_number));
                            return;
                        }
                        //2：判断验证码非空
                        String messageCode = etMessageCode.getContent();
                        if (StringTool.isEmpty(messageCode)) {
                            showToast(getString(R.string.please_input_phone_verify_code));
                            return;
                        }
                        String sendPhoneInfo = tvCode.getText() + phone;
                        presenter.securityPhone(sendPhoneInfo, messageCode);

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(tvSelect).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(Object o) {

                        if (ListTool.isEmpty(countryCodes)) {
                            showToast("获取数据失败！");
                            return;
                        }
                        Intent intent = new Intent();
                        intent.putExtra(Constants.From.COUNTRY_CODE, GsonTool.string(countryCodes));
                        intent.setClass(BindPhoneActivity.this, CountryCodeActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.COUNTRY_CODE);
//                showListPopWindow(new OnItemSelectListener() {
//                    @Override
//                    public <T> void onItemSelect(T type, String from) {
//                        LogTool.d(TAG, "onItemSelect:" + type);
//                    }
//                }, countryCodes);

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
    public void securityPhoneSuccess(String info) {
        //验证成功，返回到上一个页面，然后重置状态
        showToast(getString(R.string.bind_success));
        setResult(false);
    }

    @Override
    public void securityPhoneFailure(String info) {

        //验证失败，提示重新验证？
        showToast(info);
    }

    @Override
    public void getPhoneCodeSuccess(String info) {
        //设置「确定」按钮可以点击
        btnSure.setEnabled(true);
    }

    @Override
    public void getPhoneCodeFailure(String info) {
        showToast(info);
        // 设置「确定」按钮可以点击
        btnSure.setEnabled(true);
    }

    @Override
    public void getCountryCodeSuccess(List<CountryCodeBean.CountryCode> countryCodes) {
        this.countryCodes.clear();
        this.countryCodes.addAll(countryCodes);

    }

    @Override
    public void getCountryCodeFailure() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.COUNTRY_CODE://选择城市区号页面返回
                    LogTool.d(TAG, "country code");
                    if (data != null) {
                        CountryCodeBean.CountryCode countryCode = (CountryCodeBean.CountryCode) data.getSerializableExtra(Constants.KeyMaps.SELECT_COUNTRY_CODE);
                        if (countryCode != null) {
                            if (tvCode != null) {
                                tvCode.setText("+" + countryCode.getPhoneCode());
                            }
                            if (tvSelect != null) {
                                tvSelect.setText(countryCode.getCountryName());
                            }
                            LogTool.d(TAG, "当前选中的城市区号：" + countryCode);
                        }
                    }
                    break;
            }
        }
    }
}
