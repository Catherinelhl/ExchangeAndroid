package io.bcaas.exchange.ui.activity;

import android.content.res.Resources;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.bean.ExchangeBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.SafetyCenterContract;
import io.bcaas.exchange.ui.contracts.MainContract;
import io.bcaas.exchange.ui.fragment.AccountFragment;
import io.bcaas.exchange.ui.fragment.BuyFragment;
import io.bcaas.exchange.ui.fragment.OrderFragment;
import io.bcaas.exchange.ui.fragment.SellFragment;
import io.bcaas.exchange.ui.presenter.SafetyCenterPresenterImp;
import io.bcaas.exchange.ui.presenter.MainPresenterImp;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 首頁主Activity
 */
public class MainActivity extends BaseActivity
        implements MainContract.View {
    @BindView(R.id.home_container)
    FrameLayout homeContainer;
    @BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    //声明当前需要和底部栏搭配的所有fragment
    private List<Fragment> fragments;
    //得到当前显示的Fragment
    private Fragment currentFragment;

    private MainContract.Presenter mainPresenter;

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
        tvTitle.setVisibility(View.VISIBLE);
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
        mainPresenter = new MainPresenterImp(this);
        ExchangeBean exchangeBean = new ExchangeBean();
        exchangeBean.setCurrency("BTC");
        exchangeBean.setBalance("8");
        /*获取币种汇率*/
        mainPresenter.getCurrencyUSDPrice(Constants.User.MEMBER_ID, exchangeBean);
        /*获取账户所有币种余额*/
        mainPresenter.getAllBalance();

        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = bottomTabLayout.newTab();
            // method 自定义布局-----
            tab.setCustomView(R.layout.item_bottom_tab);
            TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
//            textView.getPaint().setShader(getShader(textView, false));
            textView.setTextColor(context.getResources().getColor(R.color.grey_999999));
            textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(this, i, false), null, null);
            textView.setText(dataGenerationRegister.getTabTitle(i));
            //自定义布局-----

            bottomTabLayout.addTab(tab);
            if (i == 0) {
                onTabItemSelected(i);
                tab.getCustomView().findViewById(R.id.ll_tab_item).setSelected(true);
                textView.setTextColor(context.getResources().getColor(R.color.button_color));
                //method 2
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(this, 0, true), null, null);


            }
        }
    }

    /**
     * 设置标题
     */
    private void setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void initListener() {
        RxView.clicks(tvTitle).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //改变当前中间content信息；Fragment变换
                onTabItemSelected(tab.getPosition());
                //自定义:如果是自定义的tabItem，那么就需要调用这句来设置选中状态，虽然没有界面上的变化
                tab.getCustomView().findViewById(R.id.ll_tab_item).setSelected(true);
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
                textView.setTextColor(context.getResources().getColor(R.color.button_color));
                //method 2：如果是直接就用一个TextView控件来表示了，那么就可以直接用下面这一句来表示
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(MainActivity.this, tab.getPosition(), true), null, null);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //自定义
                tab.getCustomView().findViewById(R.id.ll_tab_item).setSelected(false);
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
                textView.setTextColor(context.getResources().getColor(R.color.grey_999999));
                //method 2
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(MainActivity.this, tab.getPosition(), false), null, null);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //自定义
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
                textView.setTextColor(context.getResources().getColor(R.color.button_color));
                //method 2
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationRegister.getDrawableTop(MainActivity.this, tab.getPosition(), true), null, null);

            }
        });

    }

    /**
     * 根据选中的position来切换选项卡
     *
     * @param position
     */
    private void onTabItemSelected(int position) {
        if (ListTool.noEmpty(fragments) && position < fragments.size()) {
            currentFragment = fragments.get(position);
            if (currentFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, currentFragment).commit();
            }
            switch (position) {
                case 0:
                    setTitle(getString(R.string.buy_title));
                    if (currentFragment instanceof BuyFragment) {
                        ((BuyFragment) currentFragment).resetView();
                    }
                    break;
                case 1:
                    setTitle(getString(R.string.sell_title));
                    break;
                case 2:
                    setTitle(getString(R.string.order_title));
                    break;
                case 3:
                    setTitle(getString(R.string.amount_title));
                    break;
            }
        }

    }

//    /**
//     * 设置底部选项卡渐变色
//     *
//     * @param textView
//     * @param isCheck
//     * @return
//     */
//    private LinearGradient getShader(TextView textView, boolean isCheck) {
//        return new LinearGradient(0, 0, 0, textView.getPaint().getTextSize(),
//                getResources().getColor(isCheck ? R.color.theme_FDD400 : R.color.grey_c5c5c5),
//                getResources().getColor(isCheck ? R.color.theme_FF6400 : R.color.grey_c5c5c5),
//                Shader.TileMode.CLAMP);
//    }


    /**
     * 设置tab Indicator 的宽度
     *
     * @param tabs
     * @param leftDip
     * @param rightDip
     */
    private void setTabIndicatorWidth(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("tabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }

        if (tabStrip != null) {
            tabStrip.setAccessible(true);
        }
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    public void getCurrencyUSDPriceSuccess(String info) {
        LogTool.d(TAG, info);
    }

    @Override
    public void getCurrencyUSDPriceFailure(String info) {
        showToast(info);
    }

    @Override
    public void getAllBalanceSuccess(List<MemberKeyVO> memberKeyVOList) {

    }

    @Override
    public void getAllBalanceFailure(String info) {
        showToast(info);
    }
}
