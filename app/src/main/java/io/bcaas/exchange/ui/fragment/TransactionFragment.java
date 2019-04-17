package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.ui.view.TransactionView;
import io.bcaas.exchange.view.viewGroup.TransactionTabLayout;
import io.bcaas.exchange.view.viewpager.BaseViewPager;

import java.util.ArrayList;
import java.util.List;

/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/4/11 14:26
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.ui.fragment
+--------------+---------------------------------
+ description  +   v1.2 版交易
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public class TransactionFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    TransactionTabLayout tabLayout;
    @BindView(R.id.viewpager)
    BaseViewPager viewPager;
    @BindView(R.id.srl_data)
    SwipeRefreshLayout srlData;
    @BindView(R.id.ll_transaction)
    LinearLayout llTransaction;
    private TabViewAdapter tabViewAdapter;
    private List<View> views;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_transaction;
    }

    @Override
    public void initViews(View view) {

        // 设置加载按钮的形态
        srlData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color
        );
        //设置下拉进度条的背景颜色，默认白色
        srlData.setProgressBackgroundColorSchemeResource(R.color.transparent);
        srlData.setSize(SwipeRefreshLayout.DEFAULT);
        views = new ArrayList<>();
        int size = dataGenerationManager.getTransactionTitleSize();
        tabLayout.setTabSize(size);

        for (int i = 0; i < size; i++) {
            //显示标题
            tabLayout.addTab(dataGenerationManager.getTransactionTitle(i), i);
            TransactionView transactionView = new TransactionView(activity);
            views.add(transactionView);
        }

        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(size);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
        tabLayout.setupWithViewPager(viewPager, new TabLayout.OnTabSelectedListener() {
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
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void cancelSubscribe() {

    }

}
