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
import io.bcaas.exchange.ui.contracts.WithDrawContract;
import io.bcaas.exchange.ui.presenter.WithDrawPresenterImp;
import io.bcaas.exchange.view.editview.EditTextWithAction;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/4
 * 「提现详情，输入密码」
 */
public class WithDrawDetailActivity extends BaseActivity implements WithDrawContract.View {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.etwa_fund_password)
    EditTextWithAction etwaFundPassword;
    @BindView(R.id.tv_email_key)
    TextView tvEmailKey;
    @BindView(R.id.tv_email_value)
    TextView tvEmailValue;
    @BindView(R.id.etwa_email_verify_code)
    EditTextWithAction etwaEmailVerifyCode;
    @BindView(R.id.tv_phone_key)
    TextView tvPhoneKey;
    @BindView(R.id.tv_phone_value)
    TextView tvPhoneValue;
    @BindView(R.id.etwa_message_verify_code)
    EditTextWithAction etwaMessageVerifyCode;
    @BindView(R.id.tv_google_verify_key)
    TextView tvGoogleVerifyKey;
    @BindView(R.id.etwa_google_verify_code)
    EditTextWithAction etwaGoogleVerifyCode;
    @BindView(R.id.tv_start_immediate)
    TextView tvStartImmediate;
    @BindView(R.id.btn_sure)
    Button btnSure;

    private String amount, mark, address, currencyUid;

    private WithDrawContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_withdraw_detail;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.with_draw);
        ibBack.setVisibility(View.VISIBLE);

        etwaMessageVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
        etwaEmailVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));
        etwaGoogleVerifyCode.setRightTextColor(context.getResources().getColor(R.color.blue_5B88FF));

    }

    @Override
    public void initData() {
        presenter = new WithDrawPresenterImp(this);
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
        Disposable subscribeSure = RxView.clicks(btnSure).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //1:判断当前是否输入了资金密码
                        String txPassword = etwaFundPassword.getContent();
                        if (StringTool.isEmpty(txPassword)) {
                            showToast("请输入资金密码！");
                            return;
                        }
                        //2：判断当前是否输入了邮箱验证码
                        String emailVerifyCode = etwaEmailVerifyCode.getContent();
                        if (StringTool.isEmpty(emailVerifyCode)) {
                            showToast("请输入邮箱验证码！");
                            return;
                        }
                        //3:判断当前是否输入手机验证码
                        String phoneVerifyCode = etwaMessageVerifyCode.getContent();
                        if (StringTool.isEmpty(phoneVerifyCode)) {
                            showToast("请输入手机验证码！");
                            return;
                        }
                        //4：判断当前是否输入google验证码
                        String googleVerifyCode = etwaGoogleVerifyCode.getContent();
                        if (StringTool.isEmpty(googleVerifyCode)) {
                            showToast("请输入Google验证码！");
                            return;
                        }
                        MemberOrderVO memberOrderVO = new MemberOrderVO();
                        memberOrderVO.setAmount(amount);
                        memberOrderVO.setMark(mark);
                        //3：请求接口提现
                        presenter.withDraw(txPassword, memberOrderVO, address, currencyUid);
                    }
                });
    }

    @Override
    public void withDrawFailure(String info) {
        showToast(info);
    }

    @Override
    public void withDrawSuccess(String info) {
        setResult(false);
    }
}
