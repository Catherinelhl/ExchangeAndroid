package io.bcaas.exchange.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
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
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.bean.CoinMarketCapBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.chart.ValueMarkerView;
import io.bcaas.exchange.tools.chart.XLineValueFormatter;
import io.bcaas.exchange.tools.chart.YLineValueFormatter;
import io.bcaas.exchange.tools.file.FilePathTool;
import io.bcaas.exchange.tools.file.ResourceTool;
import io.bcaas.exchange.ui.contracts.GetCoinMarketCapContract;
import io.bcaas.exchange.ui.presenter.GetCoinMarketCapPresenterImp;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 多数据
 * <p>
 * 接入CoinMarketCap真实数据
 */

public class GetCoinMarketCapActivity extends BaseActivity
        implements OnChartValueSelectedListener, GetCoinMarketCapContract.View {
    private String TAG = GetCoinMarketCapActivity.class.getSimpleName();

    @BindView(R.id.cb_usd)
    CheckBox cbUsd;
    @BindView(R.id.cb_btc)
    CheckBox cbBtc;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.line_chart)
    LineChart chart;

    //存储当前currency市值
    private List<List<Double>> valueMarket = new ArrayList<>();
    //    存储当前currency交易量
    private List<List<Double>> volumeUSD = new ArrayList<>();
    //存储当前currency以BTC为参考
    private List<List<Double>> priceBTC = new ArrayList<>();
    //存储当前currency以USD为参照物
    private List<List<Double>> priceUSD = new ArrayList<>();
    //默认当前的币种为bitCoin
    private String coinName = "bitcoin";

    private GetCoinMarketCapContract.Presenter presenter;
    //得到当前币种所有的信息
    private CoinMarketCapBean coinMarketCapBean;

    private LineData data;
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
//                    long endTime = System.currentTimeMillis();
//                    long startTime = 0l;
//
//                    switch (cycleTimeType) {
//
//                        case oneDay:
//                            startTime = endTime - 24 * 60 * 60 * 1000;
//                            break;
//                        case sevenDay:
//                            startTime = endTime - 24 * 7 * 60 * 60 * 1000;
//                            break;
//                        case oneMonth:
//                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
//
//                            break;
//                        case threeMonth:
//                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
//                            break;
//                        case oneYear:
//                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
//                            break;
//                        case YTD:// 年初至今
////                            endTime = getPastTimeOfEndByCycleTime(cycleTimeType).getTime();
////                            startTime = getPastTimeOfStartByCycleTime(cycleTimeType).getTime();
//                            startTime = DateFormatTool.getCurrentYearStartTime().getTime();
//                            break;
//                        case ALL:
//                            endTime = 0l;
//                            startTime = 0l;
//                            break;
        //默认获取所有的数据信息
        presenter.getCoinMarketCap(coinName, System.currentTimeMillis() - 24 * 60 * 60 * 1000, System.currentTimeMillis());
    }

    /**
     * 初始化线性图表¬
     */
    private void initLineChart() {
        //自定义一个底部显示内容格式化
        ValueFormatter custom = new XLineValueFormatter(priceUSD);
        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            // 如果当前没有数据返回，那么就是用默认的
            if (data != null && priceUSD.size() > 0) {
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
            if (data != null && priceUSD.size() > 0) {
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
        YAxis yAxisRight;
        {
            // Y-Axis Style
            yAxisRight = chart.getAxisRight();

            // disable dual axis (only use LEFT axis)
//            chart.getAxisRight().setEnabled(false);
            // 如果当前没有数据返回，那么就是用默认的
            if (data != null && priceUSD.size() > 0) {
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
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setTextColor(context.getResources().getColor(R.color.green_22ac22));
        // draw legend entries as lines
        l.setForm(Legend.LegendForm.EMPTY);

        data = new LineData();
        //添加USD
        data.addDataSet(initChartData(getValuesEntry(true, priceUSD), labelUSD));
        //添加BTC
        data.addDataSet(initChartData(getValuesEntry(false, priceBTC), labelBTC));
        // set data
        chart.setData(data);

    }

    /**
     * 根据是否显示chart图返回渲染数据
     *
     * @param isShow
     * @param data
     * @return
     */
    private List<Entry> getValuesEntry(boolean isShow, List<List<Double>> data) {
        ArrayList<Entry> values = new ArrayList<>();

        if (isShow) {
            for (int i = 0; i < data.size(); i++) {
                values.add(new Entry(i, Float.valueOf(String.valueOf(data.get(i).get(1)))));
            }
        }
        return values;
    }

    /**
     * 检查当前数据
     *
     * @param label 线型图的标签
     */
    private void checkData(String label, List<Entry> values) {
        LineDataSet setLineDataSet;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            String[] strings = chart.getData().getDataSetLabels();
            for (String s : strings) {
                LogTool.d(TAG, "labels:" + s);
            }
            setLineDataSet = (LineDataSet) chart.getData().getDataSetByLabel(label, true);
            if (StringTool.equals(label, labelUSD)) {
                if (setLineDataSet == null) {
                    initChartData(values, label);
                } else {
                    setLineDataSet.setValues(values);
                    setLineDataSet.notifyDataSetChanged();
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                }
            } else {
                LineDataSet setLineDataSetOther = (LineDataSet) chart.getData().getDataSetByLabel(labelBTC, true);
                if (setLineDataSetOther != null) {
                    ArrayList<Entry> valuesOther = new ArrayList<>();
                    for (int i = 0; i < priceBTC.size(); i++) {
                        valuesOther.add(new Entry(i, Float.valueOf(String.valueOf(priceBTC.get(i).get(1)))));
                    }
                    setLineDataSetOther.setValues(valuesOther);
                    setLineDataSetOther.notifyDataSetChanged();
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                }

            }


            if (StringTool.equals(label, labelBTC)) {
                if (setLineDataSet == null) {
                    initChartData(values, label);
                } else {
                    setLineDataSet.setValues(values);
                    setLineDataSet.notifyDataSetChanged();
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                }

            } else {
                LineDataSet setLineDataSetOther = (LineDataSet) chart.getData().getDataSetByLabel(labelUSD, true);
                if (setLineDataSetOther != null) {
                    ArrayList<Entry> valuesOther = new ArrayList<>();
                    for (int i = 0; i < priceUSD.size(); i++) {
                        valuesOther.add(new Entry(i, Float.valueOf(String.valueOf(priceUSD.get(i).get(1)))));
                    }
                    setLineDataSetOther.setValues(valuesOther);
                    setLineDataSetOther.notifyDataSetChanged();
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
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
    private LineDataSet initChartData(List<Entry> values, String label) {
        // create a dataset and give it a type
        LineDataSet setLineDataSet = new LineDataSet(values, label);
        setLineDataSet.setDrawIcons(false);
        // draw dashed line
//            setLineDataSet.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        if (StringTool.equals(label, labelUSD)) {
            setLineDataSet.setColor(context.getResources().getColor(R.color.green_22ac22));
        } else {
            setLineDataSet.setColor(context.getResources().getColor(R.color.yellow_FFA73B));

        }
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
//        setLineDataSet.setFillFormatter(new IFillFormatter() {
//            @Override
//            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                return chart.getAxisLeft().getAxisMinimum();
//            }
//        });

        // drawables only supported on api level 21 and above
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Drawable drawable = context.getDrawable(R.drawable.bg_button);
//                setLineDataSet.setFillDrawable(drawable);
//
//            } else {
//                setLineDataSet.setFillColor(Color.BLACK);
//
//            }
        // create a data object with the data sets
        return setLineDataSet;
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

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void initListener() {
        cbBtc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkData(labelBTC, getValuesEntry(isChecked, priceBTC));
                Legend l = chart.getLegend();
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                l.setTextColor(context.getResources().getColor(R.color.yellow_FFA73B));
                // draw legend entries as lines
                l.setForm(Legend.LegendForm.EMPTY);
            }
        });
        cbUsd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkData(labelUSD, getValuesEntry(isChecked, priceUSD));
                Legend l = chart.getLegend();
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                l.setTextColor(context.getResources().getColor(R.color.green_22ac22));
                // draw legend entries as lines
                l.setForm(Legend.LegendForm.EMPTY);

            }
        });
        Disposable subscribe = RxView.clicks(tvAction).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        String text = tvAction.getText().toString();
                        if (StringTool.equals(text, getString(R.string.retract))) {
                            tvAction.setText(R.string.expand);
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
        LogTool.d(TAG, "lineBean:" + valueMarket);
        priceBTC = coinMarketCapBean.getPrice_btc();
        LogTool.d(TAG, "priceBTC:" + priceBTC);
        priceUSD = coinMarketCapBean.getPrice_usd();
        LogTool.d(TAG, "priceUSD:" + priceUSD);
        volumeUSD = coinMarketCapBean.getVolume_usd();
        LogTool.d(TAG, "volumeUSD:" + volumeUSD);
        initLineChart();
    }

    @Override
    public void getCoinMarketCapFailure(String info) {
        showToast(info);
        String content = ResourceTool.getJsonFromAssets(FilePathTool.getJsonFileContent("priceUSD"));
        if (StringTool.notEmpty(content)) {
            priceUSD = GsonTool.convert(content, new TypeToken<List<List<Double>>>() {
            }.getType());
        }

        String contentBTC = ResourceTool.getJsonFromAssets(FilePathTool.getJsonFileContent("priceBTC"));

        if (StringTool.notEmpty(contentBTC)) {
            priceBTC = GsonTool.convert(contentBTC, new TypeToken<List<List<Double>>>() {
            }.getType());
        }

        if (ListTool.isEmpty(priceUSD)) {
            return;
        }
        //加载离线数据
        initLineChart();
    }
}
