package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.listener.RadioButtonCheckListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.PayWayManagerContract;
import io.bcaas.exchange.ui.presenter.PaymentManagerPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.view.viewGroup.CustomRechargeAmount;
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

public class RechargeDetailActivity extends BaseActivity
        implements PayWayManagerContract.View {
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
    @BindView(R.id.custom_recharge_amount)
    CustomRechargeAmount customRechargeAmount;
    @BindView(R.id.rg)
    RadioGroup rg;


    private PayWayManagerContract.Presenter presenter;

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
        presenter = new PaymentManagerPresenterImp(this);
        if (memberPayInfoVO != null) {
            tvEmailValue.setText(memberPayInfoVO.getMemberId());
            tvPayAmount.setText(R.string.zero_yuan);
            tvReceiveUsername.setText(memberPayInfoVO.getBankPersonalName());
            tvReceiveAccount.setText(memberPayInfoVO.getBankName());
            tvReceiveBank.setText(memberPayInfoVO.getBankAccount());
            tvPaymentNote.setText("987657");
        }

    }

    @Override
    public void initListener() {
        customRechargeAmount.setRadioButtonCheckListener(new RadioButtonCheckListener() {
            @Override
            public void onChange(boolean isCheck) {
                if (isCheck) {
                    LogTool.d(TAG, isCheck);
                    //清空其它的选择
                    rbOne.setChecked(false);
                    rbThree.setChecked(false);
                    rbTwo.setChecked(false);
                }
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                customRechargeAmount.resetContent(true);
                switch (checkedId) {
                    case R.id.rb_one:
                        break;
                    case R.id.rb_two:
                        break;
                    case R.id.rb_three:
                        break;
                }
            }
        });

        customRechargeAmount.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {
                if (tvPayAmount != null) {
                    tvPayAmount.setText(content + getString(R.string.yuan));
                }
            }

            @Override
            public void onAction(String from) {

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
                        //step 1:判断当前输入不能为空
                        String rechargeAmount = customRechargeAmount.getContent();
                        if (StringTool.isEmpty(rechargeAmount)) {
                            //如果当前不是自定义，那么判断是否选择了其它
                            if (!rbOne.isChecked()) {
                                if (!rbTwo.isChecked()) {
                                    if (!rbThree.isChecked()) {
                                        showToast(getString(R.string.please_input_recharge_amount));
                                        return;

                                    } else {
                                        rechargeAmount = "1000";
                                    }
                                } else {
                                    rechargeAmount = "500";
                                }
                            } else {
                                rechargeAmount = "100";
                            }
                        }

                        String imageCode = etwaImageCode.getContent();
                        if (StringTool.isEmpty(imageCode)) {
                            showToast(getString(R.string.please_input_verify_code_first));
                            return;
                        }
                        String mark = tvPaymentNote.getText().toString();

                        //Step 2:根据用户输入的内容请求数据
                        presenter.rechargeVirtualCoin(Constants.Payment.RECHARGE_VIRTUAL_COIN, Constants.CURRENCY_TYPE_SCS, rechargeAmount, mark, imageCode);

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
            case Constants.Payment.RECHARGE_VIRTUAL_COIN:
                //充值申请点击后的提示：您的充值申请已提交，请尽快使用已绑定的银行账号进行转账！
                showToast(getString(R.string.recharge_success_tips));
                break;
        }

    }

    @Override
    public void responseFailed(String message, String type) {
        showToast(message);
    }


}
