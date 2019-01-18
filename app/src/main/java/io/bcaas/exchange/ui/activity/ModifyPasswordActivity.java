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
import io.bcaas.exchange.ui.contracts.ResetPasswordContract;
import io.bcaas.exchange.ui.presenter.ResetPasswordPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 「修改密码」 根据传入的字段来决定修改登录密码还是修改资金密码
 */
public class ModifyPasswordActivity extends BaseActivity implements ResetPasswordContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_original_password)
    EditTextWithAction etOriginalPassword;
    @BindView(R.id.etwa_new_password)
    EditTextWithAction etNewPassword;
    @BindView(R.id.etwa_confirm_new_password)
    EditTextWithAction etConfirmNewPassword;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private ResetPasswordContract.Presenter presenter;

    /*标志当前界面是修改登录密码，还是修改资金密码*/
    private boolean isModifyLoginPassword;

    @Override
    public int getContentView() {
        return R.layout.activity_modify_login_password;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        String from = bundle.getString(Constants.KeyMaps.From);
        isModifyLoginPassword = StringTool.equals(from, Constants.From.LOGIN_PASSWORD);
    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        if (isModifyLoginPassword) {
            tvTitle.setText(R.string.modify_login_password);
        } else {
            tvTitle.setText(R.string.modify_fund_password);
        }
    }

    @Override
    public void initData() {
        presenter = new ResetPasswordPresenterImp(this);
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
        RxView.clicks(btnSure).throttleFirst(Constants.Time.sleep1000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断原登录密码非空
                        String originalPassword = etOriginalPassword.getContent();
                        if (StringTool.isEmpty(originalPassword)) {
                            showToast(getString(R.string.please_input_original_password));
                            return;
                        }
                        //2：判断新密码非空
                        String newPassword = etNewPassword.getContent();
                        if (StringTool.isEmpty(newPassword)) {
                            showToast(getString(R.string.please_input_new_password));
                            return;
                        }

                        //3:判断当前新密码是否输入8位
                        if (newPassword.length() < Constants.ValueMaps.PASSWORD_MIN_LENGTH) {
                            showToast(getString(R.string.password_to_short));
                            return;
                        }
                        //4：判断确认新密码非空
                        String confirmNewPassword = etConfirmNewPassword.getContent();
                        if (StringTool.isEmpty(confirmNewPassword)) {
                            showToast(getString(R.string.please_input_confirm_password));
                            return;
                        }

                        //5:判断新密码输入是否一致
                        if (!StringTool.equals(newPassword, confirmNewPassword)) {
                            showToast(getString(R.string.password_does_not_match));
                            return;
                        }
                        //6：判断当前修改密码类型：资金还是登录
                        if (isModifyLoginPassword) {
                            if (presenter != null) {
                                presenter.resetPassword(originalPassword, newPassword);
                            }
                        } else {

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
    public void resetPasswordFailure(String info) {
        showToast(info);
    }

    @Override
    public void resetPasswordSuccess(String info) {
        setResult(false);
    }
}
