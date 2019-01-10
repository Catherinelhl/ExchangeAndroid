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
    @BindView(R.id.etwa_random_verify_code)
    EditTextWithAction etRandomVerifyCode;
    @BindView(R.id.tv_start_immediate)
    TextView tvStartImmediate;
    @BindView(R.id.btn_sell)
    Button btnSell;
    private SellContract.Presenter presenter;
    //当前卖出的币种，需要支付的币种
    private String currencyUid, currencyPaymentUid, amount, unitPrice, fee = "0.0001";

    @Override
    public int getContentView() {
        return R.layout.activity_sell_detail;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        currencyUid = bundle.getString(Constants.KeyMaps.SELL_CURRENCY_UID);
        currencyPaymentUid = bundle.getString(Constants.KeyMaps.SELL_CURRENCY_PAYMENT_UID);
        amount = bundle.getString(Constants.KeyMaps.SELL_AMOUNT);
        unitPrice = bundle.getString(Constants.KeyMaps.SELL_UNIT_PRICE);
    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.buy_detail);
        if (StringTool.notEmpty(currencyUid)) {
            tvPrice.setText(unitPrice + "\t " + currencyPaymentUid);
        }
        if (StringTool.notEmpty(amount)) {
            tvNumber.setText(amount + "\t " + currencyUid);
        }
        if (StringTool.notEmpty(fee)) {
            tvFee.setText(fee + "\t " + currencyUid);
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
                        String verifyCode = etRandomVerifyCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast("请先输入google验证码！");
                            return;
                        }
                        //3：接口请求数据
                        presenter.sell(currencyUid, currencyPaymentUid, amount, unitPrice, txPassword, verifyCode);

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
}
