package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.UserInfoBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「提现」页面视图
 */
public class WithDrawView extends LinearLayout {
    private TextView tvCashableBalance;
    private EditTextWithAction etwaReceiveAddress;
    private EditTextWithAction etwaWithdrawAmount;
    private TextView tvFeeTips;
    private EditTextWithAction etwaRemarks;
    private TextView tvInfo;
    private Button btnSend;
    private LinearLayout llWithDrawContent;
    private TextView tvNoFundPasswordTips;
    private TextView tvSetImmediately;
    private LinearLayout llNoFundPassword;

    private Context context;
    private OnItemSelectListener onItemSelectListener;

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public WithDrawView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public WithDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_with_draw, this, true);
        tvCashableBalance = view.findViewById(R.id.tv_cashable_balance);
        etwaReceiveAddress = view.findViewById(R.id.etwa_receive_address);
        etwaWithdrawAmount = view.findViewById(R.id.etwa_withdraw_amount);
        tvInfo = view.findViewById(R.id.tv_info);
        tvFeeTips = view.findViewById(R.id.tv_fee_tips);
        etwaRemarks = view.findViewById(R.id.etwa_remarks);
        btnSend = view.findViewById(R.id.btn_send);
        llNoFundPassword = view.findViewById(R.id.ll_no_fund_password);
        tvSetImmediately = view.findViewById(R.id.tv_set_immediately);
        tvSetImmediately = view.findViewById(R.id.tv_set_immediately);
        llWithDrawContent = view.findViewById(R.id.ll_with_draw_content);
        etwaReceiveAddress.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
                //跳转扫描二维码
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(null, from);
                }
            }
        }, Constants.EditTextFrom.WITHDRAW_SCAN);
        initListener();

    }

    private void initListener() {
        RxView.clicks(btnSend).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(tvSetImmediately).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(MessageConstants.EMPTY, MessageConstants.EMPTY);
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

    /**
     * 更新当前界面信息
     *
     * @param userInfoBean
     */
    public void refreshData(UserInfoBean userInfoBean) {
        //判断当前是否设置资金密码
        boolean hasFundPassword = BaseApplication.isSetFundPassword();
        if (llNoFundPassword != null) {
            llNoFundPassword.setVisibility(hasFundPassword ? GONE : VISIBLE);
        }

        if (llWithDrawContent != null) {
            llWithDrawContent.setVisibility(hasFundPassword ? VISIBLE : GONE);
        }
        if (hasFundPassword) {
            if (tvCashableBalance != null) {
                tvCashableBalance.setText(context.getResources().getString(R.string.cash_able_balance) + "231.213415  BTC");
            }
            if (etwaWithdrawAmount != null) {
                etwaWithdrawAmount.setRightText(context.getString(R.string.all_in));
                etwaWithdrawAmount.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
            }
        }

    }

    /**
     * 将当前扫描的值返回
     *
     * @param scanInfo
     */
    public void setScanInfo(String scanInfo) {
        if (etwaReceiveAddress != null) {
            etwaReceiveAddress.setContent(scanInfo);
        }
    }
}
