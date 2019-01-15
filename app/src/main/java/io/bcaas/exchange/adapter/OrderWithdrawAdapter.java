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
import io.bcaas.exchange.vo.MemberOrderVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 订单页面的提现
 */
public class OrderWithdrawAdapter extends RecyclerView.Adapter<OrderWithdrawAdapter.ViewHolder> {

    private String TAG = OrderWithdrawAdapter.class.getSimpleName();


    private Context context;
    private List<MemberOrderVO> memberOrderVOS;
    private OnItemSelectListener onItemSelectListener;

    public OrderWithdrawAdapter(Context context, List<MemberOrderVO> memberOrderVOS) {
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
        String enName = memberOrderVO.getCurrencyListVO().getEnName();
        String paymentEnName = memberOrderVO.getPaymentCurrencyUid().getEnName();
        viewHolder.tvWithdrawType.setText(String.valueOf(memberOrderVO.getType()));
        viewHolder.tvWithdrawTime.setText(memberOrderVO.getCreateTime());
        viewHolder.tvWithdrawStatus.setText(String.valueOf(memberOrderVO.getStatus()));
        viewHolder.tvPayAccount.setText(memberOrderVO.getPrice() + "  " + paymentEnName);
        viewHolder.tvFee.setText(memberOrderVO.getHandlingFee() + "  " + enName);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(memberOrderVO.getMemberOrderUid(), Constants.From.ORDER_WITHDRAW);
                }
            }
        });
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
