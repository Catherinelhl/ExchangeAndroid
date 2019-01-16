package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import com.google.gson.reflect.TypeToken;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.gson.JsonTool;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
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
 * 点击此页面，展现第一个页面的交易信息；然后点击tapTitle，进行网络的请求
 */
public class OrderFragment extends BaseFragment implements OrderRecordContract.View {

    private String TAG = OrderFragment.class.getSimpleName();

    @BindView(R.id.tab_layout)
    BcaasTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private OrderRecordContract.Presenter presenter;
    private List<MemberOrderVO> memberOrderVOList;

    private TabViewAdapter tabViewAdapter;
    private List<View> views;
    private String nextObjectIdRecharge = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
    private String nextObjectIdWithDraw = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
    private String nextObjectIdTx = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
    //得到当前选中的列表信息
    private PaginationVO paginationVO;
    private int currentPosition;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_content;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        views = new ArrayList<>();
        memberOrderVOList = new ArrayList<>();
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
            OrderView orderView = new OrderView(getContext());
            orderView.setAdapter(memberOrderVOList, currentPosition);
            orderView.setOnItemSelectListener(onItemSelectListener);
            views.add(orderView);
        }

        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
        tabLayout.setupWithViewPager(viewPager, new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPosition = tab.getPosition();
                memberOrderVOList.clear();
                refreshView();
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

    private void refreshView() {
        LogTool.d(TAG, "refreshView:" + currentPosition);
        if (ListTool.noEmpty(views) && currentPosition < views.size()) {
            ((OrderView) views.get(currentPosition)).setAdapter(memberOrderVOList, currentPosition);
        }
        switch (currentPosition) {
            case 0:
                if (presenter != null) {
                    presenter.getRecord(Constants.OrderType.TX, nextObjectIdTx);
                }
                break;
            case 1:
                if (presenter != null) {
                    presenter.getRecord(Constants.OrderType.RECHARGE, nextObjectIdRecharge);
                }
                break;
            case 2:
                if (presenter != null) {
                    presenter.getRecord(Constants.OrderType.WITHDRAW, nextObjectIdWithDraw);
                }
                break;
        }

    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            LogTool.d(TAG, from);
            if (type == null) {
                return;
            }
            if (type instanceof Long) {
                long memberOrderUid = (Long) type;
                switch (from) {
                    case Constants.From.ORDER_CANCEL_TRANSACTION:
                        // 撤销订单
                        presenter.cancelOrder(memberOrderUid);
                        break;
                    case Constants.From.ORDER_RECHARGE:
                        presenter.cancelOrder(memberOrderUid);
                        break;
                    case Constants.From.ORDER_WITHDRAW:
                        presenter.cancelOrder(memberOrderUid);
                        break;
                }
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
            //得到当前接口的页面信息
            String nextObjectId = paginationVO.getNextObjectId();
            int totalPageNumber = paginationVO.getTotalPageNumber();
            long totalObjectNumber = paginationVO.getTotalObjectNumber();

            List<Object> objects = paginationVO.getObjectList();

            if (ListTool.isEmpty(objects)) {
                memberOrderVOList.clear();
            } else {
                memberOrderVOList = GsonTool.convert(GsonTool.string(paginationVO.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                }.getType());
            }
            int type = JsonTool.getInt(GsonTool.string(paginationVO.getObjectList()), "type", 0);
            LogTool.d(TAG, "当前的Type为：" + type);
            LogTool.d(TAG, "memberOrderVOList:" + memberOrderVOList);
            switch (type) {
                case Constants.OrderType.RECHARGE:
                    if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                        ((OrderView) views.get(1)).setAdapter(memberOrderVOList, 1);
                    }
                    nextObjectIdRecharge = paginationVO.getNextObjectId();
                    break;
                case Constants.OrderType.WITHDRAW:
                    if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                        ((OrderView) views.get(2)).setAdapter(memberOrderVOList, 2);
                    }
                    nextObjectIdWithDraw = paginationVO.getNextObjectId();
                    break;
                default:
                    if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                        ((OrderView) views.get(0)).setAdapter(memberOrderVOList, 0);
                    }
                    nextObjectIdTx = paginationVO.getNextObjectId();
                    break;
            }
        } else {
            //清空当前列表
            memberOrderVOList.clear();
            //判断当前的选中position，然后更新当前页面
            if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                ((OrderView) views.get(currentPosition)).setAdapter(memberOrderVOList, currentPosition);
            }
        }
    }

    @Override
    public void cancelOrderFailure(String info) {
        showToast(info);
    }

    @Override
    public void cancelOrderSuccess(MemberOrderVO memberOrderVO) {
        refreshView();
    }
}
