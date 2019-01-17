package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.OrderRechargeAdapter;
import io.bcaas.exchange.adapter.OrderTransactionAdapter;
import io.bcaas.exchange.adapter.OrderWithdrawAdapter;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
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

    private OnItemSelectListener onItemSelectListenerTemp;

    //订单页面「交易」数据显示的适配器
    private OrderTransactionAdapter orderTransactionAdapter;
    //订单页面「充值」数据显示的适配器
    private OrderRechargeAdapter orderRechargeAdapter;
    //订单页面「提现」数据显示的适配器
    private OrderWithdrawAdapter orderWithdrawAdapter;

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


    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            onItemSelectListenerTemp.onItemSelect(type, from);
        }
    };

    public void setAdapter(List<MemberOrderVO> memberOrderVOS, int currentPosition) {
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
                }

                break;
            case 1://充值
                if (ListTool.isEmpty(memberOrderVOS)) {
                    //显示没有信息页面
                    rlNoData.setVisibility(VISIBLE);
                } else {
                    rlNoData.setVisibility(GONE);
                    if (orderRechargeAdapter == null) {
                        orderRechargeAdapter = new OrderRechargeAdapter(getContext(), memberOrderVOS);
                        orderRechargeAdapter.setOnItemSelectListener(onItemSelectListener);
                        rvOrderData.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
                        rvOrderData.setLayoutManager(linearLayoutManager);
                    } else {
                        orderRechargeAdapter.addList(memberOrderVOS);
                    }
                    rvOrderData.setAdapter(orderRechargeAdapter);
                }
                break;
            case 2:// 提現
                if (ListTool.isEmpty(memberOrderVOS)) {
                    //显示没有信息页面
                    rlNoData.setVisibility(VISIBLE);
                } else {
                    rlNoData.setVisibility(GONE);
                    if (orderWithdrawAdapter == null) {
                        orderWithdrawAdapter = new OrderWithdrawAdapter(getContext(), memberOrderVOS);
                        orderWithdrawAdapter.setOnItemSelectListener(onItemSelectListener);
                        rvOrderData.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
                        rvOrderData.setLayoutManager(linearLayoutManager);
                    } else {
                        orderWithdrawAdapter.addList(memberOrderVOS);
                    }
                    rvOrderData.setAdapter(orderWithdrawAdapter);
                }
                break;

        }
    }
}
