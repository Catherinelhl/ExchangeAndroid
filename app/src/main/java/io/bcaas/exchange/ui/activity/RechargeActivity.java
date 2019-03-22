package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.BuildConfig;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/21 17:22
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.activity
+--------------+---------------------------------
+ description  +   充值
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public class RechargeActivity extends BaseActivity {

    private String TAG = RechargeActivity.class.getSimpleName();
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_recharge_name)
    TextView tvRechargeName;
    @BindView(R.id.btn_buy_back)
    Button btnBuyBack;
    @BindView(R.id.btn_recharge)
    Button btnRecharge;
    @BindView(R.id.tv_recharge_intro)
    TextView tvRechargeIntro;

    @Override
    public int getContentView() {
        return R.layout.activity_recharge;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.recharge));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        RxView.clicks(btnBuyBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        intentToActivity(BuyBackActivity.class);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btnRecharge).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        intentToActivity(RechargeDetailActivity.class);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
