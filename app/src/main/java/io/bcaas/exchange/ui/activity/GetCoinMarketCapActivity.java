package io.bcaas.exchange.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.bean.CoinMarketCapBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.chart.ValueMarkerView;
import io.bcaas.exchange.tools.chart.XLineValueFormatter;
import io.bcaas.exchange.tools.chart.YLineValueFormatter;
import io.bcaas.exchange.tools.time.DateFormatTool;
import io.bcaas.exchange.ui.contracts.GetCoinMarketCapContract;
import io.bcaas.exchange.ui.presenter.GetCoinMarketCapPresenterImp;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.bcaas.exchange.tools.time.DateFormatTool.getPastTimeOfStartByCycleTime;

/**
 * 多数据
 * <p>
 * 接入CoinMarketCap真实数据
 */

public class GetCoinMarketCapActivity extends BaseActivity
        implements OnChartValueSelectedListener, GetCoinMarketCapContract.View {
    @BindView(R.id.cb_usd)
    CheckBox cbUsd;
    @BindView(R.id.cb_btc)
    CheckBox cbBtc;
    @BindView(R.id.tv_action)
    TextView tvAction;
    private String TAG = GetCoinMarketCapActivity.class.getSimpleName();
    @BindView(R.id.tv_value_usd)
    TextView tvValueUsd;
    @BindView(R.id.tv_volume)
    TextView tvVolume;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.tv_value_btc)
    TextView tvValueBtc;
    @BindView(R.id.tv_value_market)
    TextView tvValueMarket;
    @BindView(R.id.line_chart)
    LineChart chart;
    @BindView(R.id.rg_cycle_time)
    RadioGroup rgCycleTime;
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    //用来得到数据获取之后的条数
    private int count;
    //存储当前currency市值
    private List<List<Double>> valueMarket = new ArrayList<>();
    //存储当前currency交易量
    private List<List<Double>> volumeUSD = new ArrayList<>();
    //存储当前currency以BTC为参考
    private List<List<Double>> priceBTC = new ArrayList<>();
    //存储当前currency以USD为参照物
    private List<List<Double>> priceUSD = new ArrayList<>();
    //存储当前可以切换的时间选择
    private List<Constants.CycleTime> cycleTime = new ArrayList<>();
    //存储当前new出的所有RadioButton
    private List<RadioButton> radioButtons = new ArrayList<>();
    //默认当前的币种为bitCoin
    private String coinName = "bitcoin";

    private GetCoinMarketCapContract.Presenter presenter;
    //得到当前币种所有的信息
    private CoinMarketCapBean coinMarketCapBean;

    private String labelBTC = "Price(BTC)", labelUSD = "Price(USD)";

    //  设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    @Override
    public int getContentView() {
        return R.layout.activity_coin_market_chart;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
    }

    @Override
    public void initView() {
        presenter = new GetCoinMarketCapPresenterImp(this);
        presenter.getCoinNameList();

    }

    @Override
    public void initData() {
        //初始化所有时间段选择
        cycleTime.add(Constants.CycleTime.oneDay);
        cycleTime.add(Constants.CycleTime.sevenDay);
        cycleTime.add(Constants.CycleTime.oneMonth);
        cycleTime.add(Constants.CycleTime.threeMonth);
        cycleTime.add(Constants.CycleTime.oneYear);
        cycleTime.add(Constants.CycleTime.YTD);//年初至今
        cycleTime.add(Constants.CycleTime.ALL);
        //初始化RadioGroup信息
        for (int i = 0; i < cycleTime.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(cycleTime.get(i).getName());
            radioButton.setGravity(Gravity.CENTER);
            radioButtons.add(radioButton);
            if (i == 0) {
                radioButton.setBackground(getResources().getDrawable(R.drawable.selector_black_left_style));
            } else if (i == cycleTime.size() - 1) {
                radioButton.setBackground(getResources().getDrawable(R.drawable.selector_black_right_style));
            } else {
                radioButton.setBackground(getResources().getDrawable(R.drawable.selector_black_middle_style));
            }
            radioButton.setButtonDrawable(null);
            rgCycleTime.addView(radioButton);
            final int finalI = i;
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.CycleTime cycleTimeType = cycleTime.get(finalI);
                    long endTime = System.currentTimeMillis();
                    long startTime = 0l;

                    switch (cycleTimeType) {

                        case oneDay:
                            startTime = endTime - 24 * 60 * 60 * 1000;
                            break;
                        case sevenDay:
                            startTime = endTime - 24 * 7 * 60 * 60 * 1000;
                            break;
                        case oneMonth:
                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();

                            break;
                        case threeMonth:
                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
                            break;
                        case oneYear:
                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
                            break;
                        case YTD:// 年初至今
//                            endTime = getPastTimeOfEndByCycleTime(cycleTimeType).getTime();
//                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
                            startTime = DateFormatTool.getCurrentYearStartTime().getTime();
                            break;
                        case ALL:
                            endTime = 0l;
                            startTime = 0l;
                            break;
                    }
                    presenter.getCoinMarketCap(coinName, startTime, endTime);
                }
            });
        }
        //默认获取所有的数据信息
        presenter.getCoinMarketCap(coinName, System.currentTimeMillis() - 24 * 60 * 60 * 1000, System.currentTimeMillis());
        //遍历当前的所有的cycleTime选项，然后默认指向all最后一个
        for (int i = 0; i < radioButtons.size(); i++) {
            if (i == radioButtons.size() - 1) {
                radioButtons.get(i).setChecked(true);
            }
        }
    }

    private void initRightChart(boolean isShow, String label, List<List<Double>> data) {
        ValueFormatter yLineValueFormatter = new YLineValueFormatter();
        YAxis yAxisRight;
        {
            // Y-Axis Style
            yAxisRight = chart.getAxisRight();

            // disable dual axis (only use LEFT axis)
//            chart.getAxisRight().setEnabled(false);
            // 如果当前没有数据返回，那么就是用默认的
            if (data != null && data.size() > 0) {
                yAxisRight.setValueFormatter(yLineValueFormatter);
            }
            // horizontal grid lines
            yAxisRight.enableGridDashedLine(10f, 10f, 0f);
            yAxisRight.setTextColor(context.getResources().getColor(R.color.yellow_FFA73B));
            yAxisRight.setTextSize(8);
            // axis range
//            yAxis.setAxisMaximum(200f);
//            yAxis.setAxisMinimum(-50f);
        }
        setData(isShow, label, data);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setTextColor(context.getResources().getColor(R.color.yellow_FFA73B));
        // draw legend entries as lines
        l.setForm(Legend.LegendForm.EMPTY);
    }

    /**
     * 初始化线性图表¬
     */
    private void initLineChart(boolean isShow, String label, List<List<Double>> data) {
        //自定义一个底部显示内容格式化
        ValueFormatter custom = new XLineValueFormatter(data);
        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            // 如果当前没有数据返回，那么就是用默认的
            if (data != null && data.size() > 0) {
                xAxis.setValueFormatter(custom);
            }
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        }

        {   // // Chart Style // //
            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);


            // create marker to display box when values are selected
            // Set the marker to the chart
            ValueMarkerView mv = new ValueMarkerView(this, coinMarketCapBean, custom);
            mv.setChartView(chart);
            chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        ValueFormatter yLineValueFormatter = new YLineValueFormatter();
        YAxis yAxisLeft;
        {
            // Y-Axis Style
            yAxisLeft = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
//            chart.getAxisRight().setEnabled(false);
            // 如果当前没有数据返回，那么就是用默认的
            if (data != null && data.size() > 0) {
                yAxisLeft.setValueFormatter(yLineValueFormatter);
            }
            // horizontal grid lines
            yAxisLeft.enableGridDashedLine(10f, 10f, 0f);
            yAxisLeft.setTextColor(context.getResources().getColor(R.color.green_22ac22));
            yAxisLeft.setTextSize(8);
            // axis range
//            yAxis.setAxisMaximum(200f);
//            yAxis.setAxisMinimum(-50f);
        }
