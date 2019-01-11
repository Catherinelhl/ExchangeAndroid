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
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.CountryCodeBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.TextTool;

import java.util.List;


/**
 * @author catherine.brainwilliam
 * @since 2018/8/15
 * <p>
 * 用於顯示已經存在的所有地址數據填充在PopWindow裡的適配器
 */
public class CountryCodeAdapter extends
        RecyclerView.Adapter<CountryCodeAdapter.viewHolder> {
    private String TAG = CountryCodeAdapter.class.getSimpleName();
    private Context context;
    private List<CountryCodeBean.CountryCode> countryCodes;
    private OnItemSelectListener onItemSelectListener;


    public CountryCodeAdapter(Context context, List<CountryCodeBean.CountryCode> list) {
        this.context = context;
        this.countryCodes = list;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country_code, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        if (countryCodes == null) {
            return;
        }
        CountryCodeBean.CountryCode countryCode = countryCodes.get(i);
        if (countryCode != null) {
            String name = countryCode.getCountryName();
            String code = countryCode.getPhoneCode();
            if (StringTool.isEmpty(name) || StringTool.isEmpty(code)) {
                return;
            }
            //1:获取屏幕的宽度
            int screenWidth = BaseApplication.getScreenWidth();
            int nameWidth = (screenWidth - context.getResources().getDimensionPixelOffset(R.dimen.d5)) / 3;
            viewHolder.tvContent.setText("+" + code);
            viewHolder.tvName.setText(name)
            ;
            viewHolder.tvName.setOnClickListener(v -> onItemSelectListener.onItemSelect(countryCode, MessageConstants.EMPTY));
            viewHolder.llAddress.setOnClickListener(view -> onItemSelectListener.onItemSelect(countryCode, MessageConstants.EMPTY));
        }


    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(countryCodes) ? 0 : countryCodes.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvContent;
        private LinearLayout llAddress;

        public viewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvContent = view.findViewById(R.id.tv_content);
            llAddress = view.findViewById(R.id.ll_country_code);
        }
    }


}
