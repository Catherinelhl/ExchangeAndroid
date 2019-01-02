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
import io.bcaas.exchange.bean.UserInfoBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.ui.view.RechargeView;
import io.bcaas.exchange.ui.view.WithDrawView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * <p>
 * 「提现」
 */
public class WithDrawActivity extends BaseActivity {

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

    private WithDrawView withDrawViewOne, withDrawViewTwo, withDrawViewThree;
    private UserInfoBean userInfoBeanBTC, userInfoBeanETH, userInfoBeanZBB;
    private List<View> views;
    private TabViewAdapter tabViewAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        views=new ArrayList<>();
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.with_draw);
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
        withDrawViewOne = new WithDrawView(this);
        withDrawViewOne.refreshData(userInfoBeanBTC);
        withDrawViewOne.setOnItemSelectListener(onItemSelectListener);
        views.add(withDrawViewOne);

        withDrawViewTwo = new WithDrawView(this);
        withDrawViewTwo.refreshData(userInfoBeanETH);
        withDrawViewTwo.setOnItemSelectListener(onItemSelectListener);
        views.add(withDrawViewTwo);


        withDrawViewThree = new WithDrawView(this);
        withDrawViewThree.refreshData(userInfoBeanZBB);
        withDrawViewThree.setOnItemSelectListener(onItemSelectListener);
        views.add(withDrawViewThree);

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(false);
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {

        }
    };
}
