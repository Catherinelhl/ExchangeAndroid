package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.OrderRechargeBean;
import io.bcaas.exchange.bean.OrderTransactionBean;
import io.bcaas.exchange.bean.OrderWithDrawBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.gson.JsonTool;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.OrderRecordContract;
import io.bcaas.exchange.ui.presenter.OrderRecordPresenterImp;
import io.bcaas.exchange.ui.view.OrderView;
import io.bcaas.exchange.view.tablayout.BcaasTabLayout;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.PaginationVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 訂單
 */
public class OrderFragment extends BaseFragment implements OrderRecordContract.View {

    private String TAG = OrderFragment.class.getSimpleName();

    @BindView(R.id.tab_layout)
    BcaasTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private OrderRecordContract.Presenter presenter;
    private OrderView orderViewOne, orderViewTwo, orderViewThree;


    private List<OrderTransactionBean> orderTransactionBeans;
    private List<OrderRechargeBean> orderRechargeBeans;
    private List<OrderWithDrawBean> orderWithDrawBeans;

    private TabViewAdapter tabViewAdapter;
    private List<View> views;
    private String nextObjectIdRecharge = "1";
    private String nextObjectIdWithDraw = "1";
    private String nextObjectIdTx = "1";
    //得到当前选中的列表信息
    private PaginationVO paginationVO;

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
        presenter = new OrderRecordPresenterImp(this);
        initTopTabData();
    }

    /**
     * 初始化顶部tab的数据以及相对应的界面信息
     */
    private void initTopTabData() {
        if (tabLayout == null) {
            return;
        }
        // 获取「交易」页面的内容
        if (presenter != null) {
            presenter.getRecord(Constants.OrderType.TX, nextObjectIdTx);
        }
        // 移除所有的view
        tabLayout.removeTabLayout();
        for (int i = 0; i < 3; i++) {
            //显示标题
            tabLayout.addTab(dataGenerationRegister.getOrderTopTitles(i), i);
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

        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
        tabLayout.setupWithViewPager(viewPager, new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        if (presenter != null) {
                            presenter.getRecord(Constants.OrderType.TX, nextObjectIdTx);
                        }
                        orderViewOne.setOrderTransactionAdapter(orderTransactionBeans);
                        break;
                    case 1:
                        if (presenter != null) {
                            presenter.getRecord(Constants.OrderType.RECHARGE, nextObjectIdRecharge);
                        }

                        orderViewTwo.setOrderRechargeAdapter(orderRechargeBeans);

                        break;
                    case 2:
                        if (presenter != null) {
                            presenter.getRecord(Constants.OrderType.WITHDRAW, nextObjectIdWithDraw);
                        }
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
        tabLayout.resetSelectedTab(0);
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            LogTool.d(TAG, from);
            if (type == null) {
                return;
            }
            long memberOrderUid = (Long) type;
            switch (from) {
                case Constants.From.ORDER_TRANSACTION:
                    presenter.cancelOrder(memberOrderUid);
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
    }

    /**
     * 重置当前界面
     */
    public void resetView() {
        initTopTabData();
    }

    @Override
    public void getRecordFailure(String info) {
        showToast(info);
    }

    @Override
    public void getRecordSuccess(PaginationVO paginationVO) {
        LogTool.d(TAG, "PaginationVO:" + paginationVO);
        if (paginationVO != null) {
            this.paginationVO = paginationVO;
            int type = JsonTool.getInt(GsonTool.string(paginationVO.getObjectList()), "type", 0);
            switch (type) {
                case Constants.OrderType.RECHARGE:
                    nextObjectIdRecharge = paginationVO.getNextObjectId();
                    break;
                case Constants.OrderType.WITHDRAW:
                    nextObjectIdWithDraw = paginationVO.getNextObjectId();
                    break;
                case Constants.OrderType.TX:
                    nextObjectIdTx = paginationVO.getNextObjectId();
                    break;
            }

        }
    }

    @Override
    public void cancelOrderFailure(String info) {
        showToast(info);
    }

    @Override
    public void cancelOrderSuccess(MemberOrderVO memberOrderVO) {
        LogTool.d(TAG, "cancelOrderSuccess:" + memberOrderVO);
    }
}
