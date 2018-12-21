package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.OrderWithDrawBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 订单页面的提现
 */
public class OrderWithdrawAdapter extends RecyclerView.Adapter<OrderWithdrawAdapter.ViewHolder> {

    private String TAG = OrderWithdrawAdapter.class.getSimpleName();


    private Context context;
    private List<OrderWithDrawBean> orderWithDrawBeans;
    private OnItemSelectListener onItemSelectListener;

    public OrderWithdrawAdapter(Context context, List<OrderWithDrawBean> orderWithDrawBeans) {
        super();
        this.context = context;
        this.orderWithDrawBeans = orderWithDrawBeans;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_withdraw_cash_recycler, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i >= orderWithDrawBeans.size()) {
            return;
        }
        OrderWithDrawBean orderWithDrawBean = orderWithDrawBeans.get(i);
        if (orderWithDrawBean == null) {
            return;
        }
        viewHolder.tvWithdrawType.setText(orderWithDrawBean.getOrderType());
        viewHolder.tvWithdrawTime.setText(orderWithDrawBean.getOrderTime());
        viewHolder.tvWithdrawStatus.setText(orderWithDrawBean.getOrderStatus());
        viewHolder.tvPayAccount.setText(orderWithDrawBean.getNumber());
        viewHolder.tvWithdrawAddress.setText(orderWithDrawBean.getAddress());
        viewHolder.tvFee.setText(orderWithDrawBean.getFee());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(orderWithDrawBean, Constants.From.ORDER_WITHDRAW);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(orderWithDrawBeans) ? 0 : orderWithDrawBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWithdrawType;
        TextView tvWithdrawTime;
        TextView tvWithdrawStatus;
        TextView tvPayAccount;
        TextView tvFee;
        TextView tvWithdrawAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWithdrawType = itemView.findViewById(R.id.tv_withdraw_type);
            tvWithdrawTime = itemView.findViewById(R.id.tv_withdraw_time);
            tvWithdrawStatus = itemView.findViewById(R.id.tv_withdraw_status);
            tvPayAccount = itemView.findViewById(R.id.tv_pay_account);
            tvFee = itemView.findViewById(R.id.tv_fee);
            tvWithdrawAddress = itemView.findViewById(R.id.tv_withdraw_address);
        }
    }
}
