package io.bcaas.exchange.tools.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import io.bcaas.exchange.tools.time.DateFormatTool;

import java.text.DecimalFormat;
import java.util.List;

/**
 * X轴label数据定制
 */
public class XLineValueFormatter extends ValueFormatter {
    private String TAG = XLineValueFormatter.class.getSimpleName();
    private final DecimalFormat mFormat;
    private List<List<Double>> allLine;


    public XLineValueFormatter(List<List<Double>> allLine) {
        mFormat = new DecimalFormat("##0.0");
        this.allLine = allLine;

    }


    @Override
    public String getFormattedValue(float value) {
        if (allLine == null || allLine.size() == 0) {
            return mFormat.format(value);
        } else {
            return DateFormatTool.getUTCDateForAMPMFormat(allLine.get((int) value).get(0));
        }
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (allLine == null || allLine.size() == 0) {
            return mFormat.format(value);
        } else {
            int x = (int) value;
            if (x < allLine.size()) {
                return DateFormatTool.getUTCDateForAMPMFormat(allLine.get(x).get(0));
            } else {
                return mFormat.format(value);
            }
        }
    }
}
