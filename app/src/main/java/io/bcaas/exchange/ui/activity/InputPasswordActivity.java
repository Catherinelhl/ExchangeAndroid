package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
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
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.PayWayManagerContract;
import io.bcaas.exchange.ui.presenter.PaymentManagerPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.view.textview.AppendStringLayout;
import io.bcaas.exchange.vo.MemberPayInfoVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/3/25 15:31
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.activity
+--------------+---------------------------------
+ description  +  输入资金密码页面
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public class InputPasswordActivity extends BaseActivity
        implements PayWayManagerContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_input_fund_password)
    EditTextWithAction etwaInputFundPassword;
    @BindView(R.id.asp_fund)
    AppendStringLayout aspFund;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private MemberPayInfoVO memberPayInfoVO;
    private PayWayManagerContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_input_password;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        memberPayInfoVO = (MemberPayInfoVO) bundle.getSerializable(Constants.From.MEMBER_PAY_INFO);
    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.fund_password));

    }

    @Override
    public void initData() {
        presenter = new PaymentManagerPresenterImp(this);

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
        RxView.clicks(btnSure).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //step 1:判断当前资金密码输入
                        String fundPassword = etwaInputFundPassword.getContent();
                        if (StringTool.isEmpty(fundPassword)) {
                            showToast(getString(R.string.please_input_fund_password));
                            return;
                        }
                        //step 2:判断当前数据是否完整
                        if (memberPayInfoVO == null) {
                            showToast(getString(R.string.data_format_exception));
                            return;
                        }
                        //step 3：请求添加支付方式
                        presenter.addPayWay(Constants.Payment.ADD_PAY_WAY, memberPayInfoVO, fundPassword);


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        aspFund.setOnItemSelectListener(onItemSelectListener, Constants.ActionFrom.FUND_PASSWORD);
    }

    @Override
    public <T> void responseSuccess(T message, String type) {
        if (StringTool.equals(type, Constants.Payment.ADD_PAY_WAY)) {
            showToast(getString(R.string.set_payment_way_success));
            setResult(false);
        }
    }

    @Override
    public void responseFailed(String message, String type) {

    }
}
