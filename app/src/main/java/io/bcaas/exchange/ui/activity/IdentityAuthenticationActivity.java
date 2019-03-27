package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.view.dialog.DoubleButtonDialog;
import io.bcaas.exchange.view.viewGroup.SafetyCenterItemView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/21 17:23
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.activity
+--------------+---------------------------------
+ description  +  身份认证
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public class IdentityAuthenticationActivity extends BaseActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.sciv_authentication)
    SafetyCenterItemView scivAuthentication;

    @Override
    public int getContentView() {
        return R.layout.activity_identity_authentication;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.identity_verification));
        scivAuthentication.setTabStatusByText(false, getString(R.string.Immediate_authentication));

    }

    @Override
    public void initData() {

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
        scivAuthentication.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public <T> void onItemSelect(T type, String from) {
                showDoubleButtonDialog(getString(R.string.Immediate_authentication), new DoubleButtonDialog.ConfirmClickListener() {
                    @Override
                    public void sure() {
                        showToast(getString(R.string.authentication_success));
                        finish();
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        });

    }


}
