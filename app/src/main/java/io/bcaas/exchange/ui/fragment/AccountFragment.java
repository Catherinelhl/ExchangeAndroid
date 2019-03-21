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
import io.bcaas.exchange.ui.activity.MyFundActivity;
import io.bcaas.exchange.ui.activity.TurnInActivity;
import io.bcaas.exchange.ui.activity.SafetyCenterActivity;
import io.bcaas.exchange.ui.activity.TurnOutActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * Fragment：「帳戶」
 */
public class AccountFragment extends BaseFragment {
    private String TAG = AccountFragment.class.getSimpleName();


    @BindView(R.id.iv_account)
    ImageView ivAccount;
    @BindView(R.id.tv_account_name)
    TextView tvAccountName;
    @BindView(R.id.rv_setting)
    RecyclerView rvSetting;
    private SettingsAdapter settingTypesAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_account;
    }

    @Override
    public void initViews(View view) {
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
        SettingsBean settingTypeBean3 = new SettingsBean(getString(R.string.turn_in), Constants.SettingType.RECHARGE);
        SettingsBean settingTypeBean4 = new SettingsBean(getString(R.string.turn_out), Constants.SettingType.WITH_DRAW);
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
                Intent intent = new Intent();
                if (type instanceof SettingsBean) {
                    SettingsBean settingTypeBean = (SettingsBean) type;
                    switch (settingTypeBean.getTag()) {
                        case MY_ALL_FUND:
                            intent.setClass(getContext(), MyFundActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.ALL_FUND_CODE);
                            break;
                        case RECHARGE:
                            intent.setClass(getContext(), TurnInActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.RECHARGE);
                            break;
                        case WITH_DRAW:
                            intent.setClass(getContext(), TurnOutActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.WITH_DRAW);
                            break;
                        case SAFETY_CENTER:
                            intent.setClass(getContext(), SafetyCenterActivity.class);
                            startActivityForResult(intent, Constants.RequestCode.SAFETY_CENTER);
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
