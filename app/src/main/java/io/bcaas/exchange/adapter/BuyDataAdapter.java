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
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.BuyDataBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 */
public class BuyDataAdapter extends RecyclerView.Adapter<BuyDataAdapter.ViewHolder> {
    private String TAG = BuyDataAdapter.class.getSimpleName();


    private Context context;
    private List<BuyDataBean> buyDataBeans;
    private OnItemSelectListener onItemSelectListener;

    public BuyDataAdapter(Context context, List<BuyDataBean> buyDataBeans) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_buy_recycler, viewGroup, false);
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
        viewHolder.tvPersonName.setText(buyDataBean.getPersonName());
        viewHolder. tvPayMethod.setText(buyDataBean.getBuyMethod());
        viewHolder. tvPrice.setText(buyDataBean.getPrice());
        viewHolder.tvNumber.setText(buyDataBean.getNumber());
        viewHolder.tvTotalAccount.setText(buyDataBean.getTotalAccount());
        viewHolder.tvFee.setText(buyDataBean.getFee());
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onItemSelectListener != null) {
//                    onItemSelectListener.onItemSelect(buyDataBean, MessageConstants.EMPTY);
//                }
//            }
//        });
        viewHolder.btnBuy.setOnClickListener(new View.OnClickListener() {
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
        TextView tvPersonName;
        TextView tvPayMethod;
        TextView tvPrice;
        TextView tvNumber;
        TextView tvTotalAccount;
        TextView tvFee;
        Button btnBuy;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPersonName=itemView.findViewById(R.id.tv_person_name);
            tvPayMethod=itemView.findViewById(R.id.tv_pay_method);
            tvPrice=itemView.findViewById(R.id.tv_price);
            tvNumber=itemView.findViewById(R.id.tv_number);
            tvTotalAccount=itemView.findViewById(R.id.tv_total_account);
            tvFee=itemView.findViewById(R.id.tv_fee);
            btnBuy=itemView.findViewById(R.id.btn_buy);
        }
    }
}
