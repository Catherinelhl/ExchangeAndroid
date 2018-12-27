package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.OrderRechargeAdapter;
import io.bcaas.exchange.adapter.OrderTransactionAdapter;
import io.bcaas.exchange.adapter.OrderWithdrawAdapter;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.OrderRechargeBean;
import io.bcaas.exchange.bean.OrderTransactionBean;
import io.bcaas.exchange.bean.OrderWithDrawBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 訂單
 */
public class OrderFragment extends BaseFragment {
    @BindView(R.id.rv_order_data)
    RecyclerView rvOrderData;
    @BindView(R.id.srl_order_data)
    SwipeRefreshLayout srlOrderData;
    @BindView(R.id.tab_layout_order)
    TabLayout tabLayout;
    private String TAG = OrderFragment.class.getSimpleName();

    //订单页面「交易」数据显示的适配器
    private OrderTransactionAdapter orderTransactionAdapter;
    //订单页面「充值」数据显示的适配器
    private OrderRechargeAdapter orderRechargeAdapter;
    //订单页面「提现」数据显示的适配器
    private OrderWithdrawAdapter orderWithdrawAdapter;

    private List<OrderTransactionBean> orderTransactionBeans;
    private List<OrderRechargeBean> orderRechargeBeans;
    private List<OrderWithDrawBean> orderWithDrawBeans;

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
        return R.layout.fragment_order;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        orderTransactionBeans = new ArrayList<>();
        orderRechargeBeans = new ArrayList<>();
        orderWithDrawBeans = new ArrayList<>();
        initData();
        initRefreshLayout();
        initOrderAdapter();
        initTopNavTab(2);
    }

    private void initRefreshLayout() {
        // 设置加载按钮的形态
        srlOrderData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color

        );
        srlOrderData.setSize(SwipeRefreshLayout.DEFAULT);
    }


    /**
     * 初始化数据
     */
    private void initData() {
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
    }

    /**
     * 初始化顶部导航栏
     */
    private void initTopNavTab(int position) {
        if (tabLayout == null) {
            return;
        }
        /**
         * 判断是否需要顶部标签滑动
         * 暂时定为如果便签的数量超过了五个，那么就需要移动
         */
        if (dataGenerationRegister != null) {
            tabLayout.setTabMode(dataGenerationRegister.getTabTopTitleCount() > 5 ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        }
        tabLayout.removeAllTabs();
        int size = dataGenerationRegister.getTabOrderTopTitleCount();
        for (int i = 0; i < size; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(dataGenerationRegister.getOrderTopTitles(i));
            tabLayout.addTab(tab);
        }
//        topNavLayout.post(() -> setTabIndicatorWidth(topNavLayout, 30, 30));
    }

    /**
     * 初始化所有订单的数据，然后默认将交易填充进去
     */
    private void initOrderAdapter() {
        if (orderTransactionAdapter == null) {
            orderTransactionAdapter = new OrderTransactionAdapter(getContext(), orderTransactionBeans);
            orderTransactionAdapter.setOnItemSelectListener(onItemSelectListener);
        }


        rvOrderData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
        rvOrderData.setLayoutManager(linearLayoutManager);
        rvOrderData.setAdapter(orderTransactionAdapter);
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
        srlOrderData.setOnRefreshListener(() -> {
            srlOrderData.setRefreshing(false);
            //判断如果当前没有币种，那么就暂时不能刷新数据
//            if (StringTool.isEmpty(Bas.getBlockService())) {
//                return;
//            }
//            onRefreshTransactionRecord("swipeRefreshLayout");
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void switchTab(int position) {
        if (rvOrderData == null) {
            return;
        }
        switch (position) {
            case 0:
                setOrderTransactionAdapter();
                break;
            case 1:
                setOrderRechargeAdapter();
                break;
            case 2:
                setOrderWithDrawAdapter();
                break;
        }
    }

    private void setOrderRechargeAdapter() {
        if (orderRechargeAdapter == null) {
            orderRechargeAdapter = new OrderRechargeAdapter(getContext(), orderRechargeBeans);
            orderRechargeAdapter.setOnItemSelectListener(onItemSelectListener);
        }
        rvOrderData.setAdapter(orderRechargeAdapter);
    }

    private void setOrderWithDrawAdapter() {
        if (orderWithdrawAdapter == null) {
            orderWithdrawAdapter = new OrderWithdrawAdapter(getContext(), orderWithDrawBeans);
            orderWithdrawAdapter.setOnItemSelectListener(onItemSelectListener);
        }
        rvOrderData.setAdapter(orderWithdrawAdapter);
    }

    private void setOrderTransactionAdapter() {
        if (orderTransactionAdapter == null) {
            orderTransactionAdapter = new OrderTransactionAdapter(getContext(), orderTransactionBeans);
            orderTransactionAdapter.setOnItemSelectListener(onItemSelectListener);
        }
        rvOrderData.setAdapter(orderTransactionAdapter);
    }
}
