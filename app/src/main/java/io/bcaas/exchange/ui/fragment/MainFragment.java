package io.bcaas.exchange.ui.fragment;

import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.view.viewpager.CirclePageIndicator;
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
    @BindView(R.id.vp_structure_indicator)
    CirclePageIndicator vpStructureIndicator;
    @BindView(R.id.iv_broadcast_banner)
    ImageView ivBroadcastBanner;
    @BindView(R.id.vp_main_banner)
    ViewPager vpMainBanner;

    private int currentPager;//记录当前的页数
    private IconPagerFragmentAdapter viewFragmentAdapter;//图片页码的viewpager适配
    private List<View> viewList;//用来装拆分的article


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    public void initViews(View view) {
        currentPager = 0;
        viewList = new ArrayList<>();
        initMainTopBanner();
        viewFragmentAdapter = new IconPagerFragmentAdapter(viewList);
        initViewPager();
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

    private void initViewPager() {
        vpMainBanner.setOffscreenPageLimit(viewList.size());
        vpMainBanner.setAdapter(viewFragmentAdapter);
        vpMainBanner.setCurrentItem(0);
        iconPagerIndicator.setViewPager(vpMainBanner);
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
                currentPager = position;

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
