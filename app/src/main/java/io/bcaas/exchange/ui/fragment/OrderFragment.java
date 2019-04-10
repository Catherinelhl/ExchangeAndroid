package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import butterknife.BindView;
import com.google.gson.reflect.TypeToken;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.listener.LoadingDataListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.OrderRecordContract;
import io.bcaas.exchange.ui.presenter.OrderRecordPresenterImp;
import io.bcaas.exchange.ui.view.OrderView;
import io.bcaas.exchange.view.dialog.DoubleButtonDialog;
import io.bcaas.exchange.view.viewGroup.BaseTabLayout;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.PaginationVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * Fragment：「訂單」
 * Notice：点击此页面，展现第一个页面的交易信息；然后点击tapTitle，进行网络的请求
 */
public class OrderFragment extends BaseFragment implements OrderRecordContract.View {

    private String TAG = OrderFragment.class.getSimpleName();

    @BindView(R.id.tab_layout)
    BaseTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.srl_data)
    SwipeRefreshLayout srlData;
    private OrderRecordContract.Presenter presenter;
    private List<MemberOrderVO> memberOrderVOList;

    private TabViewAdapter tabViewAdapter;
    private List<View> views;
    private String nextObjectIdTurnIn = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
    private String nextObjectIdTurnOut = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
    private String nextObjectIdTx = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
    private String nextObjectIdRecharge = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
    private String nextObjectIdBuyBack = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
    //得到当前选中的列表信息
    private PaginationVO paginationVO;
    private int currentPosition;
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_order;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        views = new ArrayList<>();
        memberOrderVOList = new ArrayList<>();
        presenter = new OrderRecordPresenterImp(this);
        // 设置加载按钮的形态
        srlData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color

        );
        srlData.setSize(SwipeRefreshLayout.DEFAULT);
    }

    /**
     * 初始化顶部tab的数据以及相对应的界面信息
     */
    private void refreshView() {
        if (tabLayout == null && viewPager == null) {
            return;
        }
        // 获取「交易」页面的内容
        if (presenter != null) {
            presenter.getRecord(Constants.OrderType.TX, MessageConstants.DEFAULT_NEXT_OBJECT_ID);
        }
        // 移除所有的view
        tabLayout.removeTabLayout();
        viewPager.removeAllViews();
        views.clear();
        int size = dataGenerationManager.getTabOrderTopTitleCount();
        tabLayout.setTabSize(size);

        for (int i = 0; i < size; i++) {
            //显示标题
            tabLayout.addTab(dataGenerationManager.getOrderTopTitles(i), i);
            OrderView orderView = new OrderView(activity);
            orderView.setAdapter(memberOrderVOList, currentPosition);
            orderView.setCanLoadingMore(false);
            orderView.setOnItemSelectListener(onItemSelectListener);
            orderView.setLoadingDataListener(loadingDataListener);
            views.add(orderView);
        }

        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(size);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
        tabLayout.setupWithViewPager(viewPager, new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPosition = tab.getPosition();
                memberOrderVOList.clear();
                refreshView(1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 刷新当前视图
     *
     * @param refreshType 刷新的类型，共有三种；1：下拉刷新；2：上拉加载；3：刷新上一次的记录
     */
    private void refreshView(int refreshType) {
        LogTool.d(TAG, "refreshView:" + currentPosition);
        //1：替换当前界面的当前数据
        if (ListTool.noEmpty(views) && currentPosition < views.size()) {
            ((OrderView) views.get(currentPosition)).setAdapter(memberOrderVOList, currentPosition);
        }
        //2：开始重新请求当前界面需要的数据
        switch (currentPosition) {
            case 0:
                if (presenter != null) {
                    String currentNextObjectID = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
                    switch (refreshType) {
                        case 1://下拉刷新
                            // MessageConstants.DEFAULT_NEXT_OBJECT_ID
                            break;
                        case 2://上拉加载
                            currentNextObjectID = nextObjectIdTx;
                            break;
                        case 3://撤销当前订单
                            int nextObjectIdInt = Integer.valueOf(nextObjectIdTx);
                            if (nextObjectIdInt > 0) {
                                nextObjectIdInt--;
                                currentNextObjectID = String.valueOf(nextObjectIdInt);
                            } else {
                                currentNextObjectID = nextObjectIdTx;
                            }
                            break;
                    }
                    presenter.getRecord(Constants.OrderType.TX, currentNextObjectID);
                }
                break;
            case 1:
                if (presenter != null) {
                    String currentNextObjectID = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
                    switch (refreshType) {
                        case 1://下拉刷新
                            // MessageConstants.DEFAULT_NEXT_OBJECT_ID
                            break;
                        case 2://上拉加载
                            currentNextObjectID = nextObjectIdTurnIn;
                            break;
                        case 3://撤销当前订单
                            int nextObjectIdInt = Integer.valueOf(nextObjectIdTurnIn);
                            if (nextObjectIdInt > 0) {
                                nextObjectIdInt--;
                                currentNextObjectID = String.valueOf(nextObjectIdInt);
                            } else {
                                currentNextObjectID = nextObjectIdTurnIn;
                            }
                            break;
                    }
                    presenter.getRecord(Constants.OrderType.TURN_IN, currentNextObjectID);
                }
                break;
            case 2:
                if (presenter != null) {
                    String currentNextObjectID = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
                    switch (refreshType) {
                        case 1://下拉刷新
                            // MessageConstants.DEFAULT_NEXT_OBJECT_ID
                            break;
                        case 2://上拉加载
                            currentNextObjectID = nextObjectIdTurnOut;
                            break;
                        case 3://撤销当前订单
                            int nextObjectIdInt = Integer.valueOf(nextObjectIdTurnOut);
                            if (nextObjectIdInt > 0) {
                                nextObjectIdInt--;
                                currentNextObjectID = String.valueOf(nextObjectIdInt);
                            } else {
                                currentNextObjectID = nextObjectIdTurnOut;
                            }
                            break;
                    }
                    presenter.getRecord(Constants.OrderType.TURN_OUT, currentNextObjectID);
                }
                break;
            case 3:
                if (presenter != null) {
                    String currentNextObjectID = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
                    presenter.getRecord(Constants.OrderType.RECHARGE, currentNextObjectID);

                }
                break;
            case 4:
                if (presenter != null) {
                    String currentNextObjectID = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
                    presenter.getRecord(Constants.OrderType.BUY_BACK, currentNextObjectID);

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
                        // 弹框提示用户是否确认撤销
                        if (activity != null) {
                            ((BaseActivity) activity).showDoubleButtonDialog(getString(R.string.sure_to_cancel_order), new DoubleButtonDialog.ConfirmClickListener() {
                                @Override
                                public void sure() {
                                    // 撤销订单
                                    presenter.cancelOrder(memberOrderUid);
                                }

                                @Override
                                public void cancel() {
                                }
                            });
                        }


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
        srlData.setOnRefreshListener(() -> {
            srlData.setRefreshing(false);
            refreshView(1);
        });
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        refreshView();
    }

    @Override
    protected void cancelSubscribe() {
        presenter.cancelSubscribe();
    }

    @Override
    public void getRecordFailure(String info, boolean isRefresh) {
        if (isRefresh) {
            if (srlData != null) {
                srlData.setRefreshing(false);
            }
        }
        showToast(info, true);

    }

    @Override
    public void getRecordSuccess(PaginationVO paginationVO, boolean isRefresh, int responseType) {
        GsonTool.logInfo(TAG, "PaginationVO:", paginationVO);
        if (isRefresh) {
            if (srlData != null) {
                srlData.setRefreshing(false);
            }
        }
        if (paginationVO != null) {
            this.paginationVO = paginationVO;
            Integer totalPageNumber = paginationVO.getTotalPageNumber();
            boolean canLoadingMore = false;
            //判断当前是否需要继续加载
            List<Object> objects = paginationVO.getObjectList();
            //如果当前是刷新请求的接口回来，那么就需要清空当前的数据
            if (isRefresh) {
                if (ListTool.isEmpty(objects)) {
                    memberOrderVOList.clear();
                } else {
                    memberOrderVOList = GsonTool.convert(GsonTool.string(paginationVO.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                    }.getType());
                }
            } else {
                if (ListTool.noEmpty(objects)) {
                    //否则就是加载数据返回的，就是追加数据
                    List<MemberOrderVO> memberOrderVOListTemp = GsonTool.convert(GsonTool.string(paginationVO.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                    }.getType());
                    memberOrderVOList.addAll(memberOrderVOListTemp);
                }

            }
            GsonTool.logInfo(TAG, "memberOrderVOList:", memberOrderVOList);
            String objectID = paginationVO.getNextObjectId();
            switch (responseType) {
                case Constants.OrderType.TURN_IN:
                    //判断当前是否需要继续加载
                    if (StringTool.equals(MessageConstants.NEXT_PAGE_IS_EMPTY, objectID)) {
                        nextObjectIdTurnIn = MessageConstants.NEXT_PAGE_IS_EMPTY_SYMBOL;
                    } else {
                        nextObjectIdTurnIn = objectID;
                        canLoadingMore = true;
                    }
                    if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                        ((OrderView) views.get(1)).setAdapter(memberOrderVOList, 1);
                        ((OrderView) views.get(1)).setCanLoadingMore(canLoadingMore);
                    }
                    break;
                case Constants.OrderType.TURN_OUT:
                    if (StringTool.equals(MessageConstants.NEXT_PAGE_IS_EMPTY, objectID)) {
                        nextObjectIdTurnOut = MessageConstants.NEXT_PAGE_IS_EMPTY_SYMBOL;
                    } else {
                        nextObjectIdTurnOut = objectID;
                        canLoadingMore = true;
                    }
                    if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                        ((OrderView) views.get(2)).setAdapter(memberOrderVOList, 2);
                        ((OrderView) views.get(2)).setCanLoadingMore(canLoadingMore);

                    }
                    break;
                case Constants.OrderType.TX:
                    if (StringTool.equals(MessageConstants.NEXT_PAGE_IS_EMPTY, objectID)) {
                        nextObjectIdTx = MessageConstants.NEXT_PAGE_IS_EMPTY_SYMBOL;
                    } else {
                        nextObjectIdTx = objectID;
                        canLoadingMore = true;
                    }
                    if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                        ((OrderView) views.get(0)).setAdapter(memberOrderVOList, 0);
                        ((OrderView) views.get(0)).setCanLoadingMore(canLoadingMore);

                    }
                    break;
                case Constants.OrderType.RECHARGE:
                    if (StringTool.equals(MessageConstants.NEXT_PAGE_IS_EMPTY, objectID)) {
                        nextObjectIdRecharge = MessageConstants.NEXT_PAGE_IS_EMPTY_SYMBOL;
                    } else {
                        nextObjectIdRecharge = objectID;
                        canLoadingMore = true;
                    }
                    if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                        ((OrderView) views.get(3)).setAdapter(memberOrderVOList, 3);
                        ((OrderView) views.get(3)).setCanLoadingMore(canLoadingMore);

                    }
                    break;
                case Constants.OrderType.BUY_BACK:
                    if (StringTool.equals(MessageConstants.NEXT_PAGE_IS_EMPTY, objectID)) {
                        nextObjectIdBuyBack = MessageConstants.NEXT_PAGE_IS_EMPTY_SYMBOL;
                    } else {
                        nextObjectIdBuyBack = objectID;
                        canLoadingMore = true;
                    }
                    if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                        ((OrderView) views.get(4)).setAdapter(memberOrderVOList, 4);
                        ((OrderView) views.get(4)).setCanLoadingMore(canLoadingMore);

                    }
                    break;
                default:
                    break;
            }
        } else {
            //清空当前列表
            memberOrderVOList.clear();
            //判断当前的选中position，然后更新当前页面
            if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                ((OrderView) views.get(currentPosition)).setAdapter(memberOrderVOList, currentPosition);
                ((OrderView) views.get(currentPosition)).setCanLoadingMore(false);

            }
        }
    }

    @Override
    public void cancelOrderFailure(String info) {
        showToast(info, true);
    }

    @Override
    public void cancelOrderSuccess(MemberOrderVO memberOrderVO) {
        refreshView(3);
    }

    private LoadingDataListener loadingDataListener = new LoadingDataListener() {
        @Override
        public void onLoadingData() {
            refreshView(2);
        }
    };
}
