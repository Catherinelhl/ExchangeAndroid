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
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.AccountSecurityContract;
import io.bcaas.exchange.ui.contracts.PayWayManagerContract;
import io.bcaas.exchange.ui.presenter.AccountSecurityPresenterImp;
import io.bcaas.exchange.ui.presenter.PaymentManagerPresenterImp;
import io.bcaas.exchange.view.dialog.DoubleButtonDialog;
import io.bcaas.exchange.vo.MemberPayInfoVO;
import io.bcaas.exchange.vo.MemberVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.List;
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

public class RechargeActivity extends BaseActivity implements AccountSecurityContract.View {

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

    private AccountSecurityContract.Presenter presenter;

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
        presenter = new AccountSecurityPresenterImp(this);

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
                        /*step 1:判断当前是否设置资金密码*/
                        MemberVO memberVO = BaseApplication.getMemberVO();
                        // 如果当前有账户信息，那么本地替用户进行密码设置的判断
                        if (memberVO != null) {
                            //判断是否设置「资金密码」
                            String txPasswordAttribute = memberVO.getTxPassword();
                            if (StringTool.equals(txPasswordAttribute, Constants.Status.NO_TX_PASSWORD)) {
                                showToast(getString(R.string.no_fund_password_please_set_first));
                                return;
                            }
                            //4：判断当前是否设置google验证码
                            int googleVerifyAttribute = memberVO.getTwoFactorAuthVerify();
                            if (googleVerifyAttribute == Constants.Status.UN_BOUND) {
                                showToast(getString(R.string.no_google_verify_please_set_first));
                                return;
                            }
                        }
                        /*step 2:判断当前是否完成实名认证*/
                        /*step 3:判断当前是否完成支付方式绑定*/
                        presenter.getAccountSecurity();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.ADD_PAYMENT_CODE:
                    boolean isBack = data.getBooleanExtra(Constants.KeyMaps.From, false);
                    if (!isBack) {
                        //更新当前信息
                    }
                    break;
            }
        }
    }

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {
        //判断当前是否已经绑定了支付方式
        int isPayWayBind = memberVO.getIsPayWayBind();
        if (isPayWayBind == 0) {
            // 未绑定
            //如果当前查询当前的支付方式没有数据结构返回，那么进行提示拦截
            showDoubleButtonDialog(getString(R.string.cancel),
                    getString(R.string.go_to_bind),
                    getString(R.string.please_finish_payment_bind),
                    new DoubleButtonDialog.ConfirmClickListener() {
                        @Override
                        public void sure() {
                            Intent intent = new Intent();
                            intent.setClass(RechargeActivity.this, AddPaymentActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.ADD_PAYMENT_CODE);
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        } else {
            //已经绑定
            intentToActivity(RechargeDetailActivity.class);
        }

    }

    @Override
    public void getAccountSecurityFailure(String info) {
        showToast(info);
    }
}
