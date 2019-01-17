package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.AmountEditTextFilter;
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
public class WithDrawView extends BaseLinearLayout implements GetCurrencyChargeContract.View {
    private String TAG = WithDrawView.class.getSimpleName();

    @BindView(R.id.tv_cashable_balance)
    TextView tvCashAbleBalance;
    @BindView(R.id.etwa_receive_address)
    EditTextWithAction etReceiveAddress;
    @BindView(R.id.etwa_withdraw_amount)
    EditTextWithAction etWithdrawAmount;
    @BindView(R.id.tv_fee_tips)
    TextView tvFeeTips;
    @BindView(R.id.etwa_remarks)
    EditTextWithAction etRemarks;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.ll_with_draw_content)
    LinearLayout llWithDrawContent;
    @BindView(R.id.tv_no_fund_password_tips)
    TextView tvNoFundPasswordTips;
    @BindView(R.id.tv_set_immediately)
    TextView tvSetImmediately;
    @BindView(R.id.ll_no_fund_password)
    LinearLayout llNoFundPassword;
    private String info = "请勿将ETH/ZBA发送至您的比特币(BTC)地址,否则资金将会遗失。比特币的交易需要六个区块的确认,可能会花费1个小时以上才能完成。";

    private OnItemSelectListener onItemSelectListener;
    private String fee;
    private CurrencyListVO currencyListVO;
    private MemberKeyVO memberKeyVO;
    //得到可提现余额
    private String balanceAvailable;

    private GetCurrencyChargeContract.Presenter presenter;


    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public WithDrawView(Context context) {
        super(context);
        presenter = new GetCurrencyChargePresenterImp(this);
    }

    @Override
    protected int setContentView() {
        return R.layout.view_with_draw;
    }

    @Override
    protected void initView() {
        tvNoFundPasswordTips.setText(info);
        etReceiveAddress.setFrom(Constants.EditTextFrom.WITHDRAW_SCAN);
        etWithdrawAmount.setFrom(Constants.EditTextFrom.WITHDRAW_AMOUNT);

    }

    @Override
    protected void initListener() {
        etReceiveAddress.setEditTextWatcherListener(new EditTextWatcherListener() {
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
        });

        etWithdrawAmount.setEditTextWatcherListener(new EditTextWatcherListener() {
            @Override
            public void onComplete(String content) {

            }

            @Override
            public void onAction(String from) {
                if (StringTool.notEmpty(balanceAvailable)) {
                    etWithdrawAmount.setContent(balanceAvailable);

                }
            }
        });
        RxView.clicks(btnSend).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断当前地址是否输入
                        String address = etReceiveAddress.getContent();
                        if (StringTool.isEmpty(address)) {
                            Toast.makeText(context, R.string.please_input_receive_address, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //2：判断当前想要提现的金额是否输入
                        String withDrawAmount = etWithdrawAmount.getContent();
                        if (StringTool.isEmpty(withDrawAmount)) {
                            Toast.makeText(context, R.string.please_input_amount_withdraw, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MemberOrderVO memberOrderVO = new MemberOrderVO();
                        memberOrderVO.setPrice(withDrawAmount);
                        memberOrderVO.setMark(etRemarks.getContent());
                        RequestJson requestJson = new RequestJson();
                        requestJson.setMemberOrderVO(memberOrderVO);
                        MemberKeyVO memberKeyVOTemp = new MemberKeyVO();
                        memberKeyVOTemp.setAddress(address);
                        requestJson.setMemberKeyVO(memberKeyVOTemp);
                        CurrencyListVO currencyListVO = new CurrencyListVO();
                        currencyListVO.setCurrencyUid(memberKeyVO.getCurrencyListVO().getCurrencyUid());
                        requestJson.setCurrencyListVO(currencyListVO);
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(requestJson, Constants.From.WITHDRAW_SURE);
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
                        LogTool.e(TAG, e.getMessage());

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
        tvNoFundPasswordTips.setText(info);
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
                if (tvCashAbleBalance != null) {
                    balanceAvailable = memberKeyVO.getBalanceAvailable();
                    tvCashAbleBalance.setText(context.getResources().getString(R.string.cash_able_balance) + balanceAvailable + "  " + currencyListVO.getEnName());
                }
                if (etWithdrawAmount != null) {
                    etWithdrawAmount.setRightText(context.getString(R.string.all_in));
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
        if (etReceiveAddress != null) {
            etReceiveAddress.setContent(scanInfo);
        }
    }

    @Override
    public void getCurrencyChargeFailure(String info) {
        LogTool.e(TAG, info);
    }

    @Override
    public void getCurrencyChargeSuccess(CurrencyListVO currencyListVO) {
        if (currencyListVO != null) {
            fee = currencyListVO.getWithdrawCharge();
            if (tvFeeTips != null) {
                tvFeeTips.setText(context.getResources().getString(R.string.withdraw_need_fee) + "  " + fee + currencyListVO.getEnName());
            }
        }

    }

}
