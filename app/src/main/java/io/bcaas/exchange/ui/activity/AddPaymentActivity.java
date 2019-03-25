package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.view.viewGroup.AddPayWayItemView;
import io.bcaas.exchange.vo.MemberPayInfoVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/25 09:50
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.activity
+--------------+---------------------------------
+ description  +  添加支付方式
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public class AddPaymentActivity extends BaseActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.apwv_payment)
    AddPayWayItemView apwvPayment;
    @BindView(R.id.apwv_bank_person_name)
    AddPayWayItemView apwvBankPersonName;
    @BindView(R.id.apwv_bank_name)
    AddPayWayItemView apwvBankName;
    @BindView(R.id.apwv_bank_branch_name)
    AddPayWayItemView apwvBankBranchName;
    @BindView(R.id.apwv_bank_account)
    AddPayWayItemView apwvBankAccount;
    @BindView(R.id.btn_sure)
    Button btnSure;


    @Override
    public int getContentView() {
        return R.layout.activity_add_payment;
    }

    @Override
    public void getArgs(Bundle bundle) {


    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.add_payment_way);
        apwvPayment.setMiddleInfo(getString(R.string.please_choose_a_payment));
        apwvPayment.showRightDrawable(true);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
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
        RxView.clicks(tvTitle).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        if (multipleClickToDo(2)) {
                            apwvBankPersonName.setContent("刘红玲");
                            apwvBankName.setContent("中国招商银行");
                            apwvBankBranchName.setContent("北京分行建国路支行");
                            apwvBankAccount.setContent("6214830105268279");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(btnSure).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //step 1:判断当前所有的必填输入框不能为空，银行（支行）信息为选填项目
                        String paymentMethod = apwvPayment.getContent();
                        if (StringTool.isEmpty(paymentMethod)) {
                            showToast("请先选择支付方式");
                            return;
                        }
                        String bankPersonName = apwvBankPersonName.getContent();
                        if (StringTool.isEmpty(bankPersonName)) {
                            showToast("请先填写姓名");
                            return;
                        }
                        String bankName = apwvBankName.getContent();
                        if (StringTool.isEmpty(bankName)) {
                            showToast("请先填写开户银行");
                            return;
                        }
                        String bankBranchName = apwvBankBranchName.getContent();
                        String bankAccount = apwvBankAccount.getContent();
                        if (StringTool.isEmpty(bankAccount)) {
                            showToast("请先填写银行卡账号");
                            return;
                        }
                        //step 2:将当前内容添加到数据类里面
                        MemberPayInfoVO memberPayInfoVO = new MemberPayInfoVO();
                        memberPayInfoVO.setPayWayUid(Constants.PAY_WAY_UID_BANK);
                        memberPayInfoVO.setBankPersonalName(bankPersonName);
                        memberPayInfoVO.setBankName(bankName);
                        if (StringTool.notEmpty(bankBranchName)) {
                            memberPayInfoVO.setBankBranchName(bankBranchName);
                        }
                        memberPayInfoVO.setBankAccount(bankAccount);
                        //step 3 :跳转界面
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.From.MEMBER_PAY_INFO, memberPayInfoVO);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        intent.setClass(AddPaymentActivity.this, InputPasswordActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.INPUT_PASSWORD_CODE);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        apwvPayment.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public <T> void onItemSelect(T type, String from) {
                apwvPayment.setContent(getString(R.string.paymant_bank));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.INPUT_PASSWORD_CODE:
                    boolean isBack = data.getBooleanExtra(Constants.KeyMaps.From, false);
                    if (!isBack) {
                        setResult(false);
                    }
                    break;
            }
        }
    }
}
