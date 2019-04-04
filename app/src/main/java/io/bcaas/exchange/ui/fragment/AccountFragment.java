package io.bcaas.exchange.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.SettingsAdapter;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.SettingsBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.activity.*;
import io.bcaas.exchange.ui.contracts.AccountSecurityContract;
import io.bcaas.exchange.ui.presenter.AccountSecurityPresenterImp;
import io.bcaas.exchange.vo.MemberVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * Fragment：「帳戶」
 */
public class AccountFragment extends BaseFragment implements AccountSecurityContract.View {
    private String TAG = AccountFragment.class.getSimpleName();


    @BindView(R.id.iv_account)
    ImageView ivAccount;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.rv_setting)
    RecyclerView rvSetting;
    private SettingsAdapter settingTypesAdapter;

    private AccountSecurityContract.Presenter presenter;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_account;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        presenter = new AccountSecurityPresenterImp(this);
        tvAccountName.setText(BaseApplication.getMemberID());
        initAdapter();
    }

    private void initAdapter() {
        List<SettingsBean> settingTypes = initSettingTypes();//得到设置页面需要显示的所有设置选项
        settingTypesAdapter = new SettingsAdapter(getContext(), settingTypes);
        rvSetting.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvSetting.setAdapter(settingTypesAdapter);
    }

    /**
     * 添加页面数据，实则应该写在presenter里面，但是写在里面在切换语言的时候却不会更新数据
     *
     * @return
     */
    private List<SettingsBean> initSettingTypes() {
        List<SettingsBean> settingTypes = new ArrayList<>();
        SettingsBean settingTypeBean = new SettingsBean(getString(R.string.my_all_fund), Constants.SettingType.MY_ALL_FUND);
        SettingsBean settingTypeBean3 = new SettingsBean(getString(R.string.turn_in), Constants.SettingType.TURN_IN);
        SettingsBean settingTypeBean4 = new SettingsBean(getString(R.string.turn_out), Constants.SettingType.TURN_OUT);
        SettingsBean settingTypeBean5 = new SettingsBean(getString(R.string.recharge), Constants.SettingType.RECHARGE);
        SettingsBean settingTypeBean6 = new SettingsBean(getString(R.string.safety), Constants.SettingType.SAFETY);
        SettingsBean settingTypeBean7 = new SettingsBean(getString(R.string.identity_authentication), Constants.SettingType.IDENTITY_AUTHENTICATION);
        SettingsBean settingTypeBean8 = new SettingsBean(getString(R.string.payment_management), Constants.SettingType.PAYMENT_MANAGEMENT);
        settingTypes.add(settingTypeBean);
        settingTypes.add(settingTypeBean3);
        settingTypes.add(settingTypeBean4);
        settingTypes.add(settingTypeBean5);
        settingTypes.add(settingTypeBean6);
        settingTypes.add(settingTypeBean7);
        settingTypes.add(settingTypeBean8);
        return settingTypes;

    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {
        settingTypesAdapter.setSettingItemSelectListener(new OnItemSelectListener() {
            @Override
            public <T> void onItemSelect(T type, String from) {
                if (type == null) {
                    return;
                }
                Intent intent = new Intent();
                if (type instanceof SettingsBean) {
                    SettingsBean settingTypeBean = (SettingsBean) type;
                    switch (settingTypeBean.getTag()) {
                        case MY_ALL_FUND:
                            intent.setClass(getContext(), MyFundActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.ALL_FUND_CODE);
                            break;
                        case TURN_IN:
                            intent.setClass(getContext(), TurnInActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.TURN_IN);
                            break;
                        case TURN_OUT:
                            intent.setClass(getContext(), TurnOutActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.TURN_OUT);
                            break;
                        case RECHARGE:
                            intent.setClass(getContext(), RechargeActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.RECHARGE);
                            break;
                        case SAFETY:
                            intent.setClass(getContext(), SafetyCenterActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.SAFETY);
                            break;
                        case PAYMENT_MANAGEMENT:
                            intent.setClass(getContext(), PaymentManagerActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.PAYMENT_MANAGEMENT);
                            break;
                        case IDENTITY_AUTHENTICATION:
                            intent.setClass(getContext(), IdentityAuthenticationActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.IDENTITY_AUTHENTICATION);
                            break;

                    }
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    @Override
    protected void cancelSubscribe() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            presenter.getAccountSecurity();
            switch (requestCode) {
                case Constants.RequestCode.IDENTITY_AUTHENTICATION:
                    //从实名认证界面返回，刷新当前认证信息

                    break;
                case Constants.RequestCode.ALL_FUND_CODE:
                    //我的资产页面返回

                    break;
            }
        }
    }

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {

    }

    @Override
    public void getAccountSecurityFailure(String info) {

    }
}
