package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.OrderRechargeAdapter;
import io.bcaas.exchange.adapter.OrderTransactionAdapter;
import io.bcaas.exchange.adapter.OrderWithdrawAdapter;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.OrderRechargeBean;
import io.bcaas.exchange.bean.OrderTransactionBean;
import io.bcaas.exchange.bean.OrderWithDrawBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.view.BuyView;
import io.bcaas.exchange.ui.view.OrderView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 訂單
 */
public class OrderFragment extends BaseFragment {

    @BindView(R.id.tab_layout_top)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private String TAG = OrderFragment.class.getSimpleName();
    private OrderView orderViewOne, orderViewTwo, orderViewThree;


    private List<OrderTransactionBean> orderTransactionBeans;
    private List<OrderRechargeBean> orderRechargeBeans;
    private List<OrderWithDrawBean> orderWithDrawBeans;

    private TabViewAdapter tabViewAdapter;
    private List<View> views;

    @Override
    protected void onUserVisible() {
        LogTool.i(TAG, "onUserVisible");

    }

    @Override
    protected void onUserInvisible() {
        LogTool.i(TAG, "onUserInvisible");

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_content;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        views = new ArrayList<>();
        orderTransactionBeans = new ArrayList<>();
        orderRechargeBeans = new ArrayList<>();
        orderWithDrawBeans = new ArrayList<>();
        initTopTabData();
    }

    /**
     * 初始化顶部tab的数据以及相对应的界面信息
     */
    private void initTopTabData() {
        for (int i = 0; i < 4; i++) {
            //初始化订单「交易」页面数据
            OrderTransactionBean orderTransactionBean = new OrderTransactionBean();
            orderTransactionBean.setOrderType("出售 BTC");
            orderTransactionBean.setOrderTime("2018/12/12");
            orderTransactionBean.setOrderStatus("待出售");
            orderTransactionBean.setOutCome("2.234251 BTC");
            orderTransactionBean.setInCome("234.2341354213 ETH");
            orderTransactionBean.setFee("0.001 ETH");
            orderTransactionBeans.add(orderTransactionBean);

            //初始化订单「充值」页面数据
            OrderRechargeBean orderRechargeBean = new OrderRechargeBean();
            orderRechargeBean.setOrderType("充值 BTC");
            orderRechargeBean.setOrderTime("2018/12/23");
            orderRechargeBean.setOrderStatus("失败");
            orderRechargeBean.setNumber("123.2343412 BTC");
            orderRechargeBean.setAddress("0xajsdnfkjdnfkjsdnfkjasdnfklsanfmlkasdf");
            orderRechargeBean.setCurrency("BTC");
            orderRechargeBeans.add(orderRechargeBean);

            //初始化订单「提现」页面数据
            OrderWithDrawBean orderWithDrawBean = new OrderWithDrawBean();
            orderWithDrawBean.setOrderType("提现 BTC");
            orderWithDrawBean.setOrderTime("2018/12/21");
            orderWithDrawBean.setOrderStatus("待验证");
            orderWithDrawBean.setNumber("123.3241234123 BTC");
            orderWithDrawBean.setAddress("0xjshdfjfdbjhabfjhasdbfjkhabsdkfj");
            orderWithDrawBean.setFee("0.001 BTC");
            orderWithDrawBean.setCurrency("BTC");
            orderWithDrawBeans.add(orderWithDrawBean);
        }


        orderViewOne = new OrderView(getContext());
        orderViewOne.setOrderTransactionAdapter(orderTransactionBeans);
        orderViewOne.setOnItemSelectListener(onItemSelectListener);
        views.add(orderViewOne);

        orderViewTwo = new OrderView(getContext());
        orderViewTwo.setOrderRechargeAdapter(orderRechargeBeans);
        orderViewTwo.setOnItemSelectListener(onItemSelectListener);
        views.add(orderViewTwo);


        orderViewThree = new OrderView(getContext());
        orderViewThree.setOrderWithDrawAdapter(orderWithDrawBeans);
        orderViewThree.setOnItemSelectListener(onItemSelectListener);
        views.add(orderViewThree);

        tabViewAdapter = new TabViewAdapter(views, "2");
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            LogTool.d(TAG, from);
            switch (from) {
                case Constants.From.ORDER_TRANSACTION:
                    break;
                case Constants.From.ORDER_RECHARGE:
                    break;
                case Constants.From.ORDER_WITHDRAW:
                    break;
            }
        }
    };

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        orderViewOne.setOrderTransactionAdapter(orderTransactionBeans);
                        break;
                    case 1:
                        orderViewTwo.setOrderRechargeAdapter(orderRechargeBeans);

                        break;
                    case 2:
                        orderViewThree.setOrderWithDrawAdapter(orderWithDrawBeans);

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


}
