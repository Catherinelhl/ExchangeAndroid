package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.widget.*;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseLinearLayout;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.listener.OnTextChangeListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.decimal.DecimalTool;
import io.bcaas.exchange.ui.contracts.GetCurrencyChargeContract;
import io.bcaas.exchange.ui.presenter.GetCurrencyChargePresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.RequestJson;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「提现」页面视图
 */
public class WithDrawView extends BaseLinearLayout implements GetCurrencyChargeContract.View {
    private String TAG = WithDrawView.class.getSimpleName();

    @BindView(R.id.tv_cashable_balance)
    TextView tvCashAbleBalance;
    @BindView(R.id.etwa_receive_address)
    EditTextWithAction etReceiveAddress;
    @BindView(R.id.etwa_withdraw_amount)
    EditTextWithAction etWithdrawAmount;
    @BindView(R.id.tv_fee_tips)
    TextView tvFeeTips;
    @BindView(R.id.etwa_remarks)
    EditTextWithAction etRemarks;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.rl_withdraw)
    RelativeLayout rlWithdraw;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.ll_with_draw_content)
    LinearLayout llWithDrawContent;
    private OnItemSelectListener onItemSelectListener;
    // 需要展现的交易费用：等于Minner's fee plus Handling's Fee
    private String transactionFee;
    private CurrencyListVO currencyListVO;
    private MemberKeyVO memberKeyVO;
    //得到可提现余额
    private String balanceAvailable;

    private GetCurrencyChargeContract.Presenter presenter;


    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }


    public WithDrawView(Context context) {
        super(context);
        presenter = new GetCurrencyChargePresenterImp(this);
    }

    @Override
    protected int setContentView() {
        return R.layout.view_with_draw;
    }

    @Override
    protected void initView() {
        etReceiveAddress.setFrom(Constants.EditTextFrom.WITHDRAW_SCAN);
        etWithdrawAmount.setFrom(Constants.EditTextFrom.WITHDRAW_AMOUNT);

    }

    @Override
    protected void initListener() {
        hideSoftKeyBoardByTouchView(llWithDrawContent);
        etWithdrawAmount.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(String content) {
                if (StringTool.isEmpty(content)) {
                    return;
                }
                if (StringTool.equals(content, ".")) {
                    return;

                }                //判断当前是否大于0
                float volume = Float.valueOf(content);
                if (volume > 0) {
                    LogTool.d(TAG, "balanceAvailable:" + balanceAvailable);
                    // 判断当前输入的数量是否大于可售余额，如果输入的是一个大于可售余额的数，那么直接显示可售余额
                    if (StringTool.equals(DecimalTool.calculateFirstSubtractSecondValue(balanceAvailable, content, true), MessageConstants.NO_ENOUGH_BALANCE)) {
                        etWithdrawAmount.setContent(balanceAvailable);
                    }
                } else {

                }

            }
        });
        etReceiveAddress.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
                //跳转扫描二维码
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(null, from);
                }
            }
        });

        etWithdrawAmount.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
                if (StringTool.notEmpty(balanceAvailable)) {
                    etWithdrawAmount.setContent(balanceAvailable);

                }
            }
        });
        RxView.clicks(btnSend).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断当前地址是否输入
                        String address = etReceiveAddress.getContent();
                        if (StringTool.isEmpty(address)) {
                            Toast.makeText(context, R.string.please_input_receive_address, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //2：判断当前想要提现的金额是否输入
                        String withDrawAmount = etWithdrawAmount.getContent();
                        if (StringTool.isEmpty(withDrawAmount)) {
                            Toast.makeText(context, R.string.please_input_amount_withdraw, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //3：输入的金额必须大于手续费
                        if (StringTool.equals(DecimalTool.calculateFirstSubtractSecondValue(withDrawAmount, transactionFee, false),
                                MessageConstants.NO_ENOUGH_BALANCE)) {
                            Toast.makeText(context, R.string.amount_must_be_more_than_fee, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MemberOrderVO memberOrderVO = new MemberOrderVO();
                        memberOrderVO.setPrice(withDrawAmount);
                        memberOrderVO.setMark(etRemarks.getContent());
                        RequestJson requestJson = new RequestJson();
                        requestJson.setMemberOrderVO(memberOrderVO);
                        MemberKeyVO memberKeyVOTemp = new MemberKeyVO();
                        memberKeyVOTemp.setAddress(address);
                        requestJson.setMemberKeyVO(memberKeyVOTemp);
                        CurrencyListVO currencyListVO = new CurrencyListVO();
                        currencyListVO.setCurrencyUid(memberKeyVO.getCurrencyListVO().getCurrencyUid());
                        requestJson.setCurrencyListVO(currencyListVO);
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(requestJson, Constants.From.WITHDRAW_SURE);
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

    /**
     * 更新当前界面信息
     *
     * @param memberKeyVO
     */
    public void refreshData(MemberKeyVO memberKeyVO) {
        if (memberKeyVO != null) {
            this.memberKeyVO = memberKeyVO;
            LogTool.d(TAG, "refreshData:" + memberKeyVO);
            currencyListVO = memberKeyVO.getCurrencyListVO();
            if (currencyListVO != null) {
                getCurrencyCharge();
                String enName = currencyListVO.getEnName();
                String uid = currencyListVO.getCurrencyUid();
                if (tvCashAbleBalance != null) {
                    String balance = memberKeyVO.getBalanceAvailable();
                    String balanceAvailableDisplay = StringTool.getDisplayAmountByUId(balance, uid);
                    balanceAvailable = StringTool.getTransferStoreDatabaseAmountByUId(balance, uid);
                    LogTool.d(TAG, "refreshData:balanceAvailable:" + balanceAvailable);
                    tvCashAbleBalance.setText(context.getResources().getString(R.string.cash_able_balance) + " " + balanceAvailableDisplay + "  " + enName);
                }
                if (etReceiveAddress != null) {
                    etReceiveAddress.setHint(String.format(context.getString(R.string.receive_address), enName));
                }
                if (etWithdrawAmount != null) {
                    etWithdrawAmount.setRightText(context.getString(R.string.all_in));
                    etWithdrawAmount.setUID(uid);
                }
            }
        }


    }

    /**
     * 取得当前的汇率
     */
    public void getCurrencyCharge() {
        presenter.getCurrencyCharge(currencyListVO.getCurrencyUid());
    }

    /**
     * 将当前扫描的值返回
     *
     * @param scanInfo
     */
    public void setScanInfo(String scanInfo) {
        if (etReceiveAddress != null) {
            etReceiveAddress.setContent(scanInfo);
        }
    }

    @Override
    public void getCurrencyChargeFailure(String info) {
        LogTool.e(TAG, info);
    }

    @Override
    public void getCurrencyChargeSuccess(CurrencyListVO currencyListVO) {
        if (currencyListVO != null) {
            transactionFee = DecimalTool.calculateFirstAddSecondValue(currencyListVO.getWithdrawCharge(), currencyListVO.getGasFeeCharge());
            if (tvFeeTips != null) {
                tvFeeTips.setText(context.getResources().getString(R.string.withdraw_need_fee) + " " + StringTool.getDisplayAmountByUId(transactionFee, currencyListVO.getCurrencyUid()) + "  " + currencyListVO.getEnName());
            }
        }

    }

}
