package io.bcaas.exchange.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.GoogleContract;
import io.bcaas.exchange.ui.presenter.GooglePresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 「 google验证」
 */
public class GoogleVerifyActivity extends BaseActivity implements GoogleContract.View {


    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_how_to_verify)
    TextView tvHowToVerify;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
    @BindView(R.id.tv_my_address)
    TextView tvMyAddress;
    @BindView(R.id.etwa_verify_code)
    EditTextWithAction etwaVerifyCode;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private GoogleContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_google_verify;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.please_set_google_verify);

    }

    @Override
    public void initData() {
        presenter = new GooglePresenterImp(this);
        //获取当前绑定google验证的所需信息
        presenter.getAuthenticatorUrl();

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

                        //1：判断验证码非空
                        String verifyCode = etwaVerifyCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast(getString(R.string.please_input_verify_code));
                            return;
                        }
                        presenter.securityGoogleAuthenticator(verifyCode);

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
    public void getAuthenticatorUrlSuccess(VerificationBean verificationBean) {
        if (verificationBean == null) {
            return;
        }
        String account = verificationBean.getAccount();
        String secret = verificationBean.getSecret();
        if (tvMyAddress != null && StringTool.notEmpty(secret)) {
            tvMyAddress.setText(String.format(getString(R.string.format_ss), getString(R.string.private_key_str), secret));
        }

    }

    @Override
    public void getAuthenticatorUrlFailure(String info) {

    }

    @Override
    public void securityGoogleAuthenticatorSuccess(String info) {
        //设置google验证成功，返回上一个页面并且刷新界面
        setResult(false);
    }

    @Override
    public void securityGoogleAuthenticatorFailure(String info) {
        //清空当前数据，提示其重新验证
        if (etwaVerifyCode != null) {
            etwaVerifyCode.setContent(MessageConstants.EMPTY);
        }
        showToast("设置验证失败，请重新输入验证码。");
    }

    @Override
    public void getAuthenticatorImageSuccess(Bitmap bitmap) {
        if (bitmap != null && ivQrCode != null) {
            ivQrCode.setImageBitmap(bitmap);
//                Bitmap qrCode = EncodingUtils.createQRCode(secret, context.getResources().getDimensionPixelOffset(R.dimen.d200),
//                        context.getResources().getDimensionPixelOffset(R.dimen.d200), null, Constants.Color.foregroundOfQRCode, Constants.Color.backgroundOfQRCode);
//                ivQrCode.setImageBitmap(qrCode);
        }
    }

    @Override
    public void getAuthenticatorImageFailure() {

    }
}
