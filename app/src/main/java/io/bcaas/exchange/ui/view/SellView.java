package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.SellDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.JsonTool;
import io.bcaas.exchange.listener.AmountEditTextFilter;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.decimal.DecimalTool;
import io.bcaas.exchange.ui.contracts.GetCurrencyChargeContract;
import io.bcaas.exchange.ui.presenter.GetCurrencyChargePresenterImp;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「售出」页面视图
 */
public class SellView extends BaseLinearLayout implements GetCurrencyChargeContract.View {
    private String TAG = SellView.class.getSimpleName();

    @BindView(R.id.tv_salable_balance)
    TextView tvSalableBalance;
    @BindView(R.id.et_rate)
    EditText etRate;
    @BindView(R.id.tv_exchange_currency)
    TextView tvExchangeCurrency;
    @BindView(R.id.et_sell_volume)
    EditText etSellVolume;
    @BindView(R.id.tv_current_currency)
    TextView tvCurrentCurrency;
    @BindView(R.id.sb_progress)
    SeekBar sbProgress;
    @BindView(R.id.tv_progress_speed)
    TextView tvProgressSpeed;
    @BindView(R.id.tv_fee_introduction)
    TextView tvFeeIntroduction;
    @BindView(R.id.tv_final_tx_amount)
    TextView tvFinalTxAmount;
    @BindView(R.id.btn_sell)
    Button btnSell;
    private OnItemSelectListener onItemSelectListener;

    private MemberKeyVO memberKeyVO;

    //当前的汇率，做进度条的系数使用
    private String salableBalance;
    //得到当前最终的交易额度
    private String txAmount;

    //得到当前的币种信息
    private CurrencyListVO currencyListVO, exchangeCurrencyListVO;
    //得到当前的手续费
    private String fee;
    private GetCurrencyChargeContract.Presenter presenter;
    //进度条的边界值
    private int margin = getResources().getDimensionPixelSize(R.dimen.d20);

    public SellView(Context context) {
        super(context);
    }

    @Override
    protected int setContentView() {
        return R.layout.view_sell;
    }

    @Override
    protected void initView() {
        presenter = new GetCurrencyChargePresenterImp(this);
        etSellVolume.setFilters(new InputFilter[]{new AmountEditTextFilter().setDigits(10)});
        etRate.setFilters(new InputFilter[]{new AmountEditTextFilter().setDigits(10)});
    }

