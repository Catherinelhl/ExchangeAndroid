package io.bcaas.exchange.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.BuyDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
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
    private List<BuyDataBean> buyDataBeansETH;
    private List<BuyDataBean> buyDataBeansBTC;
    private List<BuyDataBean> buyDataBeansZBB;


    private TabViewAdapter tabViewAdapter;
    private List<View> views;
    private BuyView buyViewOne, buyViewTwo, buyViewThree;

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_buy;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        buyDataBeansETH = new ArrayList<>();
        buyDataBeansBTC = new ArrayList<>();
        buyDataBeansZBB = new ArrayList<>();
        views = new ArrayList<>();
        if (tabLayout == null) {
            return;
        }
//        /**
//         * 判断是否需要顶部标签滑动
//         * 暂时定为如果便签的数量超过了五个，那么就需要移动
//         */
//        if (dataGenerationRegister != null) {
//            tabLayout.setTabMode(dataGenerationRegister.getTabTopTitleCount() > 5 ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
//        }

        for (int i = 0; i < 4; i++) {
            BuyDataBean buyDataBean = new BuyDataBean();
            buyDataBean.setPersonName("Alice");
            buyDataBean.setBuyMethod("支付方式ETH");
            buyDataBean.setPrice("2345.02387000 ETH");
            buyDataBean.setNumber("1.00000000 BTC");
            buyDataBean.setTotalAccount("2345.02387000 ETH");
            buyDataBean.setFee("0.00001 ETH");
            buyDataBeansETH.add(buyDataBean);


            BuyDataBean buyDataBeanBTC = new BuyDataBean();
            buyDataBeanBTC.setPersonName("Catherine");
            buyDataBeanBTC.setBuyMethod("支付方式BTC");
            buyDataBeanBTC.setPrice("345.02387000 BTC");
            buyDataBeanBTC.setNumber("1.00000000 ETH");
            buyDataBeanBTC.setTotalAccount("345.02387000 BTC");
            buyDataBeanBTC.setFee("0.00001 BTC");
            buyDataBeansBTC.add(buyDataBeanBTC);


            BuyDataBean buyDataBeanZBB = new BuyDataBean();
            buyDataBeanZBB.setPersonName("Lucifer");
            buyDataBeanZBB.setBuyMethod("支付方式ZBB");
            buyDataBeanZBB.setPrice("45.02387000 ZBB");
            buyDataBeanZBB.setNumber("1.00000000 BTC");
            buyDataBeanZBB.setTotalAccount("45.02387000 ZBB");
            buyDataBeanZBB.setFee("0.00001 ZBB");
            buyDataBeansZBB.add(buyDataBeanZBB);
        }

//        int size = dataGenerationRegister.getTabTopTitleCount();
//        for (int i = 0; i < size; i++) {
//            TabLayout.Tab tab = tabLayout.newTab();
//            tab.setText(dataGenerationRegister.getTabTopTitle(i));
//            tabLayout.addTab(tab);
//        }
//        topNavLayout.post(() -> setTabIndicatorWidth(topNavLayout, 30, 30));
        buyViewOne = new BuyView(getContext());
        buyViewTwo = new BuyView(getContext());
        buyViewThree = new BuyView(getContext());

        initBuyViewData(buyViewOne);
        initBuyViewData(buyViewTwo);
        initBuyViewData(buyViewThree);

        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

//        views = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//             View buyView = LayoutInflater.from(getContext()).inflate(R.layout.view_buy, null, false);
//            views.add(buyView);
//        }
//        tabViewAdapter = new TabViewAdapter(views);
//        viewPager.setAdapter(tabViewAdapter);
//        viewPager.setCurrentItem(0);
//        viewPager.setOffscreenPageLimit(3);
//        tabLayout.setupWithViewPager(viewPager);
    }

    private void initBuyViewData(BuyView buyView) {
        buyView.refreshData(buyDataBeansETH);
        buyView.setOnItemSelectListener(onItemSelectListener);
        views.add(buyView);

    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        buyViewOne.refreshData(buyDataBeansETH);
                        break;
                    case 1:
                        buyViewTwo.refreshData(buyDataBeansBTC);

                        break;
                    case 2:
                        buyViewThree.refreshData(buyDataBeansZBB);

                        break;
                }
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
            startActivityForResult(intent, Constants.RequestCode.BUY_DETAIL_CODE);
        }
    };

    /**
     * 重置当前界面
     */
    public void resetView() {
        if (viewPager != null) {
            viewPager.setCurrentItem(0);
        }
        if (tabLayout != null) {
            tabLayout.getTabAt(0).select();
            tabLayout.invalidate();
        }
    }

}
