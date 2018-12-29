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
import io.bcaas.exchange.bean.SellDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.ui.activity.SellDetailActivity;
import io.bcaas.exchange.ui.view.BuyView;
import io.bcaas.exchange.ui.view.SellView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 「售出」
 * 拿到当前用户账户下面的各种币种的「可售余额」，根据点击TAB展现不同汇率数据，然后
 */
public class SellFragment extends BaseFragment {
    private String TAG = SellFragment.class.getSimpleName();

    @BindView(R.id.tab_layout_top)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private TabViewAdapter tabViewAdapter;
    private List<View> views;

    private SellView sellViewOne, sellViewTwo, sellViewThree;

    private SellDataBean sellDataBeanETH,sellDataBeanBTC,sellDataBeanZBB;

    @Override
    protected void onUserVisible() {
    }

    @Override
    protected void onUserInvisible() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_content;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        views = new ArrayList<>();
        initTopTabData();
    }

    /**
     * 初始化顶部tab的数据以及相对应的界面信息
     */
    private void initTopTabData() {
        sellDataBeanETH=new SellDataBean("ETH","BTC","9.234314","4235.234234");
        sellDataBeanBTC=new SellDataBean("BTC","ETH","8.234314","3235.234234");
        sellDataBeanZBB=new SellDataBean("ZBB","BTC","7.234314","5.234234");

        sellViewOne = new SellView(getContext());
        sellViewOne.refreshData(sellDataBeanETH);
        sellViewOne.setOnItemSelectListener(onItemSelectListener);
        views.add(sellViewOne);

        sellViewTwo = new SellView(getContext());
        sellViewTwo.refreshData(sellDataBeanBTC);
        sellViewTwo.setOnItemSelectListener(onItemSelectListener);
        views.add(sellViewTwo);


        sellViewThree = new SellView(getContext());
        sellViewThree.refreshData(sellDataBeanZBB);
        sellViewThree.setOnItemSelectListener(onItemSelectListener);
        views.add(sellViewThree);

        tabViewAdapter = new TabViewAdapter(views, "1");
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
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

            Intent intent = new Intent();
//                        Bundle bundle=new Bundle();
//                        bundle.putSerializable(Constants.KeyMaps.BUY_DETAIL,buyDataBean);
//                        intent.putExtras(bundle);
            intent.setClass(context, SellDetailActivity.class);
            startActivityForResult(intent, Constants.RequestCode.SELL_DETAIL_CODE);
        }
    };

}
