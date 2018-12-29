package io.bcaas.exchange.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.ui.view.RechargeView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * <p>
 * 「充值」
 */
public class RechargeActivity extends BaseActivity {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private RechargeView rechargeViewOne, rechargeViewTwo, rechargeViewThree;
    private List<View> views;

    private TabViewAdapter tabViewAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_recharge;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.recharge);
        views = new ArrayList<>();
    }

    @Override
    public void initData() {
        initTopTabData();
    }


    /**
     * 初始化顶部tab的数据以及相对应的界面信息
     */
    private void initTopTabData() {
        if (tabLayout == null) {
            return;
        }
        rechargeViewOne = new RechargeView(this);
//        rechargeViewOne.refreshData(buyDataBeansETH);
//        rechargeViewOne.setOnItemSelectListener(onItemSelectListener);
        views.add(rechargeViewOne);

        rechargeViewTwo = new RechargeView(this);
//        rechargeViewTwo.refreshData(buyDataBeansETH);
//        rechargeViewTwo.setOnItemSelectListener(onItemSelectListener);
        views.add(rechargeViewTwo);


        rechargeViewThree = new RechargeView(this);
//        rechargeViewThree.refreshData(buyDataBeansETH);
//        rechargeViewThree.setOnItemSelectListener(onItemSelectListener);
        views.add(rechargeViewThree);

        tabViewAdapter = new TabViewAdapter(views, "0");
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void initListener() {

        Disposable subscribe = RxView.clicks(ibBack).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        setResult(true);
                    }
                });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
//                        rechargeViewOne.refreshData(buyDataBeansETH);
                        break;
                    case 1:
//                        rechargeViewTwo.refreshData(buyDataBeansBTC);

                        break;
                    case 2:
//                        rechargeViewThree.refreshData(buyDataBeansZBB);

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
    public void onBackPressed() {
        super.onBackPressed();
        setResult(false);
    }
}
