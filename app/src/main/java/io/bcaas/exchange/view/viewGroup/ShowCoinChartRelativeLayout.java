package io.bcaas.exchange.view.viewGroup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
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
import io.bcaas.exchange.bean.CoinMarketCapBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.event.LogoutEvent;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.chart.ValueMarkerView;
import io.bcaas.exchange.tools.chart.XLineValueFormatter;
import io.bcaas.exchange.tools.chart.YLineValueFormatter;
import io.bcaas.exchange.tools.otto.OttoTool;
import io.bcaas.exchange.ui.contracts.GetCoinMarketCapContract;
import io.bcaas.exchange.ui.presenter.GetCoinMarketCapPresenterImp;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/30
 * <p>
 * 自定義PopWindow：显示当前的币种曲线图
 */
public class ShowCoinChartRelativeLayout extends RelativeLayout
        implements OnChartValueSelectedListener, GetCoinMarketCapContract.View {
    private String TAG = ShowCoinChartRelativeLayout.class.getSimpleName();

    @BindView(R.id.rl_coin_chart_action)
    RelativeLayout rlCoinChartAction;
    @BindView(R.id.ll_coin_chart)
    LinearLayout llCoinChart;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    @BindView(R.id.cb_usd)
    CheckBox cbUsd;
    @BindView(R.id.cb_btc)
    CheckBox cbBtc;
    @BindView(R.id.tv_extract)
    TextView tvExtract;
    @BindView(R.id.tv_curve_name)
    TextView tvCurveName;
    @BindView(R.id.line_chart)
    LineChart chart;
    @BindView(R.id.rl_loading_data)
    RelativeLayout rlLoadingData;
    @BindView(R.id.rl_chart)
    RelativeLayout rlChart;
    @BindView(R.id.tv_expend)
    TextView tvExpend;
    private Context context;

    //存储当前currency市值
    private List<List<Double>> valueMarket = new ArrayList<>();
    //存储当前currency交易量
    private List<List<Double>> volumeUSD = new ArrayList<>();
    //存储当前currency以BTC为参考
    private List<List<Double>> priceBTC = new ArrayList<>();
    //存储当前currency以USD为参照物
    private List<List<Double>> priceUSD = new ArrayList<>();
    private GetCoinMarketCapContract.Presenter presenter;
    //得到当前币种所有的信息
    private CoinMarketCapBean coinMarketCapBean;

    private LineData data;
    private String labelBTC = "Price(BTC)", labelUSD = "Price(USD)";
    //当前coin用于请求数据的coinName
    private String coinName;

    public ShowCoinChartRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_show_coin_chart, this, true);
        ButterKnife.bind(view);
        this.context = context;
        presenter = new GetCoinMarketCapPresenterImp(this);
        initListener();
    }

    public void requestInfo(String coinName) {
        if (StringTool.isEmpty(coinName)) {
            LogTool.i(TAG, "coinName is Null");
            return;
        }
        //默认获取所有的数据信息,默认显示前一天
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
            xAxis.enableGridDashedLine(10f, 10f, 1f);
            // 如果当前没有数据返回，那么就是用默认的
            xAxis.setValueFormatter(custom);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //在缩放时为轴设置最小间隔。轴不允许低于那个限度。这可以用来避免缩放时的标签复制。
            xAxis.setGranularity(1f);
            //设置显示X轴的下横线
            xAxis.setDrawGridLines(false);
            //设置不显示X轴的下标线
            xAxis.setDrawAxisLine(false);
            xAxis.setTextColor(context.getResources().getColor(R.color.black_AEAEAE));
        }
        // 设置X放大的最小显示个数
        chart.setVisibleXRangeMinimum(5);
        //设置可以自动scale
        chart.setAutoScaleMinMaxEnabled(true);
        //是否绘制边线
        chart.setDrawBorders(false);

        //设置上下内边距
        //  chart.setMinOffset(1f);
        //图标周围格额外的偏移量
        // chart.setExtraOffsets(1f, 0f, 1f, 0f);
        {
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
            ValueMarkerView mv = new ValueMarkerView(context, coinMarketCapBean, custom);
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
        ValueFormatter yLineValueFormatter;
        YAxis yAxisLeft;
        {
            yLineValueFormatter = new YLineValueFormatter(true);
            // Y-Axis Style
            yAxisLeft = chart.getAxisLeft();
            yAxisLeft.setLabelCount(5, true);
            yAxisLeft.setDrawAxisLine(false);
            // 如果当前没有数据返回，那么就是用默认的
            yAxisLeft.setValueFormatter(yLineValueFormatter);
            // horizontal grid lines
//            yAxisLeft.enableGridDashedLine(10f, 10f, 1f);
            yAxisLeft.setAxisLineColor(context.getResources().getColor(R.color.black20_AEAEAE));
            yAxisLeft.setTextColor(context.getResources().getColor(R.color.green_005744));
            yAxisLeft.setTextSize(8);
        }
        YAxis yAxisRight;
        {
            yLineValueFormatter = new YLineValueFormatter(false);
            // Y-Axis Style
            yAxisRight = chart.getAxisRight();
            yAxisRight.setDrawAxisLine(false);
            yAxisRight.setLabelCount(5, true);
            // disable dual axis (only use LEFT axis)
            yAxisRight.setValueFormatter(yLineValueFormatter);
            // horizontal grid lines
//            yAxisRight.enableGridDashedLine(10f, 10f, 1f);
            yAxisRight.setAxisLineColor(context.getResources().getColor(R.color.black20_AEAEAE));

            yAxisRight.setTextColor(context.getResources().getColor(R.color.yellow_FFA73B));
//            yAxisRight.setAxisLineColor(context.getResources().getColor(R.color.black20_AEAEAE));
            yAxisRight.setTextSize(8);
        }
        // draw points over time
        chart.animateX(1500);
        chart.setScaleYEnabled(false);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
////        l.setTextColor(context.getResources().getColor(R.color.green_22ac22));
//        // draw legend entries as lines
//        l.setForm(Legend.LegendForm.EMPTY);
        l.setEnabled(false);

        if (data == null) {
            data = new LineData();
        }
        //添加USD
        data.addDataSet(initLineData(getValuesEntry(true, priceUSD), labelUSD));
        //添加BTC
        data.addDataSet(initLineData(getValuesEntry(true, priceBTC), labelBTC));

        // set data
        chart.setData(data);

    }

    /**
     * 设置曲线名称
     *
     * @param curveName
     */
    public void setCurveName(String curveName) {
        if (tvCurveName != null) {
            tvCurveName.setText(curveName + "  " + context.getResources().getString(R.string.price_curve));
        }
        this.coinName = StringTool.getCoinNameFromCurrencyList(curveName);
        requestInfo(this.coinName);
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
        //1:判断当前chart是否为空，否则进入
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            //2：取到想要的label的dataSet
            setLineDataSet = (LineDataSet) chart.getData().getDataSetByLabel(label, true);
            //3:判断当前传入的label是否是USD
            if (StringTool.equals(label, labelUSD)) {
                //4:判断当前根据label取出的dataSet是否为空，如果为空，那么就初始化然后赋值显示
                if (setLineDataSet == null) {
                    initLineData(values, label);
                } else {
                    //4：否则，根据传入的值进行重新显示刷新
                    setLineDataSet.setValues(values);
                    setLineDataSet.notifyDataSetChanged();
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                }
            } else {
                if (setLineDataSet == null) {
                    initLineData(values, label);
                } else {
                    setLineDataSet.setValues(values);
                    setLineDataSet.notifyDataSetChanged();
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                }
            }

        } else {
            initLineData(values, label);
        }

    }

    /**
     * 初始化chart数据
     *
     * @param values
     * @param label
     */
    private LineDataSet initLineData(List<Entry> values, String label) {
        // create a dataSet and give it a type
        LineDataSet setLineDataSet = new LineDataSet(values, label);
        setLineDataSet.setDrawIcons(false);
        // draw dashed line
//            setLineDataSet.enableDashedLine(10f, 5f, 0f);

        // black lines and points
        if (StringTool.equals(label, labelUSD)) {
            setLineDataSet.setColor(context.getResources().getColor(R.color.green_005744));
            setLineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        } else {
            setLineDataSet.setColor(context.getResources().getColor(R.color.yellow_FFA73B));
            setLineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
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
        // create a data object with the data sets
        return setLineDataSet;
    }

    private void initListener() {
        rlChart.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Disposable subscribeTvExpend = RxView.clicks(tvExpend).throttleFirst(Constants.Time.sleep1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //重新请求数据
                        requestInfo(coinName);
                        ExpendChart(true);
                    }
                });

        Disposable subscribeTvExtract = RxView.clicks(tvExtract).throttleFirst(Constants.Time.sleep1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //隐藏chart，显示动作视图
                        ExpendChart(false);
                    }
                });
        cbBtc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkData(labelBTC, getValuesEntry(isChecked, priceBTC));
                chart.getAxisRight().setEnabled(isChecked);
            }
        });
        cbUsd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkData(labelUSD, getValuesEntry(isChecked, priceUSD));
                chart.getAxisLeft().setEnabled(isChecked);

            }
        });
    }

    public void ExpendChart(boolean isShow) {
        //隐藏chart，显示动作视图
        llCoinChart.setVisibility(isShow ? VISIBLE : GONE);
        rlCoinChartAction.setVisibility(isShow ? GONE : VISIBLE);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void getCoinMarketCapSuccess(CoinMarketCapBean coinMarketCapBean) {
        if (coinMarketCapBean == null) {
            return;
        }
        boolean isRefresh = this.coinMarketCapBean == null;
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

        // 如果当前有数据，那么就不更新界面
        if (isRefresh) {
            initLineChart();
        }
        if (rlLoadingData != null) {
            rlLoadingData.setVisibility(GONE);
        }


    }

    @Override
    public void getCoinMarketCapFailure(String info) {
        LogTool.e(TAG, info);
        if (rlLoadingData != null
                && tvNoData != null
                && pbLoading != null) {
            rlLoadingData.setVisibility(VISIBLE);
            tvNoData.setText(StringTool.isEmpty(info) ? context.getString(R.string.no_curve_info) : context.getResources().getString(R.string.get_data_failure));
            pbLoading.setVisibility(GONE);
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void noNetWork() {

    }

    @Override
    public boolean httpExceptionDisposed(ResponseJson responseJson) {
        if (responseJson == null) {
            return false;
        }
        int code = responseJson.getCode();
        //判断是否是Token过期，弹出提示重新登录，然后跳转界面
        if (code == MessageConstants.CODE_2019
                || code == MessageConstants.CODE_2016
                || code == MessageConstants.CODE_2018) {
            //    {"success":false,"code":2019,"message":"AccessToken expire."}
            OttoTool.getInstance().post(new LogoutEvent());
            return true;
        } else if (code == MessageConstants.CODE_2005) {
            LogoutEvent logoutEvent = new LogoutEvent();
            logoutEvent.setInfo(context.getString(R.string.please_register_email_first));
            OttoTool.getInstance().post(logoutEvent);
            return true;
        }
        return false;
    }

    @Override
    public void noData() {
        LogTool.e(TAG, context.getResources().getString(R.string.no_data));
    }
}
