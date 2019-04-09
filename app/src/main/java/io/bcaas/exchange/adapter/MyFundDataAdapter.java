package io.bcaas.exchange.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.file.ResourceTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * 數據適配器：「我的资产」：数据适配
 */
public class MyFundDataAdapter extends RecyclerView.Adapter<MyFundDataAdapter.ViewHolder> {
    private String TAG = MyFundDataAdapter.class.getSimpleName();


    private Context context;
    private List<MemberKeyVO> memberKeyVOS;
    private OnItemSelectListener onItemSelectListener;

    public MyFundDataAdapter(Context context) {
        super();
        this.context = context;
    }

    /**
     * 刷新当前adapter的数据
     */
    public void refreshData(List<MemberKeyVO> memberKeyVOS) {
        this.memberKeyVOS = memberKeyVOS;
        notifyDataSetChanged();
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
        String uid = currencyListVO.getCurrencyUid();
        //判断当前的UID是否是3，七彩贝
        if (StringTool.equals(uid, Constants.CurrencyUID.CNYC)) {
            //如果是七彩贝，需要将按钮置换成回购和充值
            viewHolder.btnRecharge.setText(context.getResources().getString(R.string.recharge));
            viewHolder.btnWithdraw.setText(context.getResources().getString(R.string.buy_back));
        }
            viewHolder.tvFundType.setText(currencyListVO.getEnName());
        Drawable drawable = context.getResources().getDrawable(ResourceTool.getDrawableByEnName(currencyListVO.getEnName()));
        // 设置币种图标
        int width = context.getResources().getDimensionPixelOffset(R.dimen.d16);
        drawable.setBounds(0, 0, width, width);
        viewHolder.tvFundType.setCompoundDrawables(drawable, null, null, null);

        String balanceBlocked = memberKeyVO.getBalanceBlocked();
        viewHolder.tvFreeze.setText(StringTool.getDisplayAmountByUId(StringTool.isEmpty(balanceBlocked) ? Constants.ValueMaps.DEFAULT_BALANCE : balanceBlocked, uid));
        viewHolder.tvAvailable.setText(StringTool.getDisplayAmountByUId(memberKeyVO.getBalanceAvailable(), uid));

        viewHolder.itemView.setOnClickListener(v -> {
        });
        viewHolder.btnRecharge.setOnClickListener(v -> {
            if (onItemSelectListener != null) {
                onItemSelectListener.onItemSelect(memberKeyVO, Constants.From.TURN_IN);
            }
        });
        viewHolder.btnWithdraw.setOnClickListener(v -> {
            if (onItemSelectListener != null) {
                onItemSelectListener.onItemSelect(memberKeyVO, Constants.From.TURN_OUT);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(memberKeyVOS) ? 0 : memberKeyVOS.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFundType;
        Button btnWithdraw;
        Button btnRecharge;
        TextView tvAvailable;
        TextView tvFreeze;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFundType = itemView.findViewById(R.id.tv_fund_type);
            btnWithdraw = itemView.findViewById(R.id.btn_turn_out);
            btnRecharge = itemView.findViewById(R.id.btn_turn_in);
            tvAvailable = itemView.findViewById(R.id.tv_available);
            tvFreeze = itemView.findViewById(R.id.tv_freeze);
        }
    }
}