    @Override
    protected void initListener() {
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (tvProgressSpeed != null && fromUser) {
                    //seekBar的宽度
                    float seekBarWidth = seekBar.getWidth();
                    float width = (seekBarWidth - margin * 3) / 100 * progress; //seekBar当前位置的宽度
                    tvProgressSpeed.setX(width + margin);
                    //得到当前的交易量
                    String sellVolume = String.valueOf(Float.valueOf(salableBalance) * 0.01f * progress);
                    tvProgressSpeed.setText(sellVolume);

                    if (etSellVolume != null) {
                        etSellVolume.setText(sellVolume);
                        etSellVolume.setSelection(sellVolume.length());
                    }

                    setTxAmountInfo(sellVolume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        etSellVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (StringTool.notEmpty(text)
                        && sbProgress != null
                        && tvProgressSpeed != null) {
                    float volume = Float.valueOf(text);
                    if (volume > 0) {
                        sbProgress.setProgress((int) (100 * volume / Float.valueOf(salableBalance)));
                        tvProgressSpeed.setText(String.valueOf(volume));
                        float seekBarWidth = sbProgress.getWidth();
                        float width = (seekBarWidth - margin * 3) / 100 * sbProgress.getProgress(); //seekBar当前位置的宽度
                        tvProgressSpeed.setX(width + margin);
                        setTxAmountInfo(text);
                    } else {
                        resetProgress();
                    }

                } else {
                    resetProgress();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        RxView.clicks(btnSell).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断当前卖出价是否输入
                        String unitPrice = etRate.getText().toString();
                        if (StringTool.isEmpty(unitPrice)) {
                            Toast.makeText(context, R.string.please_input_sell_price, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //2：判断当前卖出量是否输入
                        String sellAmount = etSellVolume.getText().toString();
                        if (StringTool.isEmpty(sellAmount) || StringTool.equals(sellAmount, "0.0")) {
                            Toast.makeText(context, R.string.please_input_sell_volume, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //3：比对当前输入的卖出量《可售余额
                        if (StringTool.equals(DecimalTool.calculateFirstSubtractSecondValue(salableBalance, sellAmount), MessageConstants.NO_ENOUGH_BALANCE)) {
                            Toast.makeText(context, R.string.no_enough_balance, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //4：判断当前的扣除手续费的交易额不能小于等于0，否则提示不能通过
                        if (StringTool.notEmpty(txAmount) &&
                                StringTool.equals(DecimalTool.calculateFirstSubtractSecondValue(txAmount, "0"), MessageConstants.NO_ENOUGH_BALANCE)) {
                            Toast.makeText(context, R.string.invalid_tx_amount, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //5:判断当前的回调以及会员信息不为空；得到所有需要的数据，然后返回
                        if (onItemSelectListener != null && memberKeyVO != null) {
                            //可用余额
                            SellDataBean sellDataBean = new SellDataBean(memberKeyVO.getBalanceAvailable());
                            //卖出量
                            sellDataBean.setSellAmount(sellAmount);
                            //单价
                            sellDataBean.setUnitPrice(unitPrice);
                            //交换的币种uid  && 币种名称
                            if (exchangeCurrencyListVO != null) {
                                sellDataBean.setExchangeCurrencyUid(exchangeCurrencyListVO.getCurrencyUid());
                                sellDataBean.setExchangeCurrencyName(exchangeCurrencyListVO.getEnName());
                            }
                            //当前币种信息不为空
                            if (currencyListVO != null) {
                                //当前币种的中文名称
                                sellDataBean.setCnName(currencyListVO.getCnName());
                                // 当前币种的英文名称
                                sellDataBean.setEnName(currencyListVO.getEnName());
                                //当前币种的UID
                                sellDataBean.setCurrencyUid(currencyListVO.getCurrencyUid());

                            }
                            // 手续费
                            sellDataBean.setGasFeeCharge(fee);
                            //得到当前最终的交易额度
                            sellDataBean.setTxAmountExceptFee(txAmount);
                            // 直接返回扣除手续费的交易额度文字信息
                            sellDataBean.setTxAmountExceptFeeString(tvFinalTxAmount.getText().toString());
                            //回调，进入下一个页面
                            onItemSelectListener.onItemSelect(sellDataBean, Constants.From.SELL_VIEW);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Disposable subscribe = RxView.clicks(tvExchangeCurrency).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        // 点击可以切换币种
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(currencyListVO, Constants.From.SELL_SELECT_CURRENCY);
                        }
                    }
                });
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    /**
     * 计算得到交易额信息
     *
     * @param sellVolume 交易量
     */
    private void setTxAmountInfo(String sellVolume) {
        //判断显示交易额/单价/以及用户币种信息数据是否为空
        if (tvFinalTxAmount != null && etRate != null && exchangeCurrencyListVO != null) {
            //得到当前的单价
            String rateStr = etRate.getText().toString();
            //如果当前没有输入单价，则不显示交易额
            if (StringTool.isEmpty(rateStr)) {
                return;
            }
            // 得到当前除去手续费之后的交易额度
//                        1.卖出方手续费单位为出售币种的单位；
//
//                        2.卖出方手续费具体比例待定；
//                        举例，假如用户出售10个BTC,此时 1 BTC = 20 ETH, 手续费为0.1 BTC 则：
//                        手续费 = 0.1 BTC
//                                交易额 = (10 - 0.1) * 20 = 200 ETH
//                        4.用户输入资金密码后，该用户的该笔出售金额被冻结，余额不扣减
            // 得到当前减去手续费的量
            String volumeExceptFee = DecimalTool.calculateFirstSubtractSecondValue(sellVolume, fee);
            //得到当前可换做售出币种的交易额
            txAmount = DecimalTool.calculateFirstmultiplySecondValue(volumeExceptFee, rateStr);
            // 判断当前的交易额是否为0，如果为0，则不显示
//            if (StringTool.equals(DecimalTool.calculateFirstSubtractSecondValue(txAmount, "0"),
//                    MessageConstants.NO_ENOUGH_BALANCE)) {
//                tvFinalTxAmount.setVisibility(GONE);
//            } else {
                tvFinalTxAmount.setVisibility(VISIBLE);
                tvFinalTxAmount.setText(context.getResources().getString(R.string.sell_out_transaction_amount)
                        + txAmount + "  " + exchangeCurrencyListVO.getEnName());
//            }

        }
    }

    /**
     * 根据传入的数据，刷新当前页面
     *
     * @param memberKeyVO
     */
    public void refreshData(MemberKeyVO memberKeyVO) {
        this.memberKeyVO = memberKeyVO;
        LogTool.d(TAG, "refreshData:" + memberKeyVO);
        if (memberKeyVO != null) {
            currencyListVO = memberKeyVO.getCurrencyListVO();
            if (currencyListVO == null) {
                return;
            }
            if (tvSalableBalance != null) {
                tvSalableBalance.setText(String.format(getContext().getString(R.string.format_sss), context.getResources().getString(R.string.salable_balance),
                        memberKeyVO.getBalanceAvailable(),
                        currencyListVO.getEnName()));
            }
            getCurrencyCharge();
            if (tvCurrentCurrency != null) {
                tvCurrentCurrency.setText(currencyListVO.getEnName());
            }
            //重置界面值
            if (etRate != null) {
                etRate.setText("1");
            }
            if (etSellVolume != null) {
                etSellVolume.setText("0.0");
            }
            resetProgress();
            //置空当前的交易额信息
            txAmount = MessageConstants.EMPTY;
            if (tvFinalTxAmount != null) {
                tvFinalTxAmount.setVisibility(GONE);
            }
            if (tvExchangeCurrency != null) {
                // 得到当前的所有币种，排出当前的币种取下一个
                exchangeCurrencyListVO = JsonTool.getNextCurrency(currencyListVO.getEnName());
                if (exchangeCurrencyListVO != null) {
                    tvExchangeCurrency.setText(exchangeCurrencyListVO.getEnName());
                    tvExchangeCurrency.setCompoundDrawablePadding(context.getResources().getDimensionPixelOffset(R.dimen.d8));
                    tvExchangeCurrency.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.icon_drop_down), null);


                }
            }
            salableBalance = DecimalTool.getCalculateString(memberKeyVO.getBalanceAvailable());
        }

    }


    private void resetProgress() {
        if (sbProgress != null) {
            sbProgress.setProgress(0);
        }
        if (tvProgressSpeed != null) {
            tvProgressSpeed.setText("0.0");
            tvProgressSpeed.setX(margin);
        }
    }

    /**
     * 取得当前的汇率
     */
    public void getCurrencyCharge() {
        presenter.getCurrencyCharge(currencyListVO.getCurrencyUid());
    }

    /**
     * 刷新当前的交换信息
     *
     * @param memberKeyVO
     */
    public void refreshExchangeCurrency(MemberKeyVO memberKeyVO) {
        if (memberKeyVO != null) {
            exchangeCurrencyListVO = memberKeyVO.getCurrencyListVO();
            if (exchangeCurrencyListVO != null) {
                tvExchangeCurrency.setText(exchangeCurrencyListVO.getEnName());
            }

        }
    }

    @Override
    public void getCurrencyChargeFailure(String info) {
        LogTool.e(TAG, info);
    }

    @Override
    public void getCurrencyChargeSuccess(CurrencyListVO currencyListVO) {
        if (currencyListVO != null) {
            fee = currencyListVO.getSellCharge();
            if (tvFeeIntroduction != null) {
                tvFeeIntroduction.setText(context.getResources().getString(R.string.sell_out_fee_text) + "  " + fee + "  " + currencyListVO.getEnName());
            }
        }

    }
}
