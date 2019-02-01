package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.BuyContract;
import io.bcaas.exchange.ui.presenter.BuyPresenterImp;
import io.bcaas.exchange.view.dialog.SingleButtonDialog;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.view.textview.AppendStringLayout;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.MemberVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * Activity：购买详情
 */
public class BuyDetailActivity extends BaseActivity implements BuyContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_purple_title)
    TextView tvPurpleTitle;
    @BindView(R.id.tv_person_name)
    TextView tvPersonName;
    @BindView(R.id.tv_pay_method)
    TextView tvPayMethod;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_total_account)
    TextView tvTotalAccount;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.etwa_fund_password)
    EditTextWithAction etFundPassword;
    @BindView(R.id.etwa_random_verify_code)
    EditTextWithAction etRandomVerifyCode;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.tv_salable_balance)
    TextView tvSalableBalance;
    @BindView(R.id.asp_fund)
    AppendStringLayout aspFund;
    @BindView(R.id.asp_google)
    AppendStringLayout aspGoogle;
    @BindView(R.id.ll_buy_detail)
    LinearLayout llBuyDetail;
    @BindView(R.id.sv_buy_detail)
    ScrollView svBuyDetail;
    private MemberOrderVO memberOrderVO;
    private BuyContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_buy_detail;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        memberOrderVO = (MemberOrderVO) bundle.getSerializable(Constants.KeyMaps.BUY_DETAIL);
    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.buy_detail);
        tvPayMethod.setText(R.string.pay_method_str);
        if (memberOrderVO != null) {
            //得到出售人的名字
            MemberVO memberVO = memberOrderVO.getMemberVO();
            if (memberVO != null) {
                tvPersonName.setText(memberVO.getMemberId());
            }
            // 得到需要支付的信息
            CurrencyListVO paymentCurrencyList = memberOrderVO.getPaymentCurrencyUid();
            if (paymentCurrencyList != null) {
                String enName = paymentCurrencyList.getEnName();
                tvPayMethod.setText(getString(R.string.pay_method_str) + "  " + enName);
                tvFee.setText(paymentCurrencyList.getBuyCharge() + "  " + enName);
                tvPrice.setText(memberOrderVO.getUnitPrice() + "  " + enName);
                tvTotalAccount.setText(memberOrderVO.getPrice() + "  " + enName);
                //得到当前账户信息
                List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
                if (ListTool.noEmpty(memberKeyVOList)) {
                    for (MemberKeyVO memberKeyVO : memberKeyVOList) {
                        CurrencyListVO currencyListVOSelf = memberKeyVO.getCurrencyListVO();
                        if (currencyListVOSelf != null) {
                            //比较当前的uid，然后返回余额
                            String currencyUid = currencyListVOSelf.getCurrencyUid();
                            if (StringTool.equals(currencyUid, paymentCurrencyList.getCurrencyUid())) {
                                tvSalableBalance.setText(context.getResources().getString(R.string.available_balance) + "  " + StringTool.getDisplayAmountByUId(memberKeyVO.getBalanceAvailable(), currencyUid) + "  " + currencyListVOSelf.getEnName());
                                break;
                            }
                        }
                    }
                }

            }
            // 得到当前币种信息
            CurrencyListVO currencyListVO = memberOrderVO.getCurrencyListVO();
            if (currencyListVO != null) {
                String enName = currencyListVO.getEnName();
                tvPurpleTitle.setText(getString(R.string.buy) + " " + enName);
                tvNumber.setText(memberOrderVO.getAmount() + "  " + enName);
            }
        }
        aspFund.setOnItemSelectListener(onItemSelectListener, Constants.ActionFrom.FUND_PASSWORD);
        aspGoogle.setOnItemSelectListener(onItemSelectListener, Constants.ActionFrom.GOOGLE_VERIFY);

    }

    @Override
    public void initData() {
        presenter = new BuyPresenterImp(this);
    }

    @Override
    public void initListener() {
        hideSoftKeyBoardByTouchView(llBuyDetail);
        hideSoftKeyBoardByTouchView(svBuyDetail);
        RxView.clicks(ibBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        setResult(true);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btnBuy).throttleFirst(Constants.Time.sleep10000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断当前资金密码是否输入
                        String txPassword = etFundPassword.getContent();
                        if (StringTool.isEmpty(txPassword)) {
                            showToast(getString(R.string.please_input_fund_password));
                            return;
                        }
                        //2：判断当前google验证码是否输入
                        String verifyCode = etRandomVerifyCode.getContent();
                        if (StringTool.isEmpty(verifyCode)) {
                            showToast(getString(R.string.please_input_google_verify_code));
                            return;
                        }

                        //3：判断当前是否设置资金密码
                        MemberVO memberVO = BaseApplication.getMemberVO();
                        // 如果当前有账户信息，那么本地替用户进行密码设置的判断
                        if (memberVO != null) {
                            //判断是否设置「资金密码」
                            String txPasswordAttribute = memberVO.getTxPassword();
                            if (StringTool.equals(txPasswordAttribute, Constants.Status.NO_TX_PASSWORD)) {
                                showToast(getString(R.string.no_fund_password_please_set_first));
                                return;
                            }
                            //4：判断当前是否设置google验证码
                            int googleVerifyAttribute = memberVO.getTwoFactorAuthVerify();
                            if (googleVerifyAttribute == Constants.Status.UN_BOUND) {
                                showToast(getString(R.string.no_google_verify_please_set_first));
                                return;
                            }
                        }
                        if (memberOrderVO != null) {
                            //5：接口请求数据
                            presenter.buy(txPassword, memberOrderVO.getMemberOrderUid(), verifyCode);
                            //在接口没有回来之前，不允许重复点击，将按钮设置为不可点击，待结果回来之后再点击
                            btnBuy.setEnabled(false);
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

    @Override
    public void buyFailure(String info) {
        //设置按钮可点击
        btnBuy.setEnabled(true);
        showSingleDialog(info, new SingleButtonDialog.ConfirmClickListener() {
            @Override
            public void sure() {
            }
        });
    }

    @Override
    public void invalidBuyOrder(String info) {
        //设置按钮可点击
        btnBuy.setEnabled(true);
        //无效的订单
        showSingleDialog(info, new SingleButtonDialog.ConfirmClickListener() {
            @Override
            public void sure() {
            }
        });
    }

    @Override
    public void buySelfError() {
        //设置按钮可点击
        btnBuy.setEnabled(true);
        // 弹框提示用户购买成功
        showSingleDialog(getString(R.string.not_to_buy_self), new SingleButtonDialog.ConfirmClickListener() {
            @Override
            public void sure() {
            }
        });
    }

    @Override
    public void buySuccess(String info) {
        //设置按钮可点击
        btnBuy.setEnabled(true);
        // 弹框提示用户购买成功
        showSingleDialog(getString(R.string.congratulations_to_buy_success), new SingleButtonDialog.ConfirmClickListener() {
            @Override
            public void sure() {
                setResult(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 如果当前是从设置google以及验证资金密码返回，应该更新当前用户的安全验证问题
                case Constants.RequestCode.GOOGLE_VERIFY:
                case Constants.RequestCode.FUND_PASSWORD:
                    presenter.getAccountSecurity();
                    break;
            }
        }
    }

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {

    }

    @Override
    public void getAccountSecurityFailure(String info) {

    }
}
