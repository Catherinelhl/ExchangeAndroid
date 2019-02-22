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
import io.bcaas.exchange.event.LogoutEvent;
import io.bcaas.exchange.listener.AmountEditTextFilter;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.listener.OnTextChangeListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.otto.OttoTool;
import io.bcaas.exchange.tools.time.IntervalTimerTool;
import io.bcaas.exchange.ui.contracts.VerifyCodeContract;
import io.bcaas.exchange.ui.presenter.VerifyCodePresenterImp;
import io.bcaas.exchange.view.viewGroup.ImageViewWithLoading;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/17
 * 自定义视图：一个带有动作的EditView，可能是「Check」，可能是「Send」 and so on
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
    @BindView(R.id.iwl)
    ImageViewWithLoading imageViewWithLoading;


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
    //监听当前文本的变化
    private OnTextChangeListener onTextChangeListener;
    //当前默认的textSize
    private int defaultTextSize = 16;

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

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
            float textSize = typedArray.getFloat(R.styleable.editViewWithAction_textSize, defaultTextSize);
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
                    imageViewWithLoading.setVisibility(GONE);
                    break;
                case 1://密码输入框，需要显示小眼睛
                    cbCheck.setVisibility(VISIBLE);
                    llAction.setVisibility(GONE);
                    imageViewWithLoading.setVisibility(GONE);
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
                    imageViewWithLoading.setVisibility(GONE);
                    etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case 3://图片验证码，需要显示图片信息
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(GONE);
                    imageViewWithLoading.setVisibility(VISIBLE);
                    presenter.getImageVerifyCode();
                    //重新设定图片的大小
                    imageViewWithLoading.setImageMeasure(context.getResources().getDimensionPixelOffset(R.dimen.d115),
                            context.getResources().getDimensionPixelOffset(R.dimen.d32));
                    break;
                case 4://显示扫描
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(GONE);
                    imageViewWithLoading.setVisibility(VISIBLE);
                    imageViewWithLoading.setSrc(R.mipmap.icon_scan);
                    imageViewWithLoading.setImageMeasure(context.getResources().getDimensionPixelOffset(R.dimen.d21),
                            context.getResources().getDimensionPixelOffset(R.dimen.d21));
                    break;
                case 5://手机号码
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(GONE);
                    imageViewWithLoading.setVisibility(GONE);
                    etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case 6://google号码
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(GONE);
                    imageViewWithLoading.setVisibility(GONE);
                    etContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case 7://amount 输入
                    cbCheck.setVisibility(GONE);
                    llAction.setVisibility(VISIBLE);
                    imageViewWithLoading.setVisibility(GONE);
                    etContent.setFilters(new InputFilter[]{new AmountEditTextFilter().setDigits(8)});
                    break;
            }

            setEditHintTextSize(hint);

        }
        initView();
        initListener();
    }


    /**
     * 设置输入框的hint的大小而不影响text size
     */
    private void setEditHintTextSize(String hint) {
        if (StringTool.isEmpty(hint)) {
            return;
        }
        SpannableString spannableString = new SpannableString(hint);//定义hint的值
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(14, true);//设置字体大小 true表示单位是sp
        spannableString.setSpan(absoluteSizeSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (etContent != null) {
            etContent.setHint(new SpannedString(spannableString));
        }
        if (etPassword != null) {
            etPassword.setHint(new SpannedString(spannableString));
        }
    }

    private void initView() {
        if (tvAction == null) {
            return;
        }
        //如果当前有Token的话，那么字体颜色就是蓝色，否则是红色
        if (StringTool.isEmpty(BaseApplication.getToken())) {
            tvAction.setTextColor(context.getResources().getColor(R.color.button_color));
        } else {
            tvAction.setTextColor(context.getResources().getColor(R.color.blue_5B88FF));

        }
    }

    /**
     * 监听
     */
    private void initListener() {
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
        Disposable disposableTvAction = RxView.clicks(tvAction).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        //判断当前是否是「发送」字样，如果是，那么就可以进行点击；如果是在倒计时就不能点击
                        String tvActionString = tvAction.getText().toString();
                        if (StringTool.notEmpty(from)) {
                            if (StringTool.equals(tvActionString, getResources().getString(R.string.send))) {
                                switch (from) {
                                    case Constants.EditTextFrom.EMAIL_CODE://邮箱
                                        requestEmail();
                                        break;
                                    case Constants.EditTextFrom.REGISTER_VERIFY_EMAIL:
                                    case Constants.EditTextFrom.FORGET_VERIFY_EMAIL:
                                        //如果当前输入邮箱的验证密码，那么需要验证当前密码是否已经注册过了，如果没有，才开始倒计时请求验证码
                                        break;
                                    default:
                                        //开始倒计时
                                        startCountDownInterval();
                                        break;
                                }

                            } else if (StringTool.equals(tvActionString, getResources().getString(R.string.all_in))) {
                                switch (from) {
                                    case Constants.EditTextFrom.WITHDRAW_AMOUNT:
                                        //提现界面的「全部发送」
                                        break;
                                }
                            }
                        }
                        //返回回调
                        if (editTextWatcherListener != null) {
                            editTextWatcherListener.onAction(from);
                        }
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
                        if (onTextChangeListener != null) {
                            onTextChangeListener.onTextChange(content);
                        }
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

        RxView.clicks(imageViewWithLoading).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Object o) {
                        //判断当前的图片类型：扫描、验证码
                        switch (behaviour) {
                            case 3:
                                //清空当前的输入框
                                if (etContent != null) {
                                    etContent.setText(MessageConstants.EMPTY);
                                }
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
     * 开始倒计时
     * 开始60s倒计时
     */
    private void startCountDownInterval() {
        IntervalTimerTool.countDownTimer(Constants.Time.sleep60)
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

    /**
     * 重置输入框右边文本信息
     */
    public void resetRightText(){
        tvAction.setText(getResources().getString(R.string.send));
        disposeRequest(disposableCountDownTimer);
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

    public void setHint(String info) {
        if (etContent != null) {
            etContent.setHint(info);
        }
        if (etPassword != null) {
            etPassword.setHint(info);
        }
        setEditHintTextSize(info);
    }

    public void setEditTextWatcherListener(EditTextWatcherListener editTextWatcherListener) {
        this.editTextWatcherListener = editTextWatcherListener;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 传入当前页面展现数据对应的UID，根据UID来重新设置输入框条件限制
     *
     * @param uid
     */
    public void setUID(String uid) {
        if (StringTool.isEmpty(uid)) {
            return;
        }
        switch (uid) {
            case "0"://BCC
                etContent.setFilters(new InputFilter[]{new AmountEditTextFilter().setDigits(8)});
                break;
            case "1"://BTC
                etContent.setFilters(new InputFilter[]{new AmountEditTextFilter().setDigits(8)});
                break;
            case "2"://ETH
                etContent.setFilters(new InputFilter[]{new AmountEditTextFilter().setDigits(10)});
                break;
        }
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
        if (imageViewWithLoading != null) {
            imageViewWithLoading.setBitmap(bitmap);
        }
    }

    @Override
    public void getImageVerifyCodeSuccess(Bitmap bitmap) {
        if (imageViewWithLoading != null) {
            imageViewWithLoading.setBitmap(bitmap);
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
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        // 停止计时
        tvAction.setText(getResources().getString(R.string.send));
        disposeRequest(disposableCountDownTimer);
    }

    public void setRightText(String info) {
        if (tvAction != null) {
            tvAction.setText(info);
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

    /**
     * 开始Http请求邮箱验证码
     */
    public void requestEmail() {
        //开始请求验证码数据
        if (presenter != null) {
            presenter.emailVerify(BaseApplication.getMemberID(),
                    BaseApplication.getCurrentLanguage(), BaseApplication.getMemberID());
        }
        //开始倒计时
        startCountDownInterval();

    }

    public void verifyAccount(String memberId) {
        if (presenter != null) {
            presenter.verifyAccount(memberId, from);
        }
    }

    @Override
    public void verifyAccountFailure(String info) {
        if (context != null) {
            Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void verifyAccountSuccess(String info) {
        // 开始请求验证码
        requestEmail();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void noNetWork() {
        Toast.makeText(context, getResources().getString(R.string.network_not_reachable), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean httpExceptionDisposed(ResponseJson responseJson) {
        if (responseJson == null) {
            return false;
        }
        int code = responseJson.getCode();
        //判断是否是Token过期，弹出提示重新登录，然后跳转界面
        if (code == MessageConstants.CODE_2019
                || code == MessageConstants.CODE_2016
                || code == MessageConstants.CODE_2018) {
            //    {"success":false,"code":2019,"message":"AccessToken expire."}
            OttoTool.getInstance().post(new LogoutEvent());
            return true;
        } else if (code == MessageConstants.CODE_2005) {
            LogoutEvent logoutEvent = new LogoutEvent();
            logoutEvent.setInfo(context.getString(R.string.please_register_email_first));
            OttoTool.getInstance().post(logoutEvent);
            return true;
        }
        return false;
    }

    @Override
    public void noData() {
        LogTool.d(TAG, context.getResources().getString(R.string.no_data));
    }
}
