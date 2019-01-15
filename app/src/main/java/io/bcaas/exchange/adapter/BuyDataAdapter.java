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
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.MemberVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 */
public class BuyDataAdapter extends RecyclerView.Adapter<BuyDataAdapter.ViewHolder> {
    private String TAG = BuyDataAdapter.class.getSimpleName();


    private Context context;
    private List<MemberOrderVO> memberOrderVOS;
    private OnItemSelectListener onItemSelectListener;

    public BuyDataAdapter(Context context) {
        super();
        this.context = context;
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
        if (i >= memberOrderVOS.size()) {
            return;
        }
        MemberOrderVO memberOrderVO = memberOrderVOS.get(i);
        if (memberOrderVO == null) {
            return;
        }
        //得到出售人的名字
        MemberVO memberVO = memberOrderVO.getMemberVO();
        if (memberVO != null) {
            viewHolder.tvPersonName.setText(memberVO.getMemberId());
        }
        // 得到需要支付的信息
        CurrencyListVO paymentCurrencyList = memberOrderVO.getPaymentCurrencyUid();
        if (paymentCurrencyList != null) {
            String enName = paymentCurrencyList.getEnName();
            viewHolder.tvPayMethod.setText("支付方式" + "  " + enName);
            viewHolder.tvFee.setText(paymentCurrencyList.getBuyCharge() + "  " + enName);
            viewHolder.tvPrice.setText(memberOrderVO.getUnitPrice() + "  " + enName);
            viewHolder.tvTotalAccount.setText(memberOrderVO.getPrice() + "  " + enName);


        }
        // 得到当前币种信息
        CurrencyListVO currencyListVO = memberOrderVO.getCurrencyListVO();
        if (currencyListVO != null) {
            viewHolder.tvNumber.setText(memberOrderVO.getAmount() + "  " + currencyListVO.getEnName());
        }
        viewHolder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(memberOrderVO, MessageConstants.EMPTY);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(memberOrderVOS) ? 0 : memberOrderVOS.size();
    }

    public void refreshData(List<MemberOrderVO> memberOrderVOS) {
        this.memberOrderVOS = memberOrderVOS;
        notifyDataSetChanged();
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
            tvPersonName = itemView.findViewById(R.id.tv_person_name);
            tvPayMethod = itemView.findViewById(R.id.tv_pay_method);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvTotalAccount = itemView.findViewById(R.id.tv_total_account);
            tvFee = itemView.findViewById(R.id.tv_fee);
            btnBuy = itemView.findViewById(R.id.btn_buy);
        }
    }
}
