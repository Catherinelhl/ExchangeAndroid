package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.maker.DataGenerationRegister;
import io.bcaas.exchange.ui.fragment.AccountFragment;
import io.bcaas.exchange.ui.fragment.BuyFragment;
import io.bcaas.exchange.ui.fragment.OrderFragment;
import io.bcaas.exchange.ui.fragment.SellFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 首頁主Activity
 */
public class MainActivity extends BaseActivity {
    private String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.home_container)
    FrameLayout homeContainer;
    @BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;
    //声明当前需要和底部栏搭配的所有fragment
    private List<Fragment> fragments;

    private DataGenerationRegister dataGenerationRegister;


    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        fragments = new ArrayList<>();
        dataGenerationRegister = new DataGenerationRegister();
        BuyFragment fragment = new BuyFragment();
        fragments.add(fragment);
        SellFragment sellFragment = new SellFragment();
        fragments.add(sellFragment);
        OrderFragment orderFragment = new OrderFragment();
        fragments.add(orderFragment);
        AccountFragment accountFragment = new AccountFragment();
        fragments.add(accountFragment);
    }

    @Override
    public void initData() {
        //通过 ColorStateList设置颜色
//        ColorStateList colorStateList = ColorStateList.valueOf(getResources().getColor(R.color.colorAccent));
//        bottomTabLayout.setTabTextColors(colorStateList);
        //直接设置默认和选中颜色
//        bottomTabLayout.setTabTextColors(getResources().getColor(R.color.grey), getResources().getColor(R.color.orange));
        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = bottomTabLayout.newTab();
// method 自定义布局-----
            tab.setCustomView(R.layout.tab_item);
            TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
            textView.setTextColor(getResources().getColor(R.color.theme_grey));
            textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(this, i, false), null, null);
            textView.setText(dataGenerationRegister.getTabTitle(i));
            //自定义布局-----

            bottomTabLayout.addTab(tab);
            if (i == 0) {
                onTabItemSelected(i);
                tab.getCustomView().findViewById(R.id.ll_tab_item).setSelected(true);
                textView.setTextColor(getResources().getColor(R.color.theme_FF6400));
                //method 2
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(this, 0, true), null, null);


            }
        }
    }

    @Override
    public void initListener() {
        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //改变当前中间content信息；Fragment变换
                onTabItemSelected(tab.getPosition());
                //自定义:如果是自定义的tabItem，那么就需要调用这句来设置选中状态，虽然没有界面上的变化
                tab.getCustomView().findViewById(R.id.ll_tab_item).setSelected(true);
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
                textView.setTextColor(getResources().getColor(R.color.theme_FF6400));
                //method 2：如果是直接就用一个TextView控件来表示了，那么就可以直接用下面这一句来表示
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(MainActivity.this, tab.getPosition(), true), null, null);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //自定义
                tab.getCustomView().findViewById(R.id.ll_tab_item).setSelected(false);
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
                textView.setTextColor(getResources().getColor(R.color.theme_grey));
                //method 2
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(MainActivity.this, tab.getPosition(), false), null, null);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //自定义
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
                textView.setTextColor(getResources().getColor(R.color.theme_FF6400));
                //method 2
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(MainActivity.this, tab.getPosition(), true), null, null);

            }
        });
    }

    private void onTabItemSelected(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).commit();
        }
    }
}
