package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.SellDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「售出」页面视图
 */
public class SellView extends LinearLayout {


    @BindView(R.id.tv_salable_balance)
    TextView tvSalableBalance;
    @BindView(R.id.tv_exchange_rate)
    TextView tvExchangeRate;
    @BindView(R.id.tv_exchange_currency)
    TextView tvExchangeCurrency;
    @BindView(R.id.tv_sell_volume)
    TextView tvSellVolume;
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

    private SellDataBean sellDataBean;

    //当前的汇率，做进度条的系数使用
    private float salableBalance;

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
                    String sellVolume = String.valueOf(salableBalance * progress);
                    tvProgressSpeed.setText(sellVolume);
                    if (tvSellVolume != null) {
                        tvSellVolume.setText(sellVolume);
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
        RxView.clicks(btnSell).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (onItemSelectListener != null) {
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
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    /**
     * 根据传入的数据，刷新当前页面
     *
     * @param sellDataBean
     */
    public void refreshData(SellDataBean sellDataBean) {
        this.sellDataBean = sellDataBean;
        if (sellDataBean != null) {
            if (tvSalableBalance != null) {
                tvSalableBalance.setText(String.format("%s%s   %s", context.getResources().getString(R.string.salable_balance), sellDataBean.getSalableBalance(), sellDataBean.getCurrency()));
            }
            if (tvCurrentCurrency != null) {
                tvCurrentCurrency.setText(sellDataBean.getCurrency());
            }
            if (tvExchangeRate != null) {
                tvExchangeRate.setText(sellDataBean.getExchangeRate());
            }
            if (tvExchangeCurrency != null) {
                tvExchangeCurrency.setText(sellDataBean.getExchangeCurrency());
            }
            salableBalance = Float.valueOf(sellDataBean.getSalableBalance()) * 0.01f;
        }

    }


}
