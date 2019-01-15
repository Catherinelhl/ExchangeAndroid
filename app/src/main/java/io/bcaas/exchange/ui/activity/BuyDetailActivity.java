package io.bcaas.exchange.ui.activity;

import android.content.Intent;
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
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.BuyContract;
import io.bcaas.exchange.ui.presenter.BuyPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.MemberVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 购买详情
 */
public class BuyDetailActivity extends BaseActivity implements BuyContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_purple_title)
    TextView tvPurpleTitle;
    @BindView(R.id.tv_person_name)
    TextView tvPersonName;
    @BindView(R.id.tv_pay_method)
    TextView tvPayMethod;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_total_account)
    TextView tvTotalAccount;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.etwa_fund_password)
    EditTextWithAction etFundPassword;
    @BindView(R.id.etwa_random_verify_code)
    EditTextWithAction etRandomVerifyCode;
    @BindView(R.id.tv_start_immediate)
    TextView tvStartImmediate;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.tv_salable_balance)
    TextView tvSalableBalance;
    private MemberOrderVO memberOrderVO;
    private BuyContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_buy_detail;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        memberOrderVO = (MemberOrderVO) bundle.getSerializable(Constants.KeyMaps.BUY_DETAIL);
    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.buy_detail);
        tvPayMethod.setText(R.string.pay_method_str);
        if (memberOrderVO != null) {
            //得到出售人的名字
            MemberVO memberVO = memberOrderVO.getMemberVO();
            if (memberVO != null) {
                tvPersonName.setText(memberVO.getMemberId());
            }
            // 得到需要支付的信息
            CurrencyListVO paymentCurrencyList = memberOrderVO.getPaymentCurrencyUid();
            if (paymentCurrencyList != null) {
                String enName = paymentCurrencyList.getEnName();
                tvPayMethod.setText(getString(R.string.pay_method_str) + "  " + enName);
                tvFee.setText(paymentCurrencyList.getBuyCharge() + "  " + enName);
                tvPrice.setText(memberOrderVO.getUnitPrice() + "  " + enName);
                tvTotalAccount.setText(memberOrderVO.getPrice() + "  " + enName);


            }
            // 得到当前币种信息
            CurrencyListVO currencyListVO = memberOrderVO.getCurrencyListVO();
            if (currencyListVO != null) {
                String enName = currencyListVO.getEnName();
                tvPurpleTitle.setText(getString(R.string.buy) + " " + enName);
                tvNumber.setText(memberOrderVO.getAmount() + "  " + enName);
            }
        }


    }

    @Override
    public void initData() {
        presenter = new BuyPresenterImp(this);
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
        RxView.clicks(btnBuy).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
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
                        String verifyCode = etRandomVerifyCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast(getString(R.string.please_input_google_verify_code));
                            return;
                        }
                        if (memberOrderVO != null) {
                            //3：接口请求数据
                            presenter.buy(txPassword, memberOrderVO.getMemberOrderUid(), verifyCode);
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
                        intent.setClass(BuyDetailActivity.this, GoogleVerifyActivity.class);
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
    public void buyFailure(String info) {
        showToast(info);
    }

    @Override
    public void buySelfError() {
        showToast(getString(R.string.not_to_buy_self));
    }

    @Override
    public void buySuccess(String info) {
        setResult(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(true);
    }
}
