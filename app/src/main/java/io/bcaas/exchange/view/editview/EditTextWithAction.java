package io.bcaas.exchange.view.editview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.text.*;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.time.IntervalTimerTool;
import io.bcaas.exchange.ui.contracts.VerifyCodeContract;
import io.bcaas.exchange.ui.presenter.VerifyCodePresenterImp;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/17
 * 自定义一个带有动作的EditView，可能是「Check」，可能是「Send」 and so on
 */
public class EditTextWithAction extends LinearLayout
        implements VerifyCodeContract.View {
    private String TAG = EditTextWithAction.class.getSimpleName();

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_password)
    EditText etPassword;
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
    @BindView(R.id.img)
    ImageView imageView;


    private Context context;
    private VerifyCodeContract.Presenter presenter;

    /*監聽當前密碼的輸入*/
    private EditTextWatcherListener editTextWatcherListener;
    /*标识当前EditText来自于什么功能*/
    private String from;
    /* 标记当前输入框的行为性质*/
    private int behaviour;

    //用于倒计时的订阅
    private Disposable disposableCountDownTimer;

    public EditTextWithAction(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_edittext_with_action, this, true);
        ButterKnife.bind(view);
        this.context = context;
        presenter = new VerifyCodePresenterImp(this);

        //获取自定义属性的值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.editViewWithAction);
        if (typedArray != null) {
            /*声明需要显示的标题以及hint*/
            String hint = typedArray.getString(R.styleable.editViewWithAction_hint);
            /*声明内容的字体大小*/
            float textSize = typedArray.getFloat(R.styleable.editViewWithAction_textSize, 16);
            boolean showLine = typedArray.getBoolean(R.styleable.editViewWithAction_showLine, true);
            behaviour = typedArray.getInt(R.styleable.editViewWithAction_behaviour, 0);
            int textColor = typedArray.getInteger(R.styleable.editViewWithAction_textColor, context.getResources().getColor(R.color.black_1d2124));
            int hintColor = typedArray.getInteger(R.styleable.editViewWithAction_hintColor, context.getResources().getColor(R.color.black30_1d2124));


            typedArray.recycle();
            if (StringTool.notEmpty(hint)) {
                if (etContent.getVisibility() == VISIBLE) {
                    etContent.setHint(hint);

                }
                if (etPassword.getVisibility() == VISIBLE) {
                    etPassword.setHint(hint);
                }
            }
            vPasswordLine.setVisibility(showLine ? VISIBLE : INVISIBLE);
            etContent.setTextColor(textColor);
            etContent.setTextSize(textSize);
            etContent.setHintTextColor(hintColor);
            switch (behaviour) {
                case 0://默认样式，什么也不显示
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(GONE);
                    imageView.setVisibility(GONE);
                    break;
                case 1://密码输入框，需要显示小眼睛
                    cbCheck.setVisibility(VISIBLE);
                    llAction.setVisibility(GONE);
                    imageView.setVisibility(GONE);
                    //显示密码框，隐藏文本框
                    etPassword.setVisibility(VISIBLE);
                    etContent.setVisibility(GONE);
                    etPassword.setTextColor(textColor);
                    etPassword.setTextSize(textSize);
                    etPassword.setHintTextColor(hintColor);
                    //最大输入长度
                    etPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                    break;
                case 2://文字验证码，需要显示发送
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(VISIBLE);
                    imageView.setVisibility(GONE);
                    break;
                case 3://图片验证码，需要显示图片信息
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(GONE);
                    imageView.setVisibility(VISIBLE);
                    presenter.getImageVerifyCode();
                    //重新设定图片的大小
                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.width = context.getResources().getDimensionPixelOffset(R.dimen.d115);
                    layoutParams.height = context.getResources().getDimensionPixelOffset(R.dimen.d32);
                    imageView.setLayoutParams(layoutParams);
                    break;
                case 4://显示扫描
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(GONE);
                    imageView.setVisibility(VISIBLE);
                    imageView.setImageResource(R.mipmap.icon_scan);
                    ViewGroup.LayoutParams layoutParamScan = imageView.getLayoutParams();
                    layoutParamScan.width = context.getResources().getDimensionPixelOffset(R.dimen.d21);
                    layoutParamScan.height = context.getResources().getDimensionPixelOffset(R.dimen.d21);
                    imageView.setLayoutParams(layoutParamScan);
                    break;
                case 5://手机号码
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(GONE);
                    imageView.setVisibility(GONE);
                    etContent.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
                    break;
            }

            if (StringTool.notEmpty(hint)) {
                setEditHintTextSize(hint);
            }

        }
        initView();
    }


    /**
     * 设置输入框的hint的大小而不影响text size
     */
    private void setEditHintTextSize(String hint) {
        SpannableString spannableString = new SpannableString(hint);//定义hint的值
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
        spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etContent.setHint(new SpannedString(spannableString));
        etPassword.setHint(new SpannedString(spannableString));
    }

    private void initView() {
        cbCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String text = etPassword.getText().toString();
            if (StringTool.isEmpty(text)) {
                return;
            }
            etPassword.setInputType(isChecked ?
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                    InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//设置当前私钥显示不可见
            etPassword.setSelection(text.length());

        });
        RxView.clicks(tvAction).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        switch (from) {
                            case Constants.EditTextFrom.PHONE:
                                if (editTextWatcherListener != null) {
                                    editTextWatcherListener.onAction(from);
                                }
                                break;
                            default:
                                //判断当前是否是「发送」字样，如果是，那么就可以进行点击；如果是在倒计时就不能点击
                                String tvActionString = tvAction.getText().toString();
                                if (StringTool.equals(tvActionString, getResources().getString(R.string.send))) {
                                    //开始请求验证码数据
                                    if (presenter != null) {
                                        presenter.emailVerify(Constants.User.MEMBER_ID, BaseApplication.getCurrentLanguage(), Constants.User.MEMBER_ID);
                                    }
                                    IntervalTimerTool.countDownTimer(60)
                                            .subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .doOnSubscribe(new Consumer<Disposable>() {
                                                @Override
                                                public void accept(Disposable disposable) throws Exception {
//                                            LogTool.d(TAG, "计时开始");
                                                }
                                            }).subscribe(new Observer<Integer>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            disposableCountDownTimer = d;

                                        }

                                        @Override
                                        public void onNext(Integer integer) {
                                            tvAction.setText(integer + " s");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
//                                    LogTool.e(TAG, e.getMessage());
                                            tvAction.setText(getResources().getString(R.string.send));
                                            disposeRequest(disposableCountDownTimer);
                                        }

                                        @Override
                                        public void onComplete() {
                                            LogTool.d(TAG, "计时完成");
                                            tvAction.setText(getResources().getString(R.string.send));
                                            disposeRequest(disposableCountDownTimer);
                                        }
                                    });
                                }
                                break;
                        }


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
                    String content = s.toString();
                    if (StringTool.notEmpty(content)) {
                        etContent.setSelection(content.length());

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null) {
                    String content = s.toString();
                    if (StringTool.notEmpty(content)) {
                        if (content.length() >= Constants.ValueMaps.PASSWORD_MIN_LENGTH) {
                            if (editTextWatcherListener != null) {
                                editTextWatcherListener.onComplete(content);
                            }
                        }

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RxView.clicks(imageView).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object o) {
                        //判断当前的图片类型：扫描、验证码
                        switch (behaviour) {
                            case 3:
                                //当前是验证码，点击重新请求验证码
                                presenter.getImageVerifyCode();
                                break;
                            case 4:
                                //如果当前是扫描，那么点击跳转扫描
                                if (editTextWatcherListener != null) {
                                    editTextWatcherListener.onAction(from);
                                }
                                break;
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

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
        if (etContent.getVisibility() == VISIBLE) {
            return etContent.getText().toString();

        } else if (etPassword.getVisibility() == VISIBLE) {
            return etPassword.getText().toString();

        }
        return MessageConstants.EMPTY;
    }

    public void setContent(String content) {
        if (etContent == null) {
            return;
        }
        if (etContent.getVisibility() == VISIBLE) {
            etContent.setText(content);

        }

        if (etPassword.getVisibility() == VISIBLE) {
            etPassword.setText(content);
        }

    }

    public void setEditTextWatcherListener(EditTextWatcherListener editTextWatcherListener, String from) {
        this.editTextWatcherListener = editTextWatcherListener;
        this.from = from;
    }

    private void disposeRequest(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        disposeRequest(disposableCountDownTimer);
    }

    /**
     * 设置输入框的输入类型
     *
     * @param type
     */
    public void setInputType(int type) {
        if (etContent != null) {
            etContent.setInputType(type);
        }
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getImageVerifyCodeSuccess(Bitmap bitmap) {
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getImageVerifyCodeFailure(String info) {
        LogTool.e(TAG, info);

    }

    @Override
    public void getEmailVerifySuccess(String info) {
        LogTool.d(TAG, info);

    }

    @Override
    public void getEmailVerifyFailure(String info) {
        LogTool.e(TAG, info);

    }

    @Override
    public void bindPhoneSuccess(String info) {
        LogTool.d(TAG, info);

    }

    @Override
    public void bindPhoneFailure(String info) {
        LogTool.e(TAG, info);

    }

    public void setRightText(String info) {
        if (tvAction != null) {
            tvAction.setText(info);
        }

    }

    public void setRightTextColor(int color) {
        if (tvAction != null) {
            tvAction.setTextColor(color);
        }
    }

    /**
     * 拿去图片验证码
     */
    public void requestImageVerifyCode() {
        if (presenter != null) {
            presenter.getImageVerifyCode();
        }
    }
}
