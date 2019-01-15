package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.EditTextWatcherListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.GetCurrencyChargeContract;
import io.bcaas.exchange.ui.presenter.GetCurrencyChargePresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.RequestJson;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「提现」页面视图
 */
public class WithDrawView extends LinearLayout implements GetCurrencyChargeContract.View {
    private String TAG = WithDrawView.class.getSimpleName();
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
    String info = "请勿将ETH/ZBA发送至您的比特币(BTC)地址,否则资金将会遗失。比特币的交易需要六个区块的确认,可能会花费1个小时以上才能完成。";

    private Context context;
    private OnItemSelectListener onItemSelectListener;
    private String fee;
    private CurrencyListVO currencyListVO;
    private MemberKeyVO memberKeyVO;

    private GetCurrencyChargeContract.Presenter presenter;


    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public WithDrawView(Context context) {
        super(context);
        this.context = context;
        presenter = new GetCurrencyChargePresenterImp(this);
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
        llWithDrawContent = view.findViewById(R.id.ll_with_draw_content);
        tvNoFundPasswordTips = view.findViewById(R.id.tv_no_fund_password_tips);
        tvNoFundPasswordTips.setText(info);
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
        RxView.clicks(btnSend).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断当前地址是否输入
                        String address = etwaReceiveAddress.getContent();
                        if (StringTool.isEmpty(address)) {
                            Toast.makeText(context, "请输入接收地址", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //2：判断当前想要提现的金额是否输入
                        String withDrawAmount = etwaWithdrawAmount.getContent();
                        if (StringTool.isEmpty(withDrawAmount)) {
                            Toast.makeText(context, "请输入 提现金额", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MemberOrderVO memberOrderVO=new MemberOrderVO();
                        memberOrderVO.setAmount(withDrawAmount);
                        memberOrderVO.setMark(etwaRemarks.getContent());
                        RequestJson requestJson=new RequestJson();
                        requestJson.setMemberOrderVO(memberOrderVO);
                        MemberKeyVO memberKeyVOTemp=new MemberKeyVO();
                        memberKeyVOTemp.setAddress(address);
                        requestJson.setMemberKeyVO(memberKeyVOTemp);
                        CurrencyListVO currencyListVO=new CurrencyListVO();
                        currencyListVO.setCurrencyUid(memberKeyVO.getCurrencyListVO().getCurrencyUid());
                        requestJson.setCurrencyListVO(currencyListVO);
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(requestJson, Constants.From.WITHDRAW_SURE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(tvSetImmediately).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
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
     * @param memberKeyVO
     */
    public void refreshData(MemberKeyVO memberKeyVO) {
        if (memberKeyVO != null) {
            this.memberKeyVO = memberKeyVO;
            currencyListVO = memberKeyVO.getCurrencyListVO();
            if (currencyListVO == null) {
                return;
            }
            getCurrencyCharge();
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
                    tvCashableBalance.setText(context.getResources().getString(R.string.cash_able_balance) + memberKeyVO.getBalanceAvailable() + "  " + currencyListVO.getEnName());
                }
                if (etwaWithdrawAmount != null) {
                    etwaWithdrawAmount.setRightText(context.getString(R.string.all_in));
                    etwaWithdrawAmount.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
                }
            }
        }


    }

    /**
     * 取得当前的汇率
     */
    public void getCurrencyCharge() {
        presenter.getCurrencyCharge(currencyListVO.getCurrencyUid());
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

    @Override
    public void getCurrencyChargeFailure(String info) {
        LogTool.e(TAG, info);
    }

    @Override
    public void getCurrencyChargeSuccess(CurrencyListVO currencyListVO) {
        if (currencyListVO != null) {
            fee = currencyListVO.getSellCharge();
            if (tvFeeTips != null) {
                tvFeeTips.setText(context.getResources().getString(R.string.withdraw_need_fee) + "  " + fee + currencyListVO.getEnName());
            }
        }

    }
}
