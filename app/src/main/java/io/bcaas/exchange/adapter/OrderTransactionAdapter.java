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
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.vo.CurrencyListVO;
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

    public void addList(List<MemberOrderVO> memberOrderVOS) {
        this.memberOrderVOS = memberOrderVOS;
        notifyDataSetChanged();
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
        // 取得交易币种信息
        CurrencyListVO currencyListVO = memberOrderVO.getCurrencyListVO();
        if (currencyListVO == null) {
            return;
        }
        //取得需要支付方的币种信息
        CurrencyListVO paymentCurrencyList = memberOrderVO.getPaymentCurrencyUid();
        if (paymentCurrencyList == null) {
            return;
        }
        // 取得当前的订单类型
        //取得币种的支付token type,
        int type = memberOrderVO.getType();
        //是否是「购买」
        boolean isBuy = type == 2;
        // 取得卖出方币种的名字
        //如果当前的订单类型是「购买」，那么paymentCurrencyList就是自己；支出单位是取paymentCurrencyList，手续费单位是currencyListVO
        String enName = isBuy ? currencyListVO.getEnName() : paymentCurrencyList.getEnName();
        //取得支付方币种的名字
        //如果当前是「售出」，那么currencyListVO才是自己,支出单位取自己；手续费是paymentCurrencyList
        String paymentEnName = isBuy ? paymentCurrencyList.getEnName() : currencyListVO.getEnName();
        //取得fee币种类型,如果当前自己是「售出」，那么手续费单位等于支出方
        String feeEnName = isBuy ? enName : paymentEnName;
        //取得当前订单类型单位
        String orderTokenType = isBuy ? enName : paymentEnName;
        //订单产生类型
        viewHolder.tvOrderType.setText(StringTool.getDisplayOrderTypeText(memberOrderVO.getType()) +"  "+ orderTokenType);
        //订单产生时间
        viewHolder.tvOrderTime.setText(memberOrderVO.getCreateTime());
        //订单状态
        viewHolder.tvOrderStatus.setText(StringTool.getDisplayOrderStatusText(memberOrderVO.getType(), memberOrderVO.getStatus()));
        // 订单支出
        viewHolder.tvOutCome.setText(memberOrderVO.getExpenditure() + "  " + paymentEnName);
        // 订单收入
        viewHolder.tvInCome.setText(memberOrderVO.getIncome() + "  " + enName);
        //订单手续费
        viewHolder.tvFee.setText(memberOrderVO.getHandlingFee() + "  " + feeEnName);
        // 判断是否需要显示撤销的按钮；type ==1；status==2
        if (StringTool.getDisplayOrderStatus(memberOrderVO.getType(), memberOrderVO.getStatus())) {
            // 需要显示撤销订单按钮
            viewHolder.btnCancel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnCancel.setVisibility(View.GONE);
        }
        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(memberOrderVO.getMemberOrderUid(), Constants.From.ORDER_CANCEL_TRANSACTION);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(memberOrderVOS) ? 0 : memberOrderVOS.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderType;
        TextView tvOrderTime;
        TextView tvOrderStatus;
        TextView tvOutCome;
        TextView tvInCome;
        TextView tvFee;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderType = itemView.findViewById(R.id.tv_order_type);
            tvOrderTime = itemView.findViewById(R.id.tv_order_time);
            tvOrderStatus = itemView.findViewById(R.id.tv_order_status);
            tvOutCome = itemView.findViewById(R.id.tv_outcome);
            tvInCome = itemView.findViewById(R.id.tv_income);
            tvFee = itemView.findViewById(R.id.tv_fee);
            btnCancel = itemView.findViewById(R.id.btn_cancel);
        }
    }
}
