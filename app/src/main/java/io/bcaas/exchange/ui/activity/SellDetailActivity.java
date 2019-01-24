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
import io.bcaas.exchange.bean.SellDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.SellContract;
import io.bcaas.exchange.ui.presenter.SellPresenterImp;
import io.bcaas.exchange.view.dialog.SingleButtonDialog;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.view.textview.AppendStringLayout;
import io.bcaas.exchange.vo.MemberVO;
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
    @BindView(R.id.btn_sell)
    Button btnSell;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.tv_salable_balance)
    TextView tvSalableBalance;
    @BindView(R.id.tv_transaction_amount)
    TextView tvTransactionAmount;
    @BindView(R.id.asp_fund)
    AppendStringLayout aspFund;
    @BindView(R.id.asp_google)
    AppendStringLayout aspGoogle;
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
            tvPrice.setText(sellDataBean.getUnitPrice() + "  " + sellDataBean.getExchangeCurrencyName());
            tvNumber.setText(sellDataBean.getSellAmount() + "  " + sellDataBean.getEnName());
            tvFee.setText(sellDataBean.getGasFeeCharge() + "  " + sellDataBean.getEnName());
            tvSalableBalance.setText(context.getResources().getString(R.string.salable_balance) + "  " + sellDataBean.getBalanceAvailable() + "  " + sellDataBean.getEnName());
            tvTransactionAmount.setText(sellDataBean.getTxAmountExceptFeeString());
            tvPurpleTitle.setText(context.getResources().getString(R.string.sell) + "  " + sellDataBean.getEnName());
        }
        aspFund.setOnItemSelectListener(onItemSelectListener, Constants.ActionFrom.FUND_PASSWORD);
        aspGoogle.setOnItemSelectListener(onItemSelectListener, Constants.ActionFrom.GOOGLE_VERIFY);

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
        RxView.clicks(btnSell).throttleFirst(Constants.Time.sleep1000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断当前资金密码是否输入
                        String txPassword = etFundPassword.getContent();
                        if (StringTool.isEmpty(txPassword)) {
                            showToast(getString(R.string.please_input_fund_password));
                            return;
                        }
                        //2：判断当前google验证码是否输入
                        String verifyCode = etGoogleVerifyCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast(getString(R.string.please_set_google_verify));
                            return;
                        }
                        //3：判断当前是否设置资金密码
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

                        //5：接口请求数据
                        if (sellDataBean != null) {
                            btnSell.setEnabled(false);
                            presenter.sell(sellDataBean.getCurrencyUid(), sellDataBean.getExchangeCurrencyUid(),
                                    sellDataBean.getSellAmount(), sellDataBean.getUnitPrice(), txPassword, verifyCode);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void sellFailure(String info) {
        btnSell.setEnabled(true);
        showSingleDialog(info, new SingleButtonDialog.ConfirmClickListener() {
            @Override
            public void sure() {
            }
        });
    }

    @Override
    public void sellSuccess(String info) {
        btnSell.setEnabled(true);
        // 弹框提示用户挂单成功
        showSingleDialog(getString(R.string.congratulations_to_sell_out), new SingleButtonDialog.ConfirmClickListener() {
            @Override
            public void sure() {
                setResult(false);
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        setResult(true);
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            if (StringTool.notEmpty(from)) {
                switch (from) {
                    case Constants.ActionFrom.GOOGLE_VERIFY:
                        //跳转到google验证
                        Intent intent = new Intent();
                        intent.setClass(SellDetailActivity.this, GoogleVerifyActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.GOOGLE_VERIFY);
                        break;
                    case Constants.ActionFrom.FUND_PASSWORD:
                        //如果当前是资金密码，那么本地对用户是否设置了资金密码进行判断
                        MemberVO memberVO = BaseApplication.getMemberVO();
                        // 如果当前有账户信息，那么本地替用户进行密码设置的判断
                        if (memberVO != null) {
                            //判断是否设置「资金密码」
                            String txPasswordAttribute = memberVO.getTxPassword();
                            if (StringTool.equals(txPasswordAttribute, Constants.Status.NO_TX_PASSWORD)) {
                                intentToSetFundPasswordActivity();
                            } else {
                                showToast(getString(R.string.have_set_fund_password));
                            }
                        } else {
                            intentToSetFundPasswordActivity();
                        }
                        break;
                }
            }
        }
    };

    /**
     * 跳转到设置资金密码的页面
     */
    private void intentToSetFundPasswordActivity() {
        Intent intent = new Intent();
        intent.setClass(this, SetFundPasswordActivity.class);
        startActivityForResult(intent, Constants.RequestCode.FUND_PASSWORD);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 如果当前是从设置google以及验证资金密码返回，应该更新当前用户的安全验证问题
                case Constants.RequestCode.GOOGLE_VERIFY:
                case Constants.RequestCode.FUND_PASSWORD:
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

    }
}
