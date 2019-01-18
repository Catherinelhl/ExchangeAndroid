package io.bcaas.exchange.tools.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.CoinMarketCapBean;
import io.bcaas.exchange.listener.ChartMarkerViewListener;
import io.bcaas.exchange.tools.time.DateFormatTool;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Catherine
 * <p>
 * 显示Bcaas自定义的线图信息
 */
@SuppressLint("ViewConstructor")
public class ValueMarkerView extends MarkerView {

    private final TextView tvContent;
    private final ValueFormatter valueFormatter;

    private final DecimalFormat format;

    private ChartMarkerViewListener chartMarkerViewListener;
    private CoinMarketCapBean coinMarketCapBean;

    public ValueMarkerView(Context context, CoinMarketCapBean coinMarketCapBean, ValueFormatter valueFormatter) {
        super(context, R.layout.custom_marker_view);
        this.coinMarketCapBean = coinMarketCapBean;
        this.valueFormatter = valueFormatter;
        tvContent = findViewById(R.id.tvContent);
        format = new DecimalFormat("###.0");
    }

    public void setChartMarkerViewListener(ChartMarkerViewListener chartMarkerViewListener) {
        this.chartMarkerViewListener = chartMarkerViewListener;
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (chartMarkerViewListener != null) {
            this.chartMarkerViewListener.getIndex((int) e.getX());
        }
        float index = e.getX();
        if (this.coinMarketCapBean == null) {
            tvContent.setText(valueFormatter.getFormattedValue(index) + "\n" + format.format(e.getY()));
        } else {
            int position = (int) index;
            List<List<Double>> market_cap_by_available_supply = coinMarketCapBean.getMarket_cap_by_available_supply();
            if (market_cap_by_available_supply == null) {
                tvContent.setText(valueFormatter.getFormattedValue(index) + "\n" + format.format(e.getY()));

            } else {
                Object object = market_cap_by_available_supply.get(position).get(0);
                if (object instanceof Double) {
                    double time = (Double) object;
                    tvContent.setText(DateFormatTool.getUTCDateForChart(time)
                            + "\nMarketValue:" + market_cap_by_available_supply.get((int) index).get(1)
                            + "\nBTC Price:" + coinMarketCapBean.getPrice_btc().get(position).get(1)
                            + "\nUSD Price:$" + coinMarketCapBean.getPrice_usd().get(position).get(1)
                            + "\nVolume:" + coinMarketCapBean.getVolume_usd().get(position).get(1));
                }


            }
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-200, -getHeight());
    }
}
