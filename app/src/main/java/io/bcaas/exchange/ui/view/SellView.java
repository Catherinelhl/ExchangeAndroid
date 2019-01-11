package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.CountryCodeBean;
import io.bcaas.exchange.bean.SellDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.JsonTool;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.decimal.DecimalTool;
import io.bcaas.exchange.view.pop.ListPop;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「售出」页面视图
 */
public class SellView extends LinearLayout {


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
    private Context context;
    private OnItemSelectListener onItemSelectListener;

    private MemberKeyVO memberKeyVO;

    //当前的汇率，做进度条的系数使用
    private String salableBalance;
    //得到当前最终的交易额度
    private double txAmount;

    //得到当前的币种信息
    private CurrencyListVO currencyListVO, exchangeCurrencyListVO;
    // TODO: 2019/1/11 暂时手续费是0.0001
    //得到当前的手续费
    private String fee = "0.0001";

    public SellView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_sell, this, true);
        ButterKnife.bind(view);
        initListener();
    }

    private void initListener() {
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (tvProgressSpeed != null) {
                    float seekBarWidth = seekBar.getWidth();//seekBar的宽度
                    int margin = getResources().getDimensionPixelSize(R.dimen.d20);
                    float width = (seekBarWidth - margin * 3) / 100 * progress; //seekBar当前位置的宽度
                    tvProgressSpeed.setX(width + margin);
                    String sellVolume = String.valueOf(Float.valueOf(salableBalance) * 0.01f * progress);
                    tvProgressSpeed.setText(sellVolume);
                    if (etSellVolume != null) {
                        etSellVolume.setText(sellVolume);
                    }
                    if (tvFinalTxAmount != null && etRate != null && currencyListVO != null) {
                        // 得到当前的卖出量
                        double sellAmount = Double.valueOf(sellVolume);
                        //得到当前的单价
                        double rate = Double.valueOf(etRate.getText().toString());
                        // 得到当前的币种
                        String enName = currencyListVO.getEnName();
                        // 得到当前除去手续费之后的交易额度
                        txAmount = sellAmount * rate - Double.valueOf(fee);
                        if (txAmount > 0) {
                            tvFinalTxAmount.setVisibility(VISIBLE);
                            tvFinalTxAmount.setText(context.getResources().getString(R.string.sell_out_transaction_amount)
                                    + txAmount + "\t" + enName);
                        } else {
                            tvFinalTxAmount.setVisibility(INVISIBLE);

                        }

                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
                            Toast.makeText(context, "请先输入卖出价！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //2：判断当前卖出量是否输入
                        String sellAmount = etSellVolume.getText().toString();
                        if (StringTool.isEmpty(sellAmount)) {
                            Toast.makeText(context, "请先输入卖出量！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //3：比对当前输入的卖出量《可售余额
                        if (Double.valueOf(sellAmount) > Double.valueOf(salableBalance)) {
                            Toast.makeText(context, "余额不足！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //4:判断当前的回调以及会员信息不为空；得到所有需要的数据，然后返回
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
                            // TODO: 2019/1/11 字段需要重构
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
     * 根据传入的数据，刷新当前页面
     *
     * @param memberKeyVO
     */
    public void refreshData(MemberKeyVO memberKeyVO) {
        this.memberKeyVO = memberKeyVO;
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
            if (tvCurrentCurrency != null) {
                tvCurrentCurrency.setText(currencyListVO.getEnName());
            }
            if (etRate != null) {
                etRate.setText("1");
            }
            if (tvExchangeCurrency != null) {
                // 得到当前的所有币种，排出当前的币种取下一个
                exchangeCurrencyListVO = JsonTool.getNextCurrency(currencyListVO.getEnName());
                if (exchangeCurrencyListVO != null) {
                    tvExchangeCurrency.setText(exchangeCurrencyListVO.getEnName());

                }
            }
            salableBalance = DecimalTool.getCalculateString(memberKeyVO.getBalanceAvailable());
        }

    }

    /**
     * 刷新当前的交换信息
     *
     * @param memberKeyVO
     */
    public void refreshExchangeCurrency(MemberKeyVO memberKeyVO) {
        if (memberKeyVO!=null){
            exchangeCurrencyListVO=memberKeyVO.getCurrencyListVO();
            if (exchangeCurrencyListVO != null) {
                tvExchangeCurrency.setText(exchangeCurrencyListVO.getEnName());
            }

        }
    }
}
