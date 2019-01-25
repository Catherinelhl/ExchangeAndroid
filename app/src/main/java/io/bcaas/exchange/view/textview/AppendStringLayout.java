package io.bcaas.exchange.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
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
import io.bcaas.exchange.tools.StringTool;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/22
 * 拼接字符的layout
 */
public class AppendStringLayout extends LinearLayout {
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_start_immediate)
    TextView tvStartImmediate;
    @BindView(R.id.ll_action)
    LinearLayout llAction;
    private Context context;
    private OnItemSelectListener onItemSelectListener;
    private String from;

    public AppendStringLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_append_string, this, true);
        ButterKnife.bind(view);
        this.context = context;

        //获取自定义属性的值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.appendStringStyle);
        if (typedArray != null) {
            String content = typedArray.getString(R.styleable.appendStringStyle_content);
            String actionText = typedArray.getString(R.styleable.appendStringStyle_action_text);
            float textSize = typedArray.getFloat(R.styleable.appendStringStyle_text_size, 12);
            int contentColor = typedArray.getInteger(R.styleable.appendStringStyle_content_color, context.getResources().getColor(R.color.grey_666666));
            int actionColor = typedArray.getInteger(R.styleable.appendStringStyle_action_color, context.getResources().getColor(R.color.blue_5B88FF));

            typedArray.recycle();
            if (StringTool.notEmpty(content) && tvContent != null) {
                tvContent.setText(content);
                tvContent.setTextSize(textSize);
                tvContent.setTextColor(contentColor);
            }
            if (StringTool.notEmpty(actionText) && tvStartImmediate != null) {
                tvStartImmediate.setText(actionText);
                tvStartImmediate.setTextSize(textSize);
                tvStartImmediate.setTextColor(actionColor);
            }
        }
        initListener();
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener, String from) {
        this.onItemSelectListener = onItemSelectListener;
        this.from = from;
    }

    public void setContent(String info) {
        this.setContent(info, R.color.grey_666666);
    }

    public void setContent(String info, int color) {
        if (tvContent != null) {
            tvContent.setText(info);
            tvContent.setTextColor(color);
        }
    }

    public void setActionText(String info) {
        this.setActionText(info, R.color.blue_5B88FF);
    }

    public void setActionText(String info, int color) {
        if (tvStartImmediate != null) {
            tvStartImmediate.setText(info);
            tvStartImmediate.setTextColor(color);
        }
    }

    private void initListener() {
        Disposable subscribe = RxView.clicks(tvStartImmediate).throttleFirst(Constants.Time.sleep1000, TimeUnit.MILLISECONDS)
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
