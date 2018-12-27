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
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.SettingsBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.activity.MyFundActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 帳戶
 */
public class AccountFragment extends BaseFragment {
    @BindView(R.id.iv_account)
    ImageView ivAccount;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.rv_setting)
    RecyclerView rvSetting;
    private String TAG = AccountFragment.class.getSimpleName();
    private SettingsAdapter settingTypesAdapter;

    @Override
    protected void onUserVisible() {
        LogTool.i(TAG, "onUserVisible");

    }

    @Override
    protected void onUserInvisible() {
        LogTool.i(TAG, "onUserInvisible");

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogTool.d(TAG, "setUserVisibleHint:" + isVisibleToUser);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_account;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        initAdapter();
    }

    private void initAdapter() {
        List<SettingsBean> settingTypes = initSettingTypes();//得到设置页面需要显示的所有设置选项
        settingTypesAdapter = new SettingsAdapter(context, settingTypes);
        rvSetting.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
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
        SettingsBean settingTypeBean3 = new SettingsBean(getString(R.string.recharge), Constants.SettingType.RECHARGE);
        SettingsBean settingTypeBean4 = new SettingsBean(getString(R.string.with_draw), Constants.SettingType.WITH_DRAW);
        SettingsBean settingTypeBean5 = new SettingsBean(getString(R.string.safety_center), Constants.SettingType.SAFETY_CENTER);
        settingTypes.add(settingTypeBean);
        settingTypes.add(settingTypeBean3);
        settingTypes.add(settingTypeBean4);
        settingTypes.add(settingTypeBean5);
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
                if (type instanceof SettingsBean) {
                    SettingsBean settingTypeBean = (SettingsBean) type;
                    switch (settingTypeBean.getTag()) {
                        case MY_ALL_FUND:
                            Intent intent = new Intent();
                            intent.setClass(getContext(), MyFundActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.ALL_FUND_CODE);
                            break;
                        case RECHARGE:
//                            intentToActivity(null, ModifyAuthorizedRepresentativesActivity.class, false);
                            break;
                        case WITH_DRAW:
//                            intentToActivity(null, AddressManagerActivity.class, false);
                            break;
                        case SAFETY_CENTER:
//                            intentToActivity(null, LanguageSwitchingActivity.class, false);
                            break;

                    }
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.RequestCode.ALL_FUND_CODE) {
                //我的资产页面返回
            }
        }
    }
}
