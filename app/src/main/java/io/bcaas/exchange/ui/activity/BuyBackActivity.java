package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.AmountEditTextFilter;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.decimal.DecimalTool;
import io.bcaas.exchange.ui.contracts.PayWayManagerContract;
import io.bcaas.exchange.ui.presenter.PaymentManagerPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.vo.MemberPayInfoVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/22 10:56
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.activity
+--------------+---------------------------------
+ description  +    充值 具体操作详细步骤页面
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public class BuyBackActivity extends BaseActivity implements PayWayManagerContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_email_key)
    TextView tvEmailKey;
    @BindView(R.id.tv_email_value)
    TextView tvEmailValue;
    @BindView(R.id.tv_recharge_amount_key)
    TextView tvRechargeAmountKey;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.tv_buy_back_amount)
    TextView tvBuyBackAmount;
    @BindView(R.id.tv_receive_username)
    TextView tvReceiveUsername;
    @BindView(R.id.tv_receive_bank)
    TextView tvReceiveBank;
    @BindView(R.id.tv_receive_account)
    TextView tvReceiveAccount;
    @BindView(R.id.etwa_fund_password)
    EditTextWithAction etwaFundPassword;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private MemberPayInfoVO memberPayInfoVO;


    private PayWayManagerContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_buy_back;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.buy_back));
        etAmount.setFilters(new InputFilter[]{new AmountEditTextFilter().setDigits(Constants.DigitalPrecision.LIMIT_TWO)});

    }

    @Override
    public void initData() {
        presenter = new PaymentManagerPresenterImp(this);
        presenter.getPayWay(Constants.Payment.GET_PAY_WAY);
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
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    String content = s.toString();
                    if (StringTool.notEmpty(content)) {
                        if (tvBuyBackAmount != null) {
                            tvBuyBackAmount.setText(DecimalTool.transferDisplay(2, content, Constants.Pattern.TWO_DISPLAY));
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        RxView.clicks(btnSure).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //step 1:判断当前输入不能为空
                        String buyBackAmount = etAmount.getText().toString();
                        if (StringTool.isEmpty(buyBackAmount)) {
                            showToast(getString(R.string.please_input_buy_back_number));
                            return;
                        }

                        String fundPassword = etwaFundPassword.getContent();
                        if (StringTool.isEmpty(fundPassword)) {
                            showToast(getString(R.string.please_input_fund_password));
                            return;
                        }
                        //Step 2:根据用户输入的内容请求数据
                        presenter.convertCoin(Constants.Payment.CONVERT_COIN, Constants.CURRENCY_TYPE_SCS, buyBackAmount, fundPassword);

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
    public <T> void responseSuccess(T message, String type) {
        switch (type) {
            case Constants.Payment.GET_PAY_WAY:
                List<MemberPayInfoVO> memberPayInfoVOList = ((List<MemberPayInfoVO>) message);
                //取出当前第一条数据，然后传入下一个界面
                if (ListTool.noEmpty(memberPayInfoVOList)) {
                    this.memberPayInfoVO = memberPayInfoVOList.get(0);
                    if (memberPayInfoVO != null) {
                        tvEmailValue.setText(memberPayInfoVO.getMemberId());
                        tvBuyBackAmount.setText(R.string.zero_yuan);
                        tvReceiveUsername.setText(memberPayInfoVO.getBankPersonalName());
                        tvReceiveAccount.setText(memberPayInfoVO.getBankName());
                        tvReceiveBank.setText(memberPayInfoVO.getBankAccount());
                    }
                }
                break;
            case Constants.Payment.CONVERT_COIN:
                showToast("申请回购成功！");
                finish();
                break;
        }
    }

    @Override
    public void responseFailed(String message, String type) {
        showToast(message);
    }
}
