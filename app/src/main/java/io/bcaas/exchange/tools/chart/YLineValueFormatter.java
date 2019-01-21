package io.bcaas.exchange.tools.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Y轴Label定制
 */
public class YLineValueFormatter extends ValueFormatter {
    private String TAG = YLineValueFormatter.class.getSimpleName();

    //根据是否是左边来决定显示什么样的单位
    private boolean isLeft;

    public YLineValueFormatter(boolean isLeft) {
        this.isLeft = isLeft;
    }


    @Override
    public String getFormattedValue(float value) {
//        if (value > 1000 && value < 1000000) {
//            return Math.round(value / 1000) + "kil";
//
//        } else if (value >= 1000000 && value < 1000000000) {
//            return Math.round(value / 1000000) + "mli";
//
//        } else if (value >= 1000000000 && value < 1000000000000f) {
//            return Math.round(value / 1000000000) + "bli";
//
//        } else if (value >= 1000000000000f && value < 1000000000000000f) {
//            return Math.round(value / 1000000000000f) + "tri";
//
//        }else {
        if (isLeft) {
            return "$" + "  " + String.valueOf(value);
        } else {
            return String.valueOf(value) + "  " + "BTC";
        }
//        }
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
//        if (value > 1000 && value < 1000000) {
//            return Math.round(value / 1000) + "kil";
//
//        } else if (value >= 1000000 && value < 1000000000) {
//            return Math.round(value / 1000000) + "mli";
//
//        } else if (value >= 1000000000 && value < 1000000000000f) {
//            return Math.round(value / 1000000000) + "bli";
//
//        } else if (value >= 1000000000000f && value < 1000000000000000f) {
//            return Math.round(value / 1000000000000f) + "tri";
//
//        }else {
        if (isLeft) {
            return "$" + String.valueOf(value);

        } else {
            return String.valueOf(value) + "BTC";

        }
//        }
    }
}
