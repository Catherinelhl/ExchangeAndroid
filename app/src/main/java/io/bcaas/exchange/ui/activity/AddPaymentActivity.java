package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.view.viewGroup.AddPayWayItemView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/25 09:50
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.activity
+--------------+---------------------------------
+ description  +  添加支付方式
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public class AddPaymentActivity extends BaseActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.apwv_payment)
    AddPayWayItemView apwvPayment;
    @BindView(R.id.apwv_bank_person_name)
    AddPayWayItemView apwvBankPersonName;
    @BindView(R.id.apwv_bank_name)
    AddPayWayItemView apwvBankName;
    @BindView(R.id.apwv_bank_branch_name)
    AddPayWayItemView apwvBankBranchName;
    @BindView(R.id.apwv_bank_account)
    AddPayWayItemView apwvBankAccount;

    @Override
    public int getContentView() {
        return R.layout.activity_add_payment;
    }

    @Override
    public void getArgs(Bundle bundle) {


    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.add_payment_way);
        apwvPayment.setMiddleInfo(getString(R.string.please_choose_a_payment));
        apwvPayment.showRightDrawable(true);
    }

    @Override
    public void initData() {

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
        apwvPayment.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public <T> void onItemSelect(T type, String from) {
                showToast("银行卡");
            }
        });
    }


}
