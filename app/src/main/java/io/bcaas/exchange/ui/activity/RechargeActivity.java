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
import io.bcaas.exchange.ui.presenter.AccountSecurityPresenterImp;
import io.bcaas.exchange.view.dialog.DoubleButtonDialog;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
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
        List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
        if (ListTool.noEmpty(memberKeyVOList)) {
            /**
             * {
             * "memberKeyUid": 48,
             * "address": "0",
             * "balanceBlocked": "1.0000000000",
             * "balanceAvailable": "1999.0000000000",
             * "currencyListVO": {
             * "currencyUid": "3",
             * "enName": "CNYC",
             * "cnName": "七彩貝"
             * }
             * }
             */
            for (MemberKeyVO memberKeyVO : memberKeyVOList) {
                if (memberKeyVO == null) {
                    tvRechargeName.setText(context.getResources().getString(R.string.default_cnyc_value));
                    return;
                }
                int memberKeyUid = memberKeyVO.getMemberKeyUid();
                //判断如果当前是七彩贝的话
                if (memberKeyUid == Constants.MemberKeyUID.CNYC) {
                    CurrencyListVO currencyListVO = memberKeyVO.getCurrencyListVO();
                    if (currencyListVO == null) {
                        tvRechargeName.setText(context.getResources().getString(R.string.default_cnyc_value));
                        return;
                    }
                    StringBuffer sbRechargeName = new StringBuffer(currencyListVO.getCnName());
                    sbRechargeName.append(":")
                            .append(memberKeyVO.getBalanceAvailable())
                            .append(" ")
                            .append(currencyListVO.getEnName());
                    tvRechargeName.setText(sbRechargeName);
                }
            }
        } else {
            tvRechargeName.setText(context.getResources().getString(R.string.default_cnyc_value));
        }
    }

    @Override
    public void initData() {
        presenter = new AccountSecurityPresenterImp(this);
        presenter.getAccountSecurity();
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
                        if (isCanIntent()) {
                            //已经绑定
                            intentToActivity(BuyBackActivity.class);
                        }

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
                        if (isCanIntent()) {
                            //已经绑定
                            intentToActivity(RechargeDetailActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.ADD_PAYMENT_CODE:
                    boolean isBack = data.getBooleanExtra(Constants.KeyMaps.From, false);
                    if (!isBack) {
                        //更新当前信息
                        presenter.getAccountSecurity();
                    }
                    break;
                case Constants.RequestCode.IDENTITY_AUTHENTICATION:
                    //更新当前的信息
                    presenter.getAccountSecurity();
                    break;
            }
        }
    }

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {

    }

    @Override
    public void getAccountSecurityFailure(String info) {
        showToast(info);
    }

    private boolean isCanIntent() {
        /*step 1:判断当前是否设置资金密码*/
        MemberVO memberVO = BaseApplication.getMemberVO();
        // 如果当前有账户信息，那么本地替用户进行密码设置的判断
        if (memberVO != null) {
            //判断是否设置「资金密码」
            String txPasswordAttribute = memberVO.getTxPassword();
            if (StringTool.equals(txPasswordAttribute, Constants.Status.NO_TX_PASSWORD)) {
                showToast(getString(R.string.no_fund_password_please_set_first));
                return false;
            }
            //4：判断当前是否设置google验证码
            int googleVerifyAttribute = memberVO.getTwoFactorAuthVerify();
            if (googleVerifyAttribute == Constants.Status.UN_BOUND) {
                showToast(getString(R.string.no_google_verify_please_set_first));
                return false;
            }
        }
        /*step 2:判断当前是否完成实名认证*/
        int identityVerify = memberVO.getIsIdentityVerify();
        if (identityVerify == 0) {
            showDoubleButtonDialog(getString(R.string.please_identity), new DoubleButtonDialog.ConfirmClickListener() {
                @Override
                public void sure() {
                    // 跳转到实名认证的界面
                    Intent intent = new Intent();
                    intent.setClass(RechargeActivity.this, IdentityAuthenticationActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.IDENTITY_AUTHENTICATION);
                }

                @Override
                public void cancel() {

                }
            });
            return false;
        }
        /*step 3:判断当前是否完成支付方式绑定*/
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
            return false;
        }
        return true;
    }
}
