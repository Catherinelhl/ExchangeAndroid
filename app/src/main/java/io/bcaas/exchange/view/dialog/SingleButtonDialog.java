package io.bcaas.exchange.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.tools.StringTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/27
 * <p>
 *  自定義Dialog：单个按钮提示框
 */
public class SingleButtonDialog extends Dialog {

    TextView tvTitle;
    TextView tvContent;
    Button btnSure;
    private ConfirmClickListener confirmClickListener;

    public SingleButtonDialog(Context context) {
        this(context, 0);
    }


    public SingleButtonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_bcaas_single_dialog, null);
        setContentView(view);
        btnSure = view.findViewById(R.id.btn_sure);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        initListener();
    }

    public SingleButtonDialog setLeftText(String left) {
        if (StringTool.isEmpty(left)) return this;
        btnSure.setText(left);
        return this;

    }

    public SingleButtonDialog setContent(String content) {
        if (StringTool.isEmpty(content)) return this;
        tvContent.setText(content);
        return this;

    }

    public SingleButtonDialog setTitle(String title) {
        if (StringTool.isEmpty(title)) return this;
        tvTitle.setText(title);
        return this;

    }

    public void initListener() {
        btnSure.setOnClickListener(v -> confirmClickListener.sure());
    }


    public SingleButtonDialog setOnConfirmClickListener(ConfirmClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
        return this;
    }

    public interface ConfirmClickListener {
        void sure();
    }
}
