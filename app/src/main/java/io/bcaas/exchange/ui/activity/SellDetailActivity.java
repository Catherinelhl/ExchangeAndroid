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
import io.bcaas.exchange.bean.SellDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.SellContract;
import io.bcaas.exchange.ui.presenter.SellPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 购买详情
 */
public class SellDetailActivity extends BaseActivity implements SellContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_purple_title)
    TextView tvPurpleTitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.etwa_fund_password)
    EditTextWithAction etFundPassword;
    @BindView(R.id.etwa_google_verify_code)
    EditTextWithAction etGoogleVerifyCode;
    @BindView(R.id.tv_start_immediate)
    TextView tvStartImmediate;
    @BindView(R.id.btn_sell)
    Button btnSell;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.tv_salable_balance)
    TextView tvSalableBalance;
    @BindView(R.id.tv_transaction_amount)
    TextView tvTransactionAmount;
    private SellContract.Presenter presenter;

    private SellDataBean sellDataBean;

    @Override
    public int getContentView() {
        return R.layout.activity_sell_detail;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        // 得到传递过来的售出数据类
        sellDataBean = (SellDataBean) bundle.getSerializable(Constants.KeyMaps.SELL_DATA_BEAN);
    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.sell_detail);

        if (sellDataBean != null) {
            tvPrice.setText(sellDataBean.getUnitPrice() + "\t " + sellDataBean.getExchangeCurrencyName());
            tvNumber.setText(sellDataBean.getSellAmount() + "\t " + sellDataBean.getEnName());
            tvFee.setText(sellDataBean.getGasFeeCharge() + "\t " + sellDataBean.getEnName());
            tvSalableBalance.setText(context.getResources().getString(R.string.salable_balance) + "\t" + sellDataBean.getBalanceAvailable());
            tvTransactionAmount.setText(sellDataBean.getTxAmountExceptFeeString());
            tvPurpleTitle.setText(context.getResources().getString(R.string.sell_out) + "\t \t" + sellDataBean.getEnName());
        }


    }

    @Override
    public void initData() {
        presenter = new SellPresenterImp(this);
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
        RxView.clicks(btnSell).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断当前资金密码是否输入
                        String txPassword = etFundPassword.getContent();
                        if (StringTool.isEmpty(txPassword)) {
                            showToast("请输入资金密码！");
                            return;
                        }
                        //2：判断当前google验证码是否输入
                        String verifyCode = etGoogleVerifyCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast("请先输入google验证码！");
                            return;
                        }
                        //3：接口请求数据
                        if (sellDataBean != null) {
                            presenter.sell(sellDataBean.getCurrencyUid(), sellDataBean.getExchangeCurrencyUid(),
                                    sellDataBean.getSellAmount(), sellDataBean.getUnitPrice(), txPassword, verifyCode);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        RxView.clicks(tvStartImmediate).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object o) {
                        //跳转到google验证
                        Intent intent = new Intent();
                        intent.setClass(SellDetailActivity.this, GoogleVerifyActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.GOOGLE_VERIFY);

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
    public void sellFailure(String info) {
        showToast(info);
    }

    @Override
    public void sellSuccess(String info) {
        setResult(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.GOOGLE_VERIFY:
                    break;
            }
        }
    }
}
