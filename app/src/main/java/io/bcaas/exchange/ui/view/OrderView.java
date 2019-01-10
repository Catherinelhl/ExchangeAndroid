package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.OrderRechargeAdapter;
import io.bcaas.exchange.adapter.OrderTransactionAdapter;
import io.bcaas.exchange.adapter.OrderWithdrawAdapter;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.vo.MemberOrderVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「订单」页面视图
 */
public class OrderView extends LinearLayout {
    private String TAG = OrderView.class.getSimpleName();
    @BindView(R.id.rv_order_data)
    RecyclerView rvOrderData;
    @BindView(R.id.srl_order_data)
    SwipeRefreshLayout srlOrderData;
    private Context context;
    private OnItemSelectListener onItemSelectListenerTemp;

    //订单页面「交易」数据显示的适配器
    private OrderTransactionAdapter orderTransactionAdapter;
    //订单页面「充值」数据显示的适配器
    private OrderRechargeAdapter orderRechargeAdapter;
    //订单页面「提现」数据显示的适配器
    private OrderWithdrawAdapter orderWithdrawAdapter;

    public OrderView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public OrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_order, this, true);
        ButterKnife.bind(view);
        // 设置加载按钮的形态
        srlOrderData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color

        );
        srlOrderData.setSize(SwipeRefreshLayout.DEFAULT);
        srlOrderData.setOnRefreshListener(() -> {
            srlOrderData.setRefreshing(false);
        });
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListenerTemp = onItemSelectListener;
    }


    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            onItemSelectListenerTemp.onItemSelect(type, from);
        }
    };

    public void setOrderRechargeAdapter(List<MemberOrderVO> memberOrderVOS) {
        if (orderRechargeAdapter == null) {
            orderRechargeAdapter = new OrderRechargeAdapter(getContext(), memberOrderVOS);
            orderRechargeAdapter.setOnItemSelectListener(onItemSelectListener);
            rvOrderData.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
            rvOrderData.setLayoutManager(linearLayoutManager);
        }
        rvOrderData.setAdapter(orderRechargeAdapter);
    }

    public void setOrderWithDrawAdapter(List<MemberOrderVO> memberOrderVOS) {
        if (orderWithdrawAdapter == null) {
            orderWithdrawAdapter = new OrderWithdrawAdapter(getContext(), memberOrderVOS);
            orderWithdrawAdapter.setOnItemSelectListener(onItemSelectListener);
            rvOrderData.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
            rvOrderData.setLayoutManager(linearLayoutManager);
        }
        rvOrderData.setAdapter(orderWithdrawAdapter);
    }

    public void setOrderTransactionAdapter(List<MemberOrderVO> orderTransactionBeans) {
        if (orderTransactionAdapter == null) {
            orderTransactionAdapter = new OrderTransactionAdapter(getContext(), orderTransactionBeans);
            orderTransactionAdapter.setOnItemSelectListener(onItemSelectListener);
            rvOrderData.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
            rvOrderData.setLayoutManager(linearLayoutManager);
        }

        rvOrderData.setAdapter(orderTransactionAdapter);
    }
}
