package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.text.InputFilter;
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
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.listener.RadioButtonCheckListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.decimal.DecimalTool;
import io.bcaas.exchange.ui.contracts.PayWayManagerContract;
import io.bcaas.exchange.ui.presenter.PaymentManagerPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.view.viewGroup.CustomRechargeAmount;
import io.bcaas.exchange.vo.CenterInfoVO;
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

public class RechargeDetailActivity extends BaseActivity
        implements PayWayManagerContract.View {
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
    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.tv_three)
    TextView tvThree;
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


    private PayWayManagerContract.Presenter presenter;

    private CenterInfoVO centerInfoVO;
    private Constants.RechargeNumber rechargeNumber;//记录当前的充值数量

    @Override
    public int getContentView() {
        return R.layout.activity_recharge_detail;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
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
        //获取当前的银行账户信息
        presenter.getBankInfo(Constants.Payment.GET_BANK_INFO);

    }

    @Override
    public void initListener() {
        customRechargeAmount.setRadioButtonCheckListener(new RadioButtonCheckListener() {
            @Override
            public void onChange(boolean isCheck) {
                LogTool.d(TAG, isCheck);
                if (isCheck) {
                    //清空其它的选择
                    resetTextViewStatus(tvOne);
                    resetTextViewStatus(tvTwo);
                    resetTextViewStatus(tvThree);
                }
            }


        });
        changeRadioButtonStatus(tvOne, 1);
        changeRadioButtonStatus(tvTwo, 2);
        changeRadioButtonStatus(tvThree, 3);
        customRechargeAmount.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {
                if (tvPayAmount != null) {
                    StringBuffer sbRechargeAmount = new StringBuffer(DecimalTool.transferDisplay(2, content, Constants.Pattern.TWO_DISPLAY))
                            .append(getString(R.string.yuan));
                    tvPayAmount.setText(sbRechargeAmount);
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
                            if (rechargeNumber != null) {
                                rechargeAmount = rechargeNumber.getNumber();
                            } else {
                                showToast(getString(R.string.please_input_recharge_amount));
                                return;
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

    /**
     * 改变当前单选按钮的状态
     *
     * @param textView
     * @param position
     */
    private void changeRadioButtonStatus(TextView textView, int position) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空所有的数据，然后重新赋值
                customRechargeAmount.resetContent(true);
                resetTextViewStatus(tvOne);
                resetTextViewStatus(tvTwo);
                resetTextViewStatus(tvThree);
                boolean currentStatus;
                if (rechargeNumber != null) {
                    int index = rechargeNumber.getIndex();
                    if (index == position) {
                        currentStatus = rechargeNumber.getIsCheck(position);

                    } else {
                        if (position == 1) {
                            rechargeNumber = Constants.RechargeNumber.ONE_HUNDRED;
                        } else if (position == 2) {
                            rechargeNumber = Constants.RechargeNumber.FIVE_HUNDRED;
                        } else if (position == 3) {
                            rechargeNumber = Constants.RechargeNumber.ONE_THOUSAND;
                        }
                        currentStatus = false;
                    }
                } else {
                    if (position == 1) {
                        rechargeNumber = Constants.RechargeNumber.ONE_HUNDRED;
                    } else if (position == 2) {
                        rechargeNumber = Constants.RechargeNumber.FIVE_HUNDRED;
                    } else if (position == 3) {
                        rechargeNumber = Constants.RechargeNumber.ONE_THOUSAND;
                    }
                    currentStatus = false;
                }
                rechargeNumber.setIndex(position);
                rechargeNumber.setCheck(!currentStatus);
                textView.setCompoundDrawablePadding(context.getResources().getDimensionPixelOffset(R.dimen.d5));
                textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(currentStatus ? R.mipmap.icon_choose : R.mipmap.icon_choose_f
                ), null, null, null);
                String recharge = rechargeNumber.getNumber();
                if (tvPayAmount != null) {
                    tvPayAmount.setText(recharge + getString(R.string.yuan));
                }
            }
        });

    }

    /**
     * 重置当前文本的状态
     *
     * @param textView
     */
    private void resetTextViewStatus(TextView textView) {
        textView.setCompoundDrawablePadding(context.getResources().getDimensionPixelOffset(R.dimen.d5));
        textView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.icon_choose
        ), null, null, null);
        rechargeNumber = Constants.RechargeNumber.CUSTOM_NUMBER;
    }

    @Override
    public <T> void responseSuccess(T message, String type) {
        switch (type) {
            case Constants.Payment.RECHARGE_VIRTUAL_COIN:
                //充值申请点击后的提示：您的充值申请已提交，请尽快使用已绑定的银行账号进行转账！
                showToast(getString(R.string.recharge_success_tips));
                finish();
                break;
            case Constants.Payment.GET_BANK_INFO:
                CenterInfoVO centerInfoVO = (CenterInfoVO) message;
                //取出当前第一条数据，然后传入下一个界面
                if (centerInfoVO != null) {
                    tvEmailValue.setText(BaseApplication.getMemberID());
                    tvPayAmount.setText(R.string.zero_yuan);
                    tvReceiveUsername.setText(centerInfoVO.getBankPersonalName());
                    tvReceiveAccount.setText(centerInfoVO.getBankName());
                    tvReceiveBank.setText(centerInfoVO.getBankAccount());
                    // TODO: 2019/3/29 服务器生成随机数
                    tvPaymentNote.setText("987657");
                }
                break;
        }

    }

    @Override
    public void responseFailed(String message, String type) {
        switch (type) {
            case Constants.Payment.ADD_PAY_WAY:
                showToast(message);
                break;
            default:
                showToast(message);
                break;
        }

    }


}
