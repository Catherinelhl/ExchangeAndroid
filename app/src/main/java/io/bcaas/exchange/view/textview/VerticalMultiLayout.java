package io.bcaas.exchange.view.textview;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/28 14:43
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.view.textview
+--------------+---------------------------------
+ description  +  
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * 用于购买页面显示文本
 */
public class VerticalMultiLayout extends LinearLayout {
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    private Context context;
    private OnItemSelectListener onItemSelectListener;
    private String from;

    public VerticalMultiLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_vertical_multi, this, true);
        ButterKnife.bind(view);
        this.context = context;

        initListener();
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener, String from) {
        this.onItemSelectListener = onItemSelectListener;
        this.from = from;
    }

    public void setContent(String info) {
        if (tvContent != null) {
            tvContent.setText(info);
        }
    }

    /**
     * 设置单位
     *
     * @param info
     * @param unit
     */
    public void setUnitText(String info, String unit) {
        if (tvUnit != null) {
            tvUnit.setText(info + "(" + unit + ")");
        }
    }

    private void initListener() {
        Disposable subscribe = RxView.clicks(tvUnit).throttleFirst(Constants.Time.sleep1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(null, from);
                        }
                    }
                });
    }
}
