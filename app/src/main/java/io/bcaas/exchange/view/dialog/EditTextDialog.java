package io.bcaas.exchange.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.tools.StringTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/27
 * <p>
 * 自定義Dialog：带输入框雙按鈕提示框
 */
public class EditTextDialog extends Dialog {

    private TextView tvTitle;
    private TextView tvContent;
    private Button btnLeft;
    private Button btnRight;
    private Context context;
    private EditText etName;
    private ConfirmClickListener confirmClickListener;

    public EditTextDialog(Context context) {
        this(context, 0);
    }


    public EditTextDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_edittext_dialog, null);
        setContentView(view);
        btnLeft = view.findViewById(R.id.btn_left);
        btnRight = view.findViewById(R.id.btn_right);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        etName = view.findViewById(R.id.et_identity_name);
        initListener();
    }

    public EditTextDialog setLeftText(String left) {
        if (StringTool.isEmpty(left)) return this;
        btnLeft.setText(left);
        return this;

    }

    public EditTextDialog setRightText(String right) {
        if (StringTool.isEmpty(right)) return this;
        btnRight.setText(right);
        return this;

    }

    public EditTextDialog setContent(String content) {
        if (StringTool.isEmpty(content)) {
            //隐藏当前的文本
            tvContent.setVisibility(View.GONE);
            return this;
        }
        tvContent.setText(content);
        return this;

    }

    public EditTextDialog setTitle(String title) {
        if (StringTool.isEmpty(title)) return this;
        tvTitle.setText(title);
        return this;

    }

    public void initListener() {
        btnLeft.setOnClickListener(v -> confirmClickListener.cancel());
        btnRight.setOnClickListener(v -> {
            String name = etName.getText().toString();
            confirmClickListener.sure(name);
        });
    }

    public EditTextDialog setOnConfirmClickListener(ConfirmClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
        return this;
    }

    public interface ConfirmClickListener {
        void sure(String name);

        void cancel();
    }
}
