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
 * 「提现」：页面数据适配
 */
public class OrderWithdrawAdapter extends RecyclerView.Adapter<OrderWithdrawAdapter.ViewHolder> {
    private Context context;
    private List<MemberOrderVO> memberOrderVOS;

    public OrderWithdrawAdapter(Context context, List<MemberOrderVO> memberOrderVOS) {
        super();
        this.context = context;
        this.memberOrderVOS = memberOrderVOS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_withdraw_cash_recycler, viewGroup, false);
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
        viewHolder.tvWithdrawType.setText(context.getResources().getString(R.string.with_draw) + "  " + enName);
        viewHolder.tvWithdrawTime.setText(DateFormatTool.timeZoneFormatUTCDate(memberOrderVO.getCreateTime()));
        viewHolder.tvWithdrawStatus.setText(StringTool.getDisplayOrderStatusText(memberOrderVO.getType(), memberOrderVO.getStatus()));
        viewHolder.tv_number.setText(StringTool.getDisplayAmountByUId(memberOrderVO.getAmount(), uid) + "  " + enName);
        viewHolder.tvFee.setText(StringTool.getDisplayAmountByUId(memberOrderVO.getHandlingFee(), uid) + "  " + enName);
        viewHolder.tvWithdrawAddress.setText(memberOrderVO.getAddress());
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
        TextView tvWithdrawType;
        TextView tvWithdrawTime;
        TextView tvWithdrawStatus;
        TextView tv_number;
        TextView tvFee;
        TextView tvWithdrawAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWithdrawType = itemView.findViewById(R.id.tv_withdraw_type);
            tvWithdrawTime = itemView.findViewById(R.id.tv_withdraw_time);
            tvWithdrawStatus = itemView.findViewById(R.id.tv_withdraw_status);
            tv_number = itemView.findViewById(R.id.tv_number);
            tvFee = itemView.findViewById(R.id.tv_fee);
            tvWithdrawAddress = itemView.findViewById(R.id.tv_withdraw_address);
        }
    }
}
