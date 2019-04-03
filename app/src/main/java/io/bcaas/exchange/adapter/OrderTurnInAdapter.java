package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.time.DateFormatTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberOrderVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 數據適配器：「转入」：数据适配器
 */
public class OrderTurnInAdapter extends RecyclerView.Adapter<OrderTurnInAdapter.ViewHolder> {

    private String TAG = OrderTurnInAdapter.class.getSimpleName();


    private Context context;
    private List<MemberOrderVO> memberOrderVOS;

    public OrderTurnInAdapter(Context context, List<MemberOrderVO> memberOrderVOS) {
        super();
        this.context = context;
        this.memberOrderVOS = memberOrderVOS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_turn_in_recycler, viewGroup, false);
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
        String uid = currencyListVO.getCurrencyUid();
        viewHolder.tvRechargeType.setText(String.format(context.getString(R.string.double_s), context.getResources().getString(R.string.turn_in), enName));
        viewHolder.tvRechargeTime.setText(DateFormatTool.timeZoneFormatUTCDate(memberOrderVO.getCreateTime()));
        viewHolder.tvRechargeStatus.setText(StringTool.getDisplayOrderStatusText(memberOrderVO.getType(), memberOrderVO.getStatus()));
        viewHolder.tvNumber.setText(String.format(context.getString(R.string.double_s), StringTool.getDisplayAmountByUId(memberOrderVO.getAmount(), uid), enName));
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
