package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.ui.contracts.PayWayManagerContract;
import io.bcaas.exchange.ui.presenter.PaymentManagerPresenterImp;
import io.bcaas.exchange.view.dialog.DoubleButtonDialog;
import io.bcaas.exchange.vo.MemberPayInfoVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/20 11:14
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.activity
+--------------+---------------------------------
+ description  +  Activity：支付管理
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public class PaymentManagerActivity extends BaseActivity implements PayWayManagerContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_bank_account)
    TextView tvBankAccount;
    @BindView(R.id.tv_card_holder)
    TextView tvCardHolder;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.ll_bank_info)
    LinearLayout llBankInfo;
//    @BindView(R.id.add_pay_way)
//    Button addPayWay;
//    @BindView(R.id.modify_pay_way)
//    Button modifyPayWay;
//    @BindView(R.id.remove_pay_way)
//    Button removePayWay;
//    @BindView(R.id.get_pay_way)
//    Button getPayWay;
//    @BindView(R.id.get_bank_info)
//    Button getBankInfo;
//    @BindView(R.id.recharge_virtual_coin)
//    Button rechargeVirtualCoin;
//    @BindView(R.id.convertCoin)
//    Button convertCoin;

    private PayWayManagerContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_pay_way_manager;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.payment_management));
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.add_pay_way_method);

    }

    @Override
    public void initData() {
        presenter = new PaymentManagerPresenterImp(this);
        /*获取当前的支付方式*/
        presenter.getPayWay(Constants.Payment.GET_PAY_WAY);

    }

    @Override
    public void initListener() {
        Disposable subscribe = RxView.clicks(ibBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        setResult(true);
                    }
                });
        RxView.clicks(tvRight).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //step 1: 跳转界面进行支付方式的添加
                        Intent intent = new Intent();
                        intent.setClass(PaymentManagerActivity.this, AddPaymentActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.ADD_PAYMENT_CODE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        RxView.clicks(modifyPayWay).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
//                .subscribe(new Observer<Object>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//
//                        MemberPayInfoVO memberPayInfoVO = new MemberPayInfoVO();
//                        memberPayInfoVO.setPayWayUid(0);
//                        memberPayInfoVO.setBankPersonalName("刘红");
//                        memberPayInfoVO.setBankName("中国招商银行");
//                        memberPayInfoVO.setBankBranchName("北京分行建国路支行");
//                        memberPayInfoVO.setBankAccount("6214830105268279");
//                        presenter.modifyPayWay(Constants.Payment.MODIFY_PAY_WAY, memberPayInfoVO);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
        RxView.clicks(tvDelete).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //step 1:进行弹框提示是否确认删除
                        showDoubleButtonDialog(getString(R.string.sure_to_delete_current_pay_way), new DoubleButtonDialog.ConfirmClickListener() {
                            @Override
                            public void sure() {
                                //step 2:确认删除
                                MemberPayInfoVO memberPayInfoVO = new MemberPayInfoVO();
                                memberPayInfoVO.setPayWayUid(Constants.PayWayUid.BANK);
                                presenter.removePayWay(Constants.Payment.REMOVE_PAY_WAY, memberPayInfoVO);
                            }

                            @Override
                            public void cancel() {

                            }
                        });

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
    public void responseFailed(String message, String type) {
        showToast(message);
    }

    @Override
    public <T> void responseSuccess(T message, String type) {
        switch (type) {
            case Constants.Payment.GET_PAY_WAY:
                //抓取到当前到支付信息，然后填充到界面上
                List<MemberPayInfoVO> memberPayInfoVOList = ((List<MemberPayInfoVO>) message);
                //取出当前第一条数据，然后传入下一个界面
                if (ListTool.noEmpty(memberPayInfoVOList)) {
                    llBankInfo.setVisibility(View.VISIBLE);
                    MemberPayInfoVO memberPayInfoVO = memberPayInfoVOList.get(0);
                    if (memberPayInfoVO != null) {
                        tvBankAccount.setText(memberPayInfoVO.getBankAccount());
                        tvCardHolder.setText(memberPayInfoVO.getBankPersonalName());
                        tvBank.setText(memberPayInfoVO.getBankName() + "  " + memberPayInfoVO.getBankBranchName());
                    }
                } else {
                    llBankInfo.setVisibility(View.INVISIBLE);
                }
                break;
            case Constants.Payment.REMOVE_PAY_WAY:
                showToast(getString(R.string.delete_pay_way_success));
                //刷新当前界面
                llBankInfo.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void noData() {
        super.noData();
        llBankInfo.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.ADD_PAYMENT_CODE:
                    boolean isBack = data.getBooleanExtra(Constants.KeyMaps.From, false);
                    if (!isBack) {
                        //刷新当前界面
                        presenter.getPayWay(Constants.Payment.GET_PAY_WAY);
                    }
                    break;
            }
        }
    }

}
