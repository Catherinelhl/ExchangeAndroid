package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import io.bcaas.exchange.bean.UserInfoBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
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
 * <p>
 * 此页面有两种展现形式，如果当前还没有设置资金密码，那么就需要展现设置资金密码的页面，如果当前已经设置了，那么就直接展现用户的账户页面
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

    //定义每个Tab点击需要切换的信息
    private UserInfoBean userInfoBeanBTC, userInfoBeanETH, userInfoBeanZBB;

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
        String info = "请勿将ETH/ZBA发送至您的比特币(BTC)地址,否则资金将会遗失。比特币的交易需要六个区块的确认,可能会花费1个小时以上才能完成。";
        userInfoBeanBTC = new UserInfoBean("BTC", info, "39LKDBERWWRH343T34VSRG434V43F4G5GT5H");
        userInfoBeanETH = new UserInfoBean("ETH", info, "sdkjfhakssssjdfkasjdbfnaksdjfblniauksj");
        userInfoBeanZBB = new UserInfoBean("ZBB", info, "q234bv41v2b34m3b24mj12b34jm13hb4jffy1h");

        //初始化顶部tab的数据以及相对应的界面信息
        if (tabLayout == null) {
            return;
        }
        rechargeViewOne = new RechargeView(this);
        rechargeViewOne.refreshData(userInfoBeanBTC);
        rechargeViewOne.setOnItemSelectListener(onItemSelectListener);
        views.add(rechargeViewOne);

        rechargeViewTwo = new RechargeView(this);
        rechargeViewTwo.refreshData(userInfoBeanETH);
        rechargeViewTwo.setOnItemSelectListener(onItemSelectListener);
        views.add(rechargeViewTwo);


        rechargeViewThree = new RechargeView(this);
        rechargeViewThree.refreshData(userInfoBeanZBB);
        rechargeViewThree.setOnItemSelectListener(onItemSelectListener);
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
                        rechargeViewOne.refreshData(userInfoBeanBTC);
                        break;
                    case 1:
                        rechargeViewTwo.refreshData(userInfoBeanETH);

                        break;
                    case 2:
                        rechargeViewThree.refreshData(userInfoBeanZBB);

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

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            //跳转界面
            Intent intent = new Intent();
            intent.setClass(context, SetFundPasswordActivity.class);
            startActivityForResult(intent, Constants.RequestCode.FUND_PASSWORD);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case Constants.RequestCode.FUND_PASSWORD:
                //如果从「设置资金密码」页面跳转回来，那么需要重新刷新一下当前的界面
                if (tabLayout != null) {
                    rechargeViewOne.refreshData(userInfoBeanBTC);
                    rechargeViewTwo.refreshData(userInfoBeanETH);
                    rechargeViewThree.refreshData(userInfoBeanZBB);
                }
                break;
        }
    }
}
