package io.bcaas.exchange.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.BuyDataAdapter;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.BuyDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.activity.BuyDetailActivity;
import io.bcaas.exchange.ui.view.BuyView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 買進
 */
public class BuyFragment extends BaseFragment {
    private String TAG = BuyFragment.class.getSimpleName();


    @BindView(R.id.tab_layout_buy)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private List<BuyDataBean> buyDataBeans;


    private TabViewAdapter tabViewAdapter;
    private List<View> views;

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
        return R.layout.fragment_buy;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        buyDataBeans = new ArrayList<>();
        initBuyData();
        initTopNavTab();
    }

    /**
     * 初始化顶部导航栏
     */
    private void initTopNavTab() {
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

        views = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            BuyView view = new BuyView(getActivity());
            view.refreshData(buyDataBeans);
            view.setOnItemSelectListener(onItemSelectListener);
            views.add(view);
        }
        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initBuyData() {
        for (int i = 0; i < 4; i++) {
            BuyDataBean buyDataBean = new BuyDataBean();
            buyDataBean.setPersonName("Alice");
            buyDataBean.setBuyMethod("支付方式ETH");
            buyDataBean.setPrice("2345.02387000 ETH");
            buyDataBean.setNumber("1.00000000 BTC");
            buyDataBean.setTotalAccount("2345.02387000 ETH");
            buyDataBean.setFee("0.00001 ETH");
            buyDataBeans.add(buyDataBean);
        }
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
    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            if (type == null) {
                return;
            }
            BuyDataBean buyDataBean = (BuyDataBean) type;
            LogTool.d(TAG, buyDataBean);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KeyMaps.BUY_DETAIL, buyDataBean);
            intent.putExtras(bundle);
            intent.setClass(context, BuyDetailActivity.class);
//            context.startActivityForResult(intent, Constants.RequestCode.BUY_DETAIL_CODE);
        }
    };
}
