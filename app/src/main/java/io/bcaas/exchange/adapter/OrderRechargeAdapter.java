package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.OrderRechargeBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 订单页面的充值
 */
public class OrderRechargeAdapter extends RecyclerView.Adapter<OrderRechargeAdapter.ViewHolder> {

    private String TAG = OrderRechargeAdapter.class.getSimpleName();


    private Context context;
    private List<OrderRechargeBean> orderRechargeBeans;
    private OnItemSelectListener onItemSelectListener;

    public OrderRechargeAdapter(Context context, List<OrderRechargeBean> orderRechargeBeans) {
        super();
        this.context = context;
        this.orderRechargeBeans = orderRechargeBeans;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_recharge_recycler, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i >= orderRechargeBeans.size()) {
            return;
        }
        OrderRechargeBean orderRechargeBean = orderRechargeBeans.get(i);
        if (orderRechargeBean == null) {
            return;
        }
        viewHolder.tvRechargeType.setText("充值 BTC");
        viewHolder.tvRechargeTime.setText("2018/12/12");
        viewHolder.tvRechargeStatus.setText("失败");
        viewHolder.tvNumber.setText("234.23434254 BTC");
        viewHolder.tvRechargeAddress.setText("sadkfjnaskdjfnaksjdnfkasdjnkf");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(orderRechargeBean,  Constants.From.ORDER_RECHARGE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(orderRechargeBeans) ? 0 : orderRechargeBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRechargeType;
        TextView tvRechargeTime;
        TextView tvRechargeStatus;
        TextView tvNumber;
        TextView tvRechargeAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRechargeType = itemView.findViewById(R.id.tv_recharge_type);
            tvRechargeTime = itemView.findViewById(R.id.tv_recharge_time);
            tvRechargeStatus = itemView.findViewById(R.id.tv_recharge_status);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvRechargeAddress = itemView.findViewById(R.id.tv_recharge_address);
        }
    }
}
