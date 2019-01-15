package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 「我的资产」数据适配
 */
public class MyFundDataAdapter extends RecyclerView.Adapter<MyFundDataAdapter.ViewHolder> {
    private String TAG = MyFundDataAdapter.class.getSimpleName();


    private Context context;
    private List<MemberKeyVO> memberKeyVOS;
    private OnItemSelectListener onItemSelectListener;

    public MyFundDataAdapter(Context context) {
        super();
        this.context = context;
        //取得当前的会员信息
        this.memberKeyVOS = BaseApplication.getMemberKeyVOList();
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
        if (i >= memberKeyVOS.size()) {
            return;
        }
        MemberKeyVO memberKeyVO = memberKeyVOS.get(i);
        if (memberKeyVO == null) {
            return;
        }
        CurrencyListVO currencyListVO = memberKeyVO.getCurrencyListVO();
        if (currencyListVO == null) {
            return;
        }
        viewHolder.tvFundType.setText(currencyListVO.getEnName());
        String balanceBlocked = memberKeyVO.getBalanceBlocked();
        viewHolder.tvFreeze.setText(StringTool.isEmpty(balanceBlocked) ? Constants.ValueMaps.DEFAULT_BALANCE : balanceBlocked);
        viewHolder.tvAvailable.setText(memberKeyVO.getBalanceAvailable());

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
                    onItemSelectListener.onItemSelect(memberKeyVO, Constants.From.RECHARGE);
                }
            }
        });
        viewHolder.cbWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.cbRecharge.setChecked(false);
                viewHolder.cbWithdraw.setChecked(true);
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(memberKeyVO, Constants.From.WITHDRAW);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(memberKeyVOS) ? 0 : memberKeyVOS.size();
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
