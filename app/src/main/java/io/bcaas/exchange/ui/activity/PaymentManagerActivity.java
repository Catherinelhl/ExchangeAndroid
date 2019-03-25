package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.ui.contracts.PayWayManagerContract;
import io.bcaas.exchange.ui.presenter.PaymentManagerPresenterImp;
import io.bcaas.exchange.vo.MemberPayInfoVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
    @BindView(R.id.add_pay_way)
    Button addPayWay;
    @BindView(R.id.modify_pay_way)
    Button modifyPayWay;
    @BindView(R.id.remove_pay_way)
    Button removePayWay;
    @BindView(R.id.get_pay_way)
    Button getPayWay;
    @BindView(R.id.get_bank_info)
    Button getBankInfo;
    @BindView(R.id.recharge_virtual_coin)
    Button rechargeVirtualCoin;
    @BindView(R.id.convertCoin)
    Button convertCoin;

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
        presenter = new PaymentManagerPresenterImp(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        RxView.clicks(addPayWay).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        MemberPayInfoVO memberPayInfoVO = new MemberPayInfoVO();
                        memberPayInfoVO.setPayWayUid(0);
                        memberPayInfoVO.setBankPersonalName("刘红玲");
                        memberPayInfoVO.setBankName("中国招商银行");
                        memberPayInfoVO.setBankBranchName("北京分行建国路支行");
                        memberPayInfoVO.setBankAccount("6214830105268279");
                        presenter.addPayWay(Constants.Payment.ADD_PAY_WAY, memberPayInfoVO, "aaaaaaaa1");

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(modifyPayWay).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                        MemberPayInfoVO memberPayInfoVO = new MemberPayInfoVO();
                        memberPayInfoVO.setPayWayUid(0);
                        memberPayInfoVO.setBankPersonalName("刘红");
                        memberPayInfoVO.setBankName("中国招商银行");
                        memberPayInfoVO.setBankBranchName("北京分行建国路支行");
                        memberPayInfoVO.setBankAccount("6214830105268279");
                        presenter.modifyPayWay(Constants.Payment.MODIFY_PAY_WAY, memberPayInfoVO);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(removePayWay).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        MemberPayInfoVO memberPayInfoVO = new MemberPayInfoVO();
                        memberPayInfoVO.setPayWayUid(0);
                        presenter.removePayWay(Constants.Payment.REMOVE_PAY_WAY, memberPayInfoVO);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(getPayWay).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        presenter.getPayWay(Constants.Payment.GET_PAY_WAY);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(getBankInfo).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        presenter.getBankInfo(Constants.Payment.GET_BANK_INFO);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(rechargeVirtualCoin).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        presenter.rechargeVirtualCoin(Constants.Payment.RECHARGE_VIRTUAL_COIN, Constants.CURRENCY_TYPE_SCS, "1000", "123456","123456");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        RxView.clicks(convertCoin).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        presenter.convertCoin(Constants.Payment.CONVERT_COIN, Constants.CURRENCY_TYPE_SCS, "1000", "aaaaaaaa1");
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

    }

    @Override
    public <T> void responseSuccess(T message, String type) {

    }
}
