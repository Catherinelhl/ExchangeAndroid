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
import io.bcaas.exchange.ui.contracts.FundPasswordContract;
import io.bcaas.exchange.ui.presenter.FundPasswordPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 「设置资金密码」
 */
public class SetFundPasswordActivity extends BaseActivity implements FundPasswordContract.View {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_fund_password)
    EditTextWithAction etwaFundPassword;
    @BindView(R.id.etwa_confirm_fund_password)
    EditTextWithAction etwaConfirmFundPassword;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private FundPasswordContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_set_fund_password;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.set_fund_password);
    }

    @Override
    public void initData() {
        presenter = new FundPasswordPresenterImp(this);
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

                        //1：判断密码非空
                        String password = etwaFundPassword.getContent();
                        if (StringTool.isEmpty(password)) {
                            showToast("请输入资金密码！");
                            return;
                        }
                        //2：判断确认新密码非空
                        String confirmPassword = etwaConfirmFundPassword.getContent();
                        if (StringTool.isEmpty(confirmPassword)) {
                            showToast("请输入确认密码！");
                            return;
                        }

                        presenter.setFundPassword(password);

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
    public void onBackPressed() {
        super.onBackPressed();
        setResult(false);
    }

    @Override
    public void setFundPasswordSuccess() {
        setResult(false);
    }

    @Override
    public void setFundPasswordFailure() {

    }
}
