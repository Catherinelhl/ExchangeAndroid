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
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.SafetyCenterContract;
import io.bcaas.exchange.ui.presenter.SafetyCenterPresenterImp;
import io.bcaas.exchange.view.viewGroup.SafetyCenterItemView;
import io.bcaas.exchange.vo.MemberVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * Activity：「安全中心」
 */
public class SafetyCenterActivity extends BaseActivity implements SafetyCenterContract.View, OnItemSelectListener {
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.btnLogout)
    Button btnLogout;
    @BindView(R.id.sciv_login_password)
    SafetyCenterItemView scivLoginPassword;
    @BindView(R.id.sciv_fund_password)
    SafetyCenterItemView scivFundPassword;
    @BindView(R.id.sciv_email_verify)
    SafetyCenterItemView scivEmailVerify;
    @BindView(R.id.sciv_phone_verify)
    SafetyCenterItemView scivPhoneVerify;
    @BindView(R.id.sciv_google_verify)
    SafetyCenterItemView scivGoogleVerify;

    private SafetyCenterContract.Presenter presenter;

    @Override
    public int getContentView() {
        return R.layout.activity_sefety_center;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.safety_center);
        //判断当前memberVO是否为空，如果为空，那么就显示默认值
        MemberVO memberVO = BaseApplication.getMemberVO();
        //登录密码
        scivLoginPassword.setTabStatusByText(false, getString(R.string.modify));
        scivLoginPassword.setOnItemSelectListener(this);
        if (memberVO == null) {
            //资金密码
            scivFundPassword.setTabStatusByText(false, getString(R.string.setting));
            //邮箱验证
            scivEmailVerify.setTabStatusByText(false, getString(R.string.close));

            //手机验证
            scivPhoneVerify.setTabStatusByText(false, getString(R.string.bind));

            //google验证
            scivGoogleVerify.setTabStatusByText(false, getString(R.string.setting));
        } else {
            getAccountSecuritySuccess(BaseApplication.getMemberVO());
        }

        scivFundPassword.setTabInfo(getString(R.string.fund_password_purpose));
        scivEmailVerify.setTabInfo(BaseApplication.getMemberID());
        scivPhoneVerify.setTabInfo(getString(R.string.phone_verify_purpose));
        scivGoogleVerify.setTabInfo(getString(R.string.google_verify_purpose));

        scivFundPassword.setOnItemSelectListener(this);
        scivEmailVerify.setOnItemSelectListener(this);
        scivPhoneVerify.setOnItemSelectListener(this);
        scivGoogleVerify.setOnItemSelectListener(this);

    }


    @Override
    public void initData() {
        presenter = new SafetyCenterPresenterImp(this);
        //取得帳戶資訊
        presenter.getAccountSecurity();
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
        RxView.clicks(btnLogout).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        presenter.logout(BaseApplication.getMemberID());
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
    public void logoutSuccess(String info) {
        //清空token
        BaseApplication.clearTokenAndMemberId();
        intentToActivity(LoginActivity.class, true);
    }

    @Override
    public <T> void onItemSelect(T type, String from) {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        MemberVO memberVO = BaseApplication.getMemberVO();
        if (memberVO == null) {
            return;
        }

        if (StringTool.equals(from, getString(R.string.login_password))) {
            // 登录密码
            intent.putExtra(Constants.KeyMaps.From, Constants.From.LOGIN_PASSWORD);
            intent.setClass(SafetyCenterActivity.this, ModifyPasswordActivity.class);
            startActivityForResult(intent, Constants.RequestCode.MODIFY_LOGIN_PASSWORD);

        } else if (StringTool.equals(from, getString(R.string.fund_password))) {
            //判断是否设置「资金密码」
            String txPassword = memberVO.getTxPassword();
            if (StringTool.equals(txPassword, Constants.Status.NO_TX_PASSWORD)) {
                intent.setClass(SafetyCenterActivity.this, SetFundPasswordActivity.class);
                startActivityForResult(intent, Constants.RequestCode.FUND_PASSWORD);
            } else {
                intent.putExtra(Constants.KeyMaps.From, Constants.From.FUND_PASSWORD);
                intent.setClass(SafetyCenterActivity.this, ModifyPasswordActivity.class);
                startActivityForResult(intent, Constants.RequestCode.MODIFY_FUND_PASSWORD);

            }
        } else if (StringTool.equals(from, getString(R.string.email_verify))) {
            int emailVerify = memberVO.getEmailVerify();
            if (emailVerify == Constants.Status.CLOSE) {
                //直接调用更改Email的状态
                presenter.securityEmail(MessageConstants.EMPTY);
            } else {
                //邮箱验证，如果当前是需要"绑定"的状态，那么就跳转到「绑定」的状态；如果是已经绑定过的，那么就显示「关闭」，并且跳转到「关闭验证」
                intent.setClass(SafetyCenterActivity.this, CloseVerifyMethodActivity.class);
                bundle.putString(Constants.KeyMaps.From, Constants.VerifyType.EMAIL);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.RequestCode.EMAIL_VERIFY);
            }
        } else if (StringTool.equals(from, getString(R.string.phone_verify))) {
            //手机验证，如果当前是需要"绑定"的状态，那么就跳转到「绑定」的状态；如果是已经绑定过的，那么就显示「关闭」，并且跳转到「关闭验证」
            String phone = memberVO.getPhone();
            if (StringTool.isEmpty(phone)) {
                intent.setClass(SafetyCenterActivity.this, BindPhoneActivity.class);
                startActivityForResult(intent, Constants.RequestCode.PHONE_VERIFY);
            } else {
                int phoneVerify = memberVO.getPhoneVerify();
                if (phoneVerify == Constants.Status.CLOSE) {
                    //直接调用更改phone的状态
                    presenter.securityPhone(memberVO.getPhone(), MessageConstants.EMPTY);
                } else if (phoneVerify == Constants.Status.OPEN) {
                    //跳转到「解绑」的操作
                    intent.setClass(SafetyCenterActivity.this, CloseVerifyMethodActivity.class);
                    bundle.putString(Constants.KeyMaps.From, Constants.VerifyType.PHONE);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Constants.RequestCode.PHONE_VERIFY);
                }
            }


        } else if (StringTool.equals(from, getString(R.string.google_verify))) {
            //判断当前的双因素认证的状态
            int googleVerify = memberVO.getTwoFactorAuthVerify();
            if (googleVerify == Constants.Status.CLOSE) {
                //如果当前Google验证是关闭的状态，那么直接调用更改Google验证的状态即可
                presenter.securityGoogle(MessageConstants.EMPTY);
            } else if (googleVerify == Constants.Status.OPEN) {
                //如果当前Google验证是开启的状态，那么点击需要跳转到关闭验证的页面
                intent.setClass(SafetyCenterActivity.this, CloseVerifyMethodActivity.class);
                bundle.putString(Constants.KeyMaps.From, Constants.VerifyType.GOOGLE);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.RequestCode.GOOGLE_VERIFY);
            } else {
                //如果当前Google验证是未绑定的状态，那么点击跳转到获取google Url绑定的状态
                intentToGoogleVerifyActivity(SafetyCenterActivity.this);

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //刷新当前界面
            presenter.getAccountSecurity();
            switch (requestCode) {
                case Constants.RequestCode.MODIFY_LOGIN_PASSWORD:
                    break;
                case Constants.RequestCode.FUND_PASSWORD:
                    break;
                case Constants.RequestCode.EMAIL_VERIFY:
                    break;
                case Constants.RequestCode.PHONE_VERIFY:
                    break;
                case Constants.RequestCode.GOOGLE_VERIFY:
                    break;
            }
        }
    }

    /**
     * 根据获取到的账户资讯，判断当前的显示情况
     *
     * @param memberVO
     */
    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {
        if (memberVO == null) {
            return;
        }
        String email = memberVO.getMemberId();
        if (StringTool.notEmpty(email) && scivEmailVerify != null) {
            scivEmailVerify.setTabInfo(email);
        }

        String txPassword = memberVO.getTxPassword();
        int emailVerify = memberVO.getEmailVerify();
        int phoneVerify = memberVO.getPhoneVerify();
        int twoFactorAuthVerify = memberVO.getTwoFactorAuthVerify();
        //判断是否设置「资金密码」
        if (StringTool.equals(txPassword, Constants.Status.NO_TX_PASSWORD)) {
            scivFundPassword.setTabStatusByText(false, getString(R.string.setting));
        } else {
            scivFundPassword.setTabStatusByText(true, getString(R.string.modify));
        }

        //判断是否开启「邮箱验证」
        if (emailVerify == Constants.Status.CLOSE) {
            scivEmailVerify.setTabStatusByText(false, getString(R.string.open));
        } else {
            scivEmailVerify.setTabStatusByText(true, getString(R.string.close));
        }
        String phone = memberVO.getPhone();

        if (StringTool.notEmpty(phone)) {
            if (scivPhoneVerify != null) {
                scivPhoneVerify.setTabInfo(phone);
            }
            //判断是否开启「手机验证」
            if (phoneVerify == Constants.Status.CLOSE) {
                scivPhoneVerify.setTabStatusByText(false, getString(R.string.open));
            } else if (phoneVerify == Constants.Status.OPEN) {
                scivPhoneVerify.setTabStatusByText(true, getString(R.string.close));
            }
        } else {
            scivPhoneVerify.setTabStatusByText(false, getString(R.string.bind));
        }
        //判断是否开启「google验证」
        if (twoFactorAuthVerify == Constants.Status.CLOSE) {
            scivGoogleVerify.setTabStatusByText(true, getString(R.string.open));
        } else if (twoFactorAuthVerify == Constants.Status.OPEN) {
            scivGoogleVerify.setTabStatusByText(true, MessageConstants.EMPTY);
        } else {
            scivGoogleVerify.setTabStatusByText(false, getString(R.string.setting));

        }

    }

    @Override
    public void getAccountSecurityFailure(String info) {

    }

    @Override
    public void securityPhoneSuccess(String info) {
        presenter.getAccountSecurity();
    }

    @Override
    public void securityPhoneFailure(String info) {
        //提示"开始失败"
        showToast(info);
    }
}
