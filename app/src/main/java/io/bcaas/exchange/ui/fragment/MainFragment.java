package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.view.viewpager.IconPageIndicator;
import io.bcaas.exchange.view.viewpager.IconPagerFragmentAdapter;

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
+ description  +   v1.2 版首页
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/

public class MainFragment extends BaseFragment {
    @BindView(R.id.icon_pager_indicator)
    IconPageIndicator iconPagerIndicator;
    @BindView(R.id.iv_broadcast_banner)
    ImageView ivBroadcastBanner;
    @BindView(R.id.vp_main_banner)
    ViewPager vpMainBanner;
    @BindView(R.id.vp_coin_info)
    ViewPager vpCoinInfo;
    @BindView(R.id.icon_coin_info_indicator)
    IconPageIndicator iconCoinInfoIndicator;

    private IconPagerFragmentAdapter viewFragmentAdapter;//图片页码的viewpager适配
    private IconPagerFragmentAdapter viewFragmentAdapterCoinType;//图片页码的viewpager适配
    private List<View> viewList;
    private List<View> coinTypeViewList;


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    public void initViews(View view) {
        viewList = new ArrayList<>();
        coinTypeViewList=new ArrayList<>();
        initMainTopBanner();
        initCoinTypeData();
        viewFragmentAdapter = new IconPagerFragmentAdapter(viewList);
        viewFragmentAdapterCoinType = new IconPagerFragmentAdapter(coinTypeViewList);
        initViewPager();
        initCoinTypeViewPager();
    }

    private void initMainTopBanner() {
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.view_main_banner, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_broadcast_banner);
//            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size_21));
//            textView.setTypeface(Typeface.DEFAULT);
            viewList.add(view);
        }
    }
    private void initCoinTypeData() {
        for (int i = 0; i < 2; i++) {
            View view = LayoutInflater.from(activity).inflate(R.layout.view_main_coin_info, null);
            coinTypeViewList.add(view);
        }
    }

    private void initViewPager() {
        vpMainBanner.setOffscreenPageLimit(viewList.size());
        vpMainBanner.setAdapter(viewFragmentAdapter);
        vpMainBanner.setCurrentItem(0);
        iconPagerIndicator.setViewPager(vpMainBanner);
    }
    private void initCoinTypeViewPager() {
        vpCoinInfo.setOffscreenPageLimit(coinTypeViewList.size());
        vpCoinInfo.setAdapter(viewFragmentAdapterCoinType);
        vpCoinInfo.setCurrentItem(0);
        iconCoinInfoIndicator.setViewPager(vpCoinInfo);
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {
        iconPagerIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void cancelSubscribe() {

    }
}
