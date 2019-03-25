package io.bcaas.exchange.view.viewGroup;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/25 17:26
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.view.viewGroup
+--------------+---------------------------------
+ description  +   自定义充值数量
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.AmountEditTextFilter;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.listener.RadioButtonCheckListener;
import io.bcaas.exchange.tools.StringTool;

public class CustomRechargeAmount extends LinearLayout {

    @BindView(R.id.rb_custom)
    RadioButton rbCustom;
    @BindView(R.id.et_amount)
    EditText etAmount;
    private View view;
    private Context context;


    private EditTextWatcherListener editTextWatcherListener;
    private RadioButtonCheckListener radioButtonCheckListener;

    public CustomRechargeAmount(Context context) {
        this(context, null);
    }

    public CustomRechargeAmount(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.item_custom_recharge_amount, this, true);
        ButterKnife.bind(view);

        initListener();
    }

    public void setEditTextWatcherListener(EditTextWatcherListener editTextWatcherListener) {
        this.editTextWatcherListener = editTextWatcherListener;
    }

    public void setRadioButtonCheckListener(RadioButtonCheckListener radioButtonCheckListener) {
        this.radioButtonCheckListener = radioButtonCheckListener;
    }

    private void initListener() {
        etAmount.setFilters(new InputFilter[]{new AmountEditTextFilter().setDigits(Constants.DigitalPrecision.LIMIT_EIGHT)});
        rbCustom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioButtonCheckListener != null) {
                    radioButtonCheckListener.onChange(rbCustom.isChecked());
                }
            }
        });

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    String content = s.toString();
                    if (StringTool.notEmpty(content)) {
                        if (editTextWatcherListener != null) {
                            editTextWatcherListener.onComplete(content);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    /**
     * 获取当前输入的自定义的充值数量
     *
     * @return
     */
    public String getContent() {
        if (etAmount != null) {
            return etAmount.getText().toString();
        }
        return MessageConstants.EMPTY;
    }

    /**
     * 重置当前的数量信心
     */
    public void resetContent(boolean isClear) {
        if (etAmount != null && isClear) {
            etAmount.setText(MessageConstants.EMPTY);
            rbCustom.setChecked(false);
        }
    }


}
