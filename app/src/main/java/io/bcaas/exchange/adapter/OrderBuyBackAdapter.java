package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.time.DateFormatTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberOrderVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 數據適配器：「回购」：页面数据适配
 */
public class OrderBuyBackAdapter extends RecyclerView.Adapter<OrderBuyBackAdapter.ViewHolder> {
    private String TAG = OrderBuyBackAdapter.class.getSimpleName();
    private Context context;
    private List<MemberOrderVO> memberOrderVOS;

    public OrderBuyBackAdapter(Context context, List<MemberOrderVO> memberOrderVOS) {
        super();
        this.context = context;
        this.memberOrderVOS = memberOrderVOS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_buy_back_recycler, viewGroup, false);
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
        viewHolder.tvRechargeType.setText(String.format(context.getString(R.string.double_s), context.getResources().getString(R.string.buy_back), enName));
        viewHolder.tvRechargeTime.setText(DateFormatTool.timeZoneFormatUTCDate(memberOrderVO.getCreateTime()));
        viewHolder.tvRechargeStatus.setText(StringTool.getDisplayOrderStatusText(memberOrderVO.getType(), memberOrderVO.getStatus()));
        viewHolder.tvNumber.setText(String.format(context.getString(R.string.double_s), StringTool.getDisplayAmountByUId(memberOrderVO.getAmount(), uid), enName));
        viewHolder.tvPayAmount.setText(String.format(context.getString(R.string.double_s), StringTool.getDisplayAmountByUId(memberOrderVO.getAmount(), uid), context.getResources().getString(R.string.yuan)));
        //取当下的支付银行账户名 银行名 银行账户
        String bankAccount = memberOrderVO.getBankAccount();
        String bankPersonalName = memberOrderVO.getBankPersonalName();
        StringBuffer sb = new StringBuffer();
        if (StringTool.notEmpty(bankPersonalName)) {
            sb.append(bankPersonalName)
                    .append(" ");
        }
        String bankName = memberOrderVO.getBankName();
        if (StringTool.notEmpty(bankName)) {
            sb.append(bankName)
                    .append(" ");
        }
        if (StringTool.notEmpty(bankAccount)) {
            int size = bankAccount.length();
            //截取前四位和后四位，然后中间用****表示
            String startFourString = bankAccount.substring(0, 4);
            String endFourString = bankAccount.substring(size - 4, size);
            sb.append(startFourString);
            sb.append(context.getResources().getString(R.string.four_star));
            sb.append(endFourString);
        }
        viewHolder.tvPayAccount.setText(sb);
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
        TextView tvPayAmount;
        TextView tvPayAccount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRechargeType = itemView.findViewById(R.id.tv_recharge_type);
            tvRechargeTime = itemView.findViewById(R.id.tv_recharge_time);
            tvRechargeStatus = itemView.findViewById(R.id.tv_recharge_status);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvPayAmount = itemView.findViewById(R.id.tv_pay_amount);
            tvPayAccount = itemView.findViewById(R.id.tv_pay_account);
        }
    }
}
