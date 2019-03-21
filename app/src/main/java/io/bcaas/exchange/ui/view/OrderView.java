package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.OrderTurnInAdapter;
import io.bcaas.exchange.adapter.OrderTransactionAdapter;
import io.bcaas.exchange.adapter.OrderTurnOutAdapter;
import io.bcaas.exchange.base.BaseLinearLayout;
import io.bcaas.exchange.listener.LoadingDataListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.vo.MemberOrderVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「订单」页面视图
 */
public class OrderView extends BaseLinearLayout {
    private String TAG = OrderView.class.getSimpleName();

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rl_no_data)
    RelativeLayout rlNoData;
    @BindView(R.id.rv_order_data)
    RecyclerView rvOrderData;
    private boolean canLoadingMore;//得到当前是否可以继续加载数据

    private OnItemSelectListener onItemSelectListenerTemp;

    //订单页面「交易」数据显示的适配器
    private OrderTransactionAdapter orderTransactionAdapter;
    //订单页面「转入」数据显示的适配器
    private OrderTurnInAdapter orderTurnInAdapter;
    //订单页面「转出」数据显示的适配器
    private OrderTurnOutAdapter orderTurnOutAdapter;
    //上拉加载的回调
    private LoadingDataListener loadingDataListener;
    //记录当前显示的页面
    private int currentPosition;

    public OrderView(Context context) {
        super(context);
    }

    @Override
    protected int setContentView() {
        return R.layout.view_order;
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initListener() {

    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListenerTemp = onItemSelectListener;
    }

    public void setLoadingDataListener(LoadingDataListener loadingDataListener) {
        this.loadingDataListener = loadingDataListener;
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            onItemSelectListenerTemp.onItemSelect(type, from);
        }
    };

    public void setCanLoadingMore(boolean canLoadingMore) {
        this.canLoadingMore = canLoadingMore;
    }

    public void setAdapter(List<MemberOrderVO> memberOrderVOS, int currentPosition) {
        this.currentPosition = currentPosition;
        switch (currentPosition) {
            case 0://交易
                if (ListTool.isEmpty(memberOrderVOS)) {
                    //显示没有信息页面
                    rlNoData.setVisibility(VISIBLE);
                } else {
                    rlNoData.setVisibility(GONE);
                    if (orderTransactionAdapter == null) {
                        orderTransactionAdapter = new OrderTransactionAdapter(getContext(), memberOrderVOS);
                        orderTransactionAdapter.setOnItemSelectListener(onItemSelectListener);
                        rvOrderData.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
                        rvOrderData.setLayoutManager(linearLayoutManager);
                    } else {
                        orderTransactionAdapter.addList(memberOrderVOS);
                    }
                    rvOrderData.setAdapter(orderTransactionAdapter);
                    rvOrderData.addOnScrollListener(scrollListener);
                }

                break;
            case 1://转入
                if (ListTool.isEmpty(memberOrderVOS)) {
                    //显示没有信息页面
                    rlNoData.setVisibility(VISIBLE);
                } else {
                    rlNoData.setVisibility(GONE);
                    if (orderTurnInAdapter == null) {
                        orderTurnInAdapter = new OrderTurnInAdapter(getContext(), memberOrderVOS);
                        rvOrderData.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
                        rvOrderData.setLayoutManager(linearLayoutManager);
                    } else {
                        orderTurnInAdapter.addList(memberOrderVOS);
                    }
                    rvOrderData.setAdapter(orderTurnInAdapter);
                    rvOrderData.addOnScrollListener(scrollListener);
                }
                break;
            case 2:// 提現
                if (ListTool.isEmpty(memberOrderVOS)) {
                    //显示没有信息页面
                    rlNoData.setVisibility(VISIBLE);
                } else {
                    rlNoData.setVisibility(GONE);
                    if (orderTurnOutAdapter == null) {
                        orderTurnOutAdapter = new OrderTurnOutAdapter(getContext(), memberOrderVOS);
                        rvOrderData.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
                        rvOrderData.setLayoutManager(linearLayoutManager);
                    } else {
                        orderTurnOutAdapter.addList(memberOrderVOS);
                    }
                    rvOrderData.setAdapter(orderTurnOutAdapter);
                    rvOrderData.addOnScrollListener(scrollListener);
                }
                break;

        }
    }


    private int mLastVisibleItemPosition;
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            switch (currentPosition) {
                case 0://交易
                    if (orderTransactionAdapter != null) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && mLastVisibleItemPosition + 1 == orderTransactionAdapter.getItemCount()) {
                            LogTool.d(TAG, "canLoadingMore is:" + canLoadingMore);
                            //发送网络请求获取更多数据
                            if (canLoadingMore) {
                                //通知「购买」的主界面还是继续加载数据
                                if (loadingDataListener != null) {
                                    loadingDataListener.onLoadingData();
                                }
                            }
                        }
                    }
                    break;
                case 1://转入
                    if (orderTurnInAdapter != null) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && mLastVisibleItemPosition + 1 == orderTurnInAdapter.getItemCount()) {
                            LogTool.d(TAG, "canLoadingMore is:" + canLoadingMore);
                            //发送网络请求获取更多数据
                            if (canLoadingMore) {
                                //通知「购买」的主界面还是继续加载数据
                                if (loadingDataListener != null) {
                                    loadingDataListener.onLoadingData();
                                }
                            }
                        }
                    }
                    break;
                case 2:// 提現
                    if (orderTurnOutAdapter != null) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE
                                && mLastVisibleItemPosition + 1 == orderTurnOutAdapter.getItemCount()) {
                            LogTool.d(TAG, "canLoadingMore is:" + canLoadingMore);
                            //发送网络请求获取更多数据
                            if (canLoadingMore) {
                                //通知「购买」的主界面还是继续加载数据
                                if (loadingDataListener != null) {
                                    loadingDataListener.onLoadingData();
                                }
                            }
                        }
                    }
                    break;

            }
        }

    };
}
