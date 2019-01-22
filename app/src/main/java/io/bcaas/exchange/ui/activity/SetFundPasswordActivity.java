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
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.regex.RegexTool;
import io.bcaas.exchange.ui.contracts.SetFundPasswordContract;
import io.bcaas.exchange.ui.presenter.SetFundPasswordPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 「设置资金密码」
 */
public class SetFundPasswordActivity extends BaseActivity implements SetFundPasswordContract.View {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_fund_password)
    EditTextWithAction etFundPassword;
    @BindView(R.id.etwa_confirm_fund_password)
    EditTextWithAction etConfirmFundPassword;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private SetFundPasswordContract.Presenter presenter;

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
        presenter = new SetFundPasswordPresenterImp(this);
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
                        String password = etFundPassword.getContent();
                        if (StringTool.isEmpty(password)) {
                            showToast(getString(R.string.please_input_fund_password));
                            return;
                        }

                        //2：判断密码是否输入8位,是否符合密码输入规则
                        if (!RegexTool.isValidatePassword(password)) {
                            showToast(getResources().getString(R.string.password_rule_of_length));
                            return;
                        }
                        //3：判断确认新密码非空
                        String confirmPassword = etConfirmFundPassword.getContent();
                        if (StringTool.isEmpty(confirmPassword)) {
                            showToast(getString(R.string.please_input_confirm_password));
                            return;
                        }
                        //4：判断密码和确认密码是否一致
                        if (!StringTool.equals(password, confirmPassword)) {
                            showToast(getString(R.string.password_does_not_match));
                            return;
                        }
                        if (presenter != null) {
                            //5:请求接口，上传资金密码
                            presenter.securityTxPassword(password);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(false);
    }

    @Override
    public void securityTxPasswordSuccess(String info) {
        setResult(false);
    }

    @Override
    public void securityTxPasswordFailure(String info) {
        showToast(info);

    }
}
