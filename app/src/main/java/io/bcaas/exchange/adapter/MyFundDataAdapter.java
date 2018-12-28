package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.MyFundBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 「我的资产」数据适配
 */
public class MyFundDataAdapter extends RecyclerView.Adapter<MyFundDataAdapter.ViewHolder> {
    private String TAG = MyFundDataAdapter.class.getSimpleName();


    private Context context;
    private List<MyFundBean> myFundBeans;
    private OnItemSelectListener onItemSelectListener;

    public MyFundDataAdapter(Context context, List<MyFundBean> myFundBeans) {
        super();
        this.context = context;
        this.myFundBeans = myFundBeans;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_fund_recycler, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i >= myFundBeans.size()) {
            return;
        }
        MyFundBean myFundBean = myFundBeans.get(i);
        if (myFundBean == null) {
            return;
        }
        viewHolder.tvFundType.setText(myFundBean.getFundType());
        viewHolder.tvFreeze.setText(myFundBean.getFreeze());
        viewHolder.tvAvailable.setText(myFundBean.getAvailable());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        viewHolder.cbRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.cbWithdraw.setChecked(false);
                viewHolder.cbRecharge.setChecked(true);
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(myFundBean, MessageConstants.RECHARGE);
                }
            }
        });
        viewHolder.cbWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.cbRecharge.setChecked(false);
                viewHolder.cbWithdraw.setChecked(true);
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(myFundBean, MessageConstants.WITHDRAW);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(myFundBeans) ? 0 : myFundBeans.size();
    }

    public void refreshData(List<MyFundBean> myFundBeans) {
        this.myFundBeans = myFundBeans;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFundType;
        CheckBox cbWithdraw;
        CheckBox cbRecharge;
        TextView tvAvailable;
        TextView tvFreeze;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFundType = itemView.findViewById(R.id.tv_fund_type);
            cbWithdraw = itemView.findViewById(R.id.cb_withdraw);
            cbRecharge = itemView.findViewById(R.id.cb_recharge);
            tvAvailable = itemView.findViewById(R.id.tv_available);
            tvFreeze = itemView.findViewById(R.id.tv_freeze);
        }
    }
}