//        {   // // Create Limit Lines // //
//            LimitLine llXAxis = new LimitLine(9f, "Index 10");
//            llXAxis.setLineWidth(4f);
//            llXAxis.enableDashedLine(10f, 10f, 0f);
//            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//            llXAxis.setTextSize(8f);
//            llXAxis.setTypeface(tfRegular);
//
//            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
//            ll1.setLineWidth(4f);
//            ll1.enableDashedLine(10f, 10f, 0f);
//            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//            ll1.setTextSize(8f);
//            ll1.setTypeface(tfRegular);
//
//            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//            ll2.setLineWidth(4f);
//            ll2.enableDashedLine(10f, 10f, 0f);
//            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//            ll2.setTextSize(8f);
//            ll2.setTypeface(tfRegular);
//
//            // draw limit lines behind data instead of on top
//            yAxisLeft.setDrawLimitLinesBehindData(true);
//            yAxisRight.setDrawLimitLinesBehindData(true);
//            xAxis.setDrawLimitLinesBehindData(true);

        // add limit lines
//            yAxis.addLimitLine(ll1);
//            yAxis.addLimitLine(ll2);
        //xAxis.addLimitLine(llXAxis);

//        }
        // draw points over time
        chart.animateX(1500);
        chart.setScaleYEnabled(false);

        setData(isShow, label, data);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setTextColor(context.getResources().getColor(R.color.green_22ac22));
        // draw legend entries as lines
        l.setForm(Legend.LegendForm.EMPTY);

    }

    /**
     * 开始绘制
     *
     * @param isShow    是否显示
     * @param label     线型图的标签
     * @param entryData 展示的数据
     */
    private void setData(boolean isShow, String label, List<List<Double>> entryData) {
        LineDataSet setLineDataSet;
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            values.add(new Entry(i, Float.valueOf(String.valueOf(entryData.get(i).get(1)))));
        }
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            String[] strings = chart.getData().getDataSetLabels();
            for (String s : strings) {
                LogTool.d(TAG, "labels:" + s);
            }
            //判断当前是否显示
            setLineDataSet = (LineDataSet) chart.getData().getDataSetByLabel(label, true);
            if (setLineDataSet == null) {
                initChartData(values, label);
            } else {
                if (isShow) {
                    setLineDataSet.setValues(values);
                    setLineDataSet.notifyDataSetChanged();
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                } else {
                    chart.getData().removeDataSet(setLineDataSet);
                }
            }
        } else {
            initChartData(values, label);
        }

    }

    /**
     * 初始化chart数据
     *
     * @param values
     * @param label
     */
    private void initChartData(ArrayList<Entry> values, String label) {
        // create a dataset and give it a type
        LineDataSet setLineDataSet = new LineDataSet(values, label);
        setLineDataSet.setDrawIcons(false);
        // draw dashed line
//            setLineDataSet.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        setLineDataSet.setColor(Color.GREEN);
        // 设置不画圆
        setLineDataSet.setDrawCircles(false);
        setLineDataSet.setDrawIcons(false);
//            setLineDataSet.setCircleColor(Color.GREEN);

        // line thickness and point size
        setLineDataSet.setLineWidth(1f);
//            setLineDataSet.setCircleRadius(1f);

        // draw points as solid circles
        setLineDataSet.setDrawCircleHole(false);

        // customize legend entry
        setLineDataSet.setFormLineWidth(1f);
//            setLineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        setLineDataSet.setFormSize(8f);
        setLineDataSet.setDrawValues(false);
        // text size of values
        setLineDataSet.setValueTextSize(8f);

        // draw selection line as dashed
//            setLineDataSet.enableDashedHighlightLine(10f, 5f, 0f);

        // set the filled area
        setLineDataSet.setDrawFilled(false);
        setLineDataSet.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMinimum();
            }
        });

        // drawables only supported on api level 21 and above
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Drawable drawable = context.getDrawable(R.drawable.bg_button);
//                setLineDataSet.setFillDrawable(drawable);
//
//            } else {
//                setLineDataSet.setFillColor(Color.BLACK);
//
//            }
        dataSets.add(setLineDataSet); // add the data sets
        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // set data
        chart.setData(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
//        int index = (int) e.getX();
//        if (priceUSD != null) {
//            tvTimer.setText("Time: " + getUTCDateForChart(priceUSD.get(index).get(0)));
//            tvValueUsd.setText("USD: " + priceUSD.get(index).get(1));
//        }
//        if (priceBTC != null) {
////            tvLow.setText("Time:" + priceBTC.get(index).get(0));
//            tvValueBtc.setText("BTC:" + priceBTC.get(index).get(1));
//        }
//
//        if (volumeUSD != null) {
////            tvNumber.setText("Time:" + volumeUSD.get(index).get(0));
//            tvVolume.setText("Volume:" + volumeUSD.get(index).get(1));
//        }
//        if (valueMarket != null) {
//            tvValueMarket.setText("Market USD:" + valueMarket.get(index).get(1));
//
//        }
//
//        LogTool.i(TAG, "Entry selected:" + e.toString());
//        LogTool.i(TAG, "low: " + chart.getLowestVisibleX() + ", high: " + chart.getHighestVisibleX());
//        LogTool.i(TAG, "MIN MAX xMin: " + chart.getXChartMin() + ", xMax: " + chart.getXChartMax() + ", yMin: " + chart.getYChartMin() + ", yMax: " + chart.getYChartMax());

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void initListener() {
        cbBtc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                initRightChart(isChecked, labelBTC, priceBTC);
            }
        });
        cbUsd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                initLineChart(isChecked, labelUSD, priceUSD);
            }
        });
        Disposable subscribe = RxView.clicks(tvAction).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        String text = tvAction.getText().toString();
                        if (StringTool.equals(text, getString(R.string.retract))) {
                            tvAction.setText("展开");
                        } else {
                            tvAction.setText(getString(R.string.retract));
                        }
                        tvAction.setCompoundDrawablePadding(context.getResources().getDimensionPixelOffset(R.dimen.d5));
                        tvAction.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.icon_drop_down), null);

                    }
                });
    }

    @Override
    public void getCoinNameListSuccess(List<CurrencyListVO> currencyListVOList) {
        LogTool.d(TAG, currencyListVOList);
    }

    @Override
    public void getCoinNameListFailure(String info) {
        showToast(info);
    }

    @Override
    public void getCoinMarketCapSuccess(CoinMarketCapBean coinMarketCapBean) {
        if (coinMarketCapBean == null) {
            return;
        }
        this.coinMarketCapBean = coinMarketCapBean;
        LogTool.d(TAG, coinMarketCapBean);
        valueMarket = coinMarketCapBean.getMarket_cap_by_available_supply();
        if (valueMarket == null || valueMarket.size() <= 0) {
            return;
        }
        count = valueMarket.size();
        LogTool.d(TAG, "lineBean:" + valueMarket);
        priceBTC = coinMarketCapBean.getPrice_btc();
        LogTool.d(TAG, "priceBTC:" + priceBTC);
        priceUSD = coinMarketCapBean.getPrice_usd();
        LogTool.d(TAG, "priceUSD:" + priceUSD);
        volumeUSD = coinMarketCapBean.getVolume_usd();
        LogTool.d(TAG, "volumeUSD:" + volumeUSD);
        initLineChart(true, labelUSD, priceUSD);
    }

    @Override
    public void getCoinMarketCapFailure(String info) {
        showToast(info);

    }
}
