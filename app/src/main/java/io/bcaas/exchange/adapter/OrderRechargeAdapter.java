package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberOrderVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 订单页面的充值
 */
public class OrderRechargeAdapter extends RecyclerView.Adapter<OrderRechargeAdapter.ViewHolder> {

    private String TAG = OrderRechargeAdapter.class.getSimpleName();


    private Context context;
    private List<MemberOrderVO> memberOrderVOS;
    private OnItemSelectListener onItemSelectListener;

    public OrderRechargeAdapter(Context context, List<MemberOrderVO> memberOrderVOS) {
        super();
        this.context = context;
        this.memberOrderVOS = memberOrderVOS;
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
        if (i >= memberOrderVOS.size()) {
            return;
        }
        MemberOrderVO memberOrderVO = memberOrderVOS.get(i);
        if (memberOrderVO == null) {
            return;
        }
        CurrencyListVO currencyListVO = memberOrderVO.getCurrencyListVO();
        if (currencyListVO == null) {
            return;
        }
        String enName = currencyListVO.getEnName();
        viewHolder.tvRechargeType.setText(context.getResources().getString(R.string.recharge) + "  " + enName);
        viewHolder.tvRechargeTime.setText(memberOrderVO.getCreateTime());
        viewHolder.tvRechargeStatus.setText(String.valueOf(memberOrderVO.getStatus()));
        viewHolder.tvNumber.setText(memberOrderVO.getAmount() + "  " + enName);
        viewHolder.tvRechargeAddress.setText(memberOrderVO.getAddress());
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(memberOrderVOS) ? 0 : memberOrderVOS.size();
    }

    public void addList(List<MemberOrderVO> memberOrderVOS) {
        this.memberOrderVOS = memberOrderVOS;
        notifyDataSetChanged();
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
