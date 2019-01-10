package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * 订单页面的交易
 */
public class OrderTransactionAdapter extends RecyclerView.Adapter<OrderTransactionAdapter.ViewHolder> {

    private String TAG = OrderTransactionAdapter.class.getSimpleName();


    private Context context;
    private List<MemberOrderVO> memberOrderVOS;
    private OnItemSelectListener onItemSelectListener;

    public OrderTransactionAdapter(Context context, List<MemberOrderVO> memberOrderVOS) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_transaction_recycler, viewGroup, false);
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
        viewHolder.tvTransactionType.setText(String.valueOf(memberOrderVO.getType()));
        viewHolder.tvTransactionTime.setText(memberOrderVO.getCreateTime());
        viewHolder.tvTransactionStatus.setText(String.valueOf(memberOrderVO.getStatus()));
        viewHolder.tvPayAccount.setText(memberOrderVO.getAmount() + "\t" + paymentEnName);
        viewHolder.tvTotalAccount.setText(memberOrderVO.getAmount() + "\t" + enName);
        viewHolder.tvFee.setText(memberOrderVO.getHandlingFee() + "\t" + enName);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemSelectListener != null) {
//                    onItemSelectListener.onItemSelect(buyDataBean, MessageConstants.EMPTY);
//                }
//            }
//        });
        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(memberOrderVO.getMemberOrderUid(), Constants.From.ORDER_TRANSACTION);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(memberOrderVOS) ? 0 : memberOrderVOS.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTransactionType;
        TextView tvTransactionTime;
        TextView tvTransactionStatus;
        TextView tvPayAccount;
        TextView tvTotalAccount;
        TextView tvFee;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransactionType = itemView.findViewById(R.id.tv_transaction_type);
            tvTransactionTime = itemView.findViewById(R.id.tv_transaction_time);
            tvTransactionStatus = itemView.findViewById(R.id.tv_transaction_status);
            tvPayAccount = itemView.findViewById(R.id.tv_pay_account);
            tvTotalAccount = itemView.findViewById(R.id.tv_total_account);
            tvFee = itemView.findViewById(R.id.tv_fee);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
        }
    }
}
