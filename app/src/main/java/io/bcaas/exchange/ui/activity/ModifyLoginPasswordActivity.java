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
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 「修改登录密码」
 */
public class ModifyLoginPasswordActivity extends BaseActivity {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_original_password)
    EditTextWithAction etwaOriginalPassword;
    @BindView(R.id.etwa_new_password)
    EditTextWithAction etwaNewPassword;
    @BindView(R.id.etwa_confirm_new_password)
    EditTextWithAction etwaConfirmNewPassword;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @Override
    public int getContentView() {
        return R.layout.activity_modify_login_password;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.modify_login_password);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        RxView.clicks(ibBack).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
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
        RxView.clicks(btnSure).throttleFirst(Constants.time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(Object o) {
                        //1：判断原登录密码非空
                        String originalPassword = etwaOriginalPassword.getContent();
                        if (StringTool.isEmpty(originalPassword)) {
                            showToast("请输入原密码！");
                            return;
                        }
                        //2：判断新密码非空
                        String newPassword = etwaNewPassword.getContent();
                        if (StringTool.isEmpty(newPassword)) {
                            showToast("请输入新密码！");
                            return;
                        }
                        //3：判断确认新密码非空
                        String confirmNewPassword = etwaConfirmNewPassword.getContent();
                        if (StringTool.isEmpty(confirmNewPassword)) {
                            showToast("请输入确认新密码！");
                            return;
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
    public void onBackPressed() {
        super.onBackPressed();
        setResult(false);
    }
}
