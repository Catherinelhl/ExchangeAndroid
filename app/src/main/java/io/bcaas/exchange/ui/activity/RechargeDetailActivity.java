package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.vo.MemberPayInfoVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

public class RechargeDetailActivity extends BaseActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_email_key)
    TextView tvEmailKey;
    @BindView(R.id.tv_email_value)
    TextView tvEmailValue;
    @BindView(R.id.tv_recharge_amount_key)
    TextView tvRechargeAmountKey;
    @BindView(R.id.rb_one)
    RadioButton rbOne;
    @BindView(R.id.rb_two)
    RadioButton rbTwo;
    @BindView(R.id.rb_three)
    RadioButton rbThree;
    @BindView(R.id.rb_four)
    RadioButton rbFour;
    @BindView(R.id.tv_pay_amount)
    TextView tvPayAmount;
    @BindView(R.id.tv_receive_username)
    TextView tvReceiveUsername;
    @BindView(R.id.tv_receive_bank)
    TextView tvReceiveBank;
    @BindView(R.id.tv_receive_account)
    TextView tvReceiveAccount;
    @BindView(R.id.tv_payment_note_number_key)
    TextView tvPaymentNoteNumberKey;
    @BindView(R.id.tv_payment_note)
    TextView tvPaymentNote;
    @BindView(R.id.etwa_image_code)
    EditTextWithAction etwaImageCode;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private MemberPayInfoVO memberPayInfoVO;

    @Override
    public int getContentView() {
        return R.layout.activity_recharge_detail;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        memberPayInfoVO = (MemberPayInfoVO) bundle.getSerializable(Constants.From.MEMBER_PAY_INFO);
    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.recharge));

    }

    @Override
    public void initData() {
        if (memberPayInfoVO != null) {
            tvEmailValue.setText(memberPayInfoVO.getMemberId());
            tvPayAmount.setText("500.00元");
            tvReceiveUsername.setText(memberPayInfoVO.getBankPersonalName());
            tvReceiveAccount.setText(memberPayInfoVO.getBankName());
            tvReceiveBank.setText(memberPayInfoVO.getBankAccount());
            tvPaymentNote.setText("987 657");
        }

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
        RxView.clicks(btnSure).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
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


    }
}
