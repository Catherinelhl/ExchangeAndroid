package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.List;


/**
 * @author catherine.brainwilliam
 * @since 2018/8/15
 * <p>
 * 數據適配器：「PopWindow 列表」：用於顯示需要显示在PopWindow裡的適配器
 */
public class PopListAdapter extends
        RecyclerView.Adapter<PopListAdapter.viewHolder> {
    private String TAG = PopListAdapter.class.getSimpleName();
    private Context context;
    private List<MemberKeyVO> memberKeyVOS;
    private OnItemSelectListener onItemSelectListener;


    public PopListAdapter(Context context, List<MemberKeyVO> memberKeyVOS) {
        this.context = context;
        this.memberKeyVOS = memberKeyVOS;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_pop, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        if (memberKeyVOS == null) {
            return;
        }
        MemberKeyVO memberKeyVO = memberKeyVOS.get(i);
        if (memberKeyVO != null) {
            CurrencyListVO currencyListVO = memberKeyVO.getCurrencyListVO();
            if (currencyListVO != null) {
                String enName = currencyListVO.getEnName();
                if (StringTool.isEmpty(enName)) {
                    return;
                }
                viewHolder.tvName.setText(enName);
                viewHolder.tvName.setOnClickListener(v -> onItemSelectListener.onItemSelect(memberKeyVO, Constants.From.SELL_SELECTED_CURRENCY));
                viewHolder.llAddress.setOnClickListener(view -> onItemSelectListener.onItemSelect(memberKeyVO, Constants.From.SELL_SELECTED_CURRENCY));

            }
        }


    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(memberKeyVOS) ? 0 : memberKeyVOS.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private LinearLayout llAddress;

        public viewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            llAddress = view.findViewById(R.id.ll_list);
        }
    }


}
