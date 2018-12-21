package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.SettingsBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;

import java.util.List;


/**
 * @author catherine.brainwilliam
 * @since 2018/8/15
 * <p>
 * 「設置」頁面所有的選項數據填充顯示的適配器
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.viewHolder> {

    private Context context;
    private List<SettingsBean> settingTypes;

    private OnItemSelectListener settingItemSelectListener;

    public SettingsAdapter(Context context, List<SettingsBean> settingTypes) {
        this.context = context;
        this.settingTypes = settingTypes;
    }

    public void setSettingItemSelectListener(OnItemSelectListener settingItemSelectListener) {
        this.settingItemSelectListener = settingItemSelectListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_setting, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        if (ListTool.isEmpty(settingTypes)) {
            return;
        }
        final SettingsBean types = settingTypes.get(i);
        if (types == null) {
            return;
        }
        String type = types.getType();
        Constants.SettingType tag = types.getTag();
        viewHolder.vLine.setVisibility(View.VISIBLE);
        switch (tag) {
            case MY_ALL_FUND:
                viewHolder.ivIcon.setImageResource(R.mipmap.icon_check_wallet_info);
                break;
            case RECHARGE:
                break;
            case WITH_DRAW:
                break;
            case SAFETY_CENTER:
                viewHolder.vLine.setVisibility(View.INVISIBLE);
                break;
        }
        viewHolder.tvSettingType.setText(type);
        viewHolder.itemView.setOnClickListener(v -> settingItemSelectListener.onItemSelect(types, MessageConstants.EMPTY));

    }

    @Override
    public int getItemCount() {
        return settingTypes.size();
    }


    class viewHolder extends RecyclerView.ViewHolder {
        TextView tvSettingType;
        ImageButton ibDetail;
        ImageView ivIcon;
        RelativeLayout rlSettingTypes;
        View vLine;

        public viewHolder(View view) {
            super(view);
            vLine = view.findViewById(R.id.v_line);
            tvSettingType = view.findViewById(R.id.tv_setting_type);
            ibDetail = view.findViewById(R.id.ib_detail);
            rlSettingTypes = view.findViewById(R.id.rl_setting_types);
            ivIcon = view.findViewById(R.id.iv_icon);
        }
    }

}
