package io.bcaas.exchange.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.activity.BuyDetailActivity;
import io.bcaas.exchange.ui.activity.SellDetailActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 賣出
 */
public class SellFragment extends BaseFragment {
    private String TAG = SellFragment.class.getSimpleName();

    @BindView(R.id.tab_layout_top)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @Override
    protected void onUserVisible() {
        LogTool.i(TAG, "onUserVisible");

    }

    @Override
    protected void onUserInvisible() {
        LogTool.i(TAG, "onUserInvisible");

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_content;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        initTopNavTab(1);
    }

    /**
     * 初始化顶部导航栏
     */
    private void initTopNavTab(int position) {
        if (tabLayout == null) {
            return;
        }
        /**
         * 判断是否需要顶部标签滑动
         * 暂时定为如果便签的数量超过了五个，那么就需要移动
         */
        if (dataGenerationRegister != null) {
            tabLayout.setTabMode(dataGenerationRegister.getTabTopTitleCount() > 5 ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        }
        tabLayout.removeAllTabs();
        int size = dataGenerationRegister.getTabTopTitleCount();
        for (int i = 0; i < size; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(dataGenerationRegister.getTabTopTitle(i));
            tabLayout.addTab(tab);
        }
//        topNavLayout.post(() -> setTabIndicatorWidth(topNavLayout, 30, 30));
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
        }
    }

}
