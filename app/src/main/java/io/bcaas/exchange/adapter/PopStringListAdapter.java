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

import java.util.List;


/**
 * @author catherine.brainwilliam
 * @since 2018/8/15
 * <p>
 * 數據適配器：「PopWindow 列表」：用於顯示需要显示在PopWindow裡的適配器,这里
 */
public class PopStringListAdapter extends
        RecyclerView.Adapter<PopStringListAdapter.viewHolder> {
    private String TAG = PopStringListAdapter.class.getSimpleName();
    private Context context;
    private List<String> strings;
    private OnItemSelectListener onItemSelectListener;


    public PopStringListAdapter(Context context, List<String> strings) {
        this.context = context;
        this.strings = strings;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_string_list_pop, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        if (strings == null) {
            return;
        }
        String string = strings.get(i);
        if (StringTool.isEmpty(string)) {
            return;
        }
        viewHolder.tvName.setText(string);
        viewHolder.tvName.setOnClickListener(v -> onItemSelectListener.onItemSelect(string, Constants.From.SELL_SELECTED_CURRENCY));
        viewHolder.llList.setOnClickListener(view -> onItemSelectListener.onItemSelect(string, Constants.From.SELL_SELECTED_CURRENCY));


    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(strings) ? 0 : strings.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private LinearLayout llList;

        public viewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            llList = view.findViewById(R.id.ll_list);
        }
    }


}
