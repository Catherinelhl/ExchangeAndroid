package io.bcaas.exchange.view.viewGroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.StringTool;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 自定义一个显示「安全中心」的View
 */
public class SafetyCenterItemView extends LinearLayout {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_middle_info)
    TextView tvMiddleInfo;
    @BindView(R.id.tv_safety_center_action)
    TextView tvSafetyCenterAction;
    private Context context;
    //标注当前item的类型，区分点击事件
    private String tabType;

    private OnItemSelectListener onItemSelectListener;

    public SafetyCenterItemView(Context context) {
        this(context, null);
    }

    public SafetyCenterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_safety_center, this, true);
        ButterKnife.bind(view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.safetyCenterItem);
        String tabText = typedArray.getString(R.styleable.safetyCenterItem_tabText);
        if (StringTool.notEmpty(tabText)) {
            this.tabType = tabText;
            tvName.setText(tabText);
        }
        initListener();
    }

    private void initListener() {
        RxView.clicks(tvSafetyCenterAction).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(null, tabType);
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
     * 设置当前item的状态，需要做什么；通过文本
     *
     * @param isHave 当前状态是否已经拥有
     * @param text   需要显示的文字
     */
    public void setTabStatusByText(boolean isHave, String text) {
        if (tvSafetyCenterAction != null) {
            tvSafetyCenterAction.setText(text);
        }
    }

    /**
     * 设置条目的含义
     *
     * @param info
     */
    public void setTabInfo(String info) {
        if (tvMiddleInfo != null) {
            tvMiddleInfo.setText(info);
        }
    }

}
