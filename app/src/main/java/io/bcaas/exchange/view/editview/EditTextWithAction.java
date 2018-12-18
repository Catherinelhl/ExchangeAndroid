package io.bcaas.exchange.view.editview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.*;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/17
 * 自定义一个带有动作的EditView，可能是「Check」，可能是「Send」 and so on
 */
public class EditTextWithAction extends LinearLayout {
    private String TAG = EditTextWithAction.class.getSimpleName();

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.cb_check)
    CheckBox cbCheck;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.v_password_line)
    View vPasswordLine;
    @BindView(R.id.ll_password_key)
    LinearLayout llPasswordKey;
    @BindView(R.id.ll_action)
    LinearLayout llAction;


    /*監聽當前密碼的輸入*/
    private EditTextWatcherListener editTextWatcherListener;

    public EditTextWithAction(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_edittext_with_action, this, true);
        ButterKnife.bind(view);
        //获取自定义属性的值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.editViewWithAction);
        if (typedArray != null) {
            /*声明需要显示的标题以及hint*/
            String hint = typedArray.getString(R.styleable.editViewWithAction_hint);
            /*声明内容的字体大小*/
            float textSize = typedArray.getFloat(R.styleable.editViewWithAction_textSize, 16);
            boolean showLine = typedArray.getBoolean(R.styleable.editViewWithAction_showLine, true);
            boolean showCheck = typedArray.getBoolean(R.styleable.editViewWithAction_showCheck, false);
            boolean isPassword = typedArray.getBoolean(R.styleable.editViewWithAction_isPassword, false);
            boolean showRightAction = typedArray.getBoolean(R.styleable.editViewWithAction_showRightAction, false);
            int textColor = typedArray.getInteger(R.styleable.editViewWithAction_textColor, context.getResources().getColor(R.color.black_1d2124));
            int hintColor = typedArray.getInteger(R.styleable.editViewWithAction_hintColor, context.getResources().getColor(R.color.black30_1d2124));


            typedArray.recycle();
            if (StringTool.notEmpty(hint)) {
                etContent.setHint(hint);
            }
            vPasswordLine.setVisibility(showLine ? VISIBLE : INVISIBLE);
            etContent.setTextColor(textColor);
            etContent.setTextSize(textSize);
            etContent.setHintTextColor(hintColor);
            cbCheck.setVisibility(showCheck ? VISIBLE : GONE);
            llAction.setVisibility(showRightAction ? VISIBLE : GONE);
            etContent.setInputType(isPassword ?
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    : InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            setEditHintTextSize(hint);

        }
        initView();
    }


    /*设置输入框的hint的大小而不影响text size*/
    private void setEditHintTextSize(String hint) {
        SpannableString spannableString = new SpannableString(hint);//定义hint的值
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
        spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etContent.setHint(new SpannedString(spannableString));
    }

    private void initView() {
        cbCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String text = etContent.getText().toString();
            if (StringTool.isEmpty(text)) {
                return;
            }
            etContent.setInputType(isChecked ?
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//设置当前私钥显示不可见
            etContent.setSelection(text.length());

        });
        RxView.clicks(tvAction).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        LogTool.d(TAG, "action dot....");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    String password = s.toString();
                    if (StringTool.notEmpty(password)) {
                        if (password.length() >= Constants.ValueMaps.PASSWORD_MIN_LENGTH) {
                            if (editTextWatcherListener != null) {
                                editTextWatcherListener.onComplete(password);

                            }
                        }
                        etContent.setSelection(password.length());

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 设置是否显示Check action
     *
     * @param show
     */
    public void showCheck(boolean show) {
        cbCheck.setVisibility(show ? VISIBLE : GONE);
    }

    /**
     * 设置是否显示文本动作
     *
     * @param show
     */
    public void showTextAction(boolean show) {
        tvAction.setVisibility(show ? VISIBLE : GONE);

    }

    /**
     * 获取内容
     */
    public String getContent() {
        if (etContent == null) {
            return null;
        }
        return etContent.getText().toString();
    }

    public void setEditTextWatcherListener(EditTextWatcherListener editTextWatcherListener) {
        this.editTextWatcherListener = editTextWatcherListener;
    }
}
