package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.BuyDataBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 订单页面的交易
 */
public class OrderTransactionAdapter extends RecyclerView.Adapter<OrderTransactionAdapter.ViewHolder> {

    private String TAG = OrderTransactionAdapter.class.getSimpleName();


    private Context context;
    private List<BuyDataBean> buyDataBeans;
    private OnItemSelectListener onItemSelectListener;

    public OrderTransactionAdapter(Context context, List<BuyDataBean> buyDataBeans) {
        super();
        this.context = context;
        this.buyDataBeans = buyDataBeans;
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
        if (i >= buyDataBeans.size()) {
            return;
        }
        BuyDataBean buyDataBean = buyDataBeans.get(i);
        if (buyDataBean == null) {
            return;
        }
        viewHolder.tvTransactionType.setText("出售 BTC");
        viewHolder.tvTransactionTime.setText("2018/11/26");
        viewHolder.tvTransactionStatus.setText("待出售");
        viewHolder.tvPayAccount.setText(buyDataBean.getPrice());
        viewHolder.tvTotalAccount.setText(buyDataBean.getNumber());
        viewHolder.tvFee.setText(buyDataBean.getFee());
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
                    onItemSelectListener.onItemSelect(buyDataBean, MessageConstants.EMPTY);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(buyDataBeans) ? 0 : buyDataBeans.size();
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
