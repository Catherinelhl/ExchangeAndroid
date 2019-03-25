package io.bcaas.exchange.view.viewGroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.StringTool;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 自定义视图：一个显示「添加支付方式」item的View
 */
public class AddPayWayItemView extends LinearLayout {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_middle_info)
    TextView tvMiddleInfo;
    @BindView(R.id.tv_payment_action)
    TextView tvPaymentAction;
    private Context context;
    //标注当前item的类型，区分点击事件
    private String tabType;

    private OnItemSelectListener onItemSelectListener;

    public AddPayWayItemView(Context context) {
        this(context, null);
    }

    public AddPayWayItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_payment, this, true);
        ButterKnife.bind(view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.addPayWayItem);
        String tabText = typedArray.getString(R.styleable.addPayWayItem_addPaymentText);
        boolean enableInput = typedArray.getBoolean(R.styleable.addPayWayItem_enableInput, true);
        boolean showMiddle = typedArray.getBoolean(R.styleable.addPayWayItem_showMiddle, false);
        if (StringTool.notEmpty(tabText)) {
            this.tabType = tabText;
            etName.setHint(tabText);
        }
        tvMiddleInfo.setVisibility(showMiddle ? VISIBLE : GONE);
        if (!enableInput) {
            etName.setEnabled(false);
        }
        initListener();
    }

    private void initListener() {
        RxView.clicks(tvPaymentAction).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
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
        if (tvPaymentAction != null) {
            //判断当前的text是否为空。如果为空，那么就不显示点击动作文本
            if (StringTool.isEmpty(text)) {
                tvPaymentAction.setVisibility(INVISIBLE);
                return;
            }
            tvPaymentAction.setText(text);
        }
    }

    /**
     * 显示最后文本右边的图片
     */
    public void showRightDrawable(boolean isShow) {
        if (tvPaymentAction != null) {
            tvPaymentAction.setVisibility(isShow ? VISIBLE : INVISIBLE);
            tvPaymentAction.setCompoundDrawablePadding(isShow ? context.getResources().getDimensionPixelOffset(R.dimen.d8) : 0);
            tvPaymentAction.setCompoundDrawablesWithIntrinsicBounds(null, null, isShow ? context.getResources().getDrawable(R.mipmap.icon_drop_down) : null, null);

        }
    }

    public void setMiddleInfo(String message) {
        if (StringTool.isEmpty(message)) {
            return;
        }
        if (tvMiddleInfo != null) {
            tvPaymentAction.setVisibility(VISIBLE);
            tvMiddleInfo.setText(message);
        }

    }

    /**
     * 给内容赋值
     *
     * @param message
     */
    public void setContent(String message) {
        if (etName != null && StringTool.notEmpty(message)) {
            etName.setText(message);
        }
    }

    /**
     * 获取当前输入框的内容信息
     */
    public String getContent() {
        if (etName != null) {
            return etName.getText().toString();
        }
        return MessageConstants.EMPTY;
    }

}
