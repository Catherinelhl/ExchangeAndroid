package io.bcaas.exchange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.bean.CountryCodeBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.FilterListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.StringTool;

import java.util.ArrayList;
import java.util.List;


/**
 * @author catherine.brainwilliam
 * @since 2018/8/15
 * <p>
 * 數據適配器：「城市区号」：用於顯示已經存在的所有地址數據填充在PopWindow裡的適配器
 */
public class CountryCodeAdapter extends
        RecyclerView.Adapter<CountryCodeAdapter.viewHolder> implements Filterable {
    private String TAG = CountryCodeAdapter.class.getSimpleName();
    private Context context;
    private List<CountryCodeBean.CountryCode> countryCodes;
    private OnItemSelectListener onItemSelectListener;
    private FilterListener filterListener;

    private SearchFilter searchFilter;


    public CountryCodeAdapter(Context context, List<CountryCodeBean.CountryCode> list) {
        this.context = context;
        this.countryCodes = list;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
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
            viewHolder.tvContent.setText(context.getResources().getString(R.string.plus_sign) + code);
            viewHolder.tvName.setText(name);
            viewHolder.tvName.setOnClickListener(v -> onItemSelectListener.onItemSelect(countryCode, MessageConstants.EMPTY));
            viewHolder.llAddress.setOnClickListener(view -> onItemSelectListener.onItemSelect(countryCode, MessageConstants.EMPTY));
        }


    }

    @Override
    public int getItemCount() {
        return ListTool.isEmpty(countryCodes) ? 0 : countryCodes.size();
    }

    @Override
    public Filter getFilter() {
        if (searchFilter == null) {
            searchFilter = new SearchFilter(countryCodes);
        }
        return searchFilter;
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvContent;
        private LinearLayout llAddress;

        public viewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvContent = view.findViewById(R.id.tv_content);
            llAddress = view.findViewById(R.id.ll_list);
        }
    }

    class SearchFilter extends Filter {
        // 创建集合保存原始数据
        private List<CountryCodeBean.CountryCode> original;

        public SearchFilter(List<CountryCodeBean.CountryCode> countryCodes) {
            this.original = countryCodes;
        }

        /**
         * 该方法返回搜索过滤后的数据
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // 创建FilterResults对象
            FilterResults results = new FilterResults();
            /**
             * 没有搜索内容的话就还是给results赋值原始数据的值和大小
             * 执行了搜索的话，根据搜索的规则过滤即可，最后把过滤后的数据的值和大小赋值给results
             *
             */
            if (StringTool.isEmpty(constraint.toString())) {
                results.values = original;
                results.count = original.size();
            } else {
                // 创建集合保存过滤后的数据
                List<CountryCodeBean.CountryCode> listNew = new ArrayList<>();
                // 遍历原始数据集合，根据搜索的规则过滤数据
                for (CountryCodeBean.CountryCode countryCode : original) {
                    if (countryCode == null) {
                        results.values = original;
                        results.count = original.size();
                    } else {
                        //取得当前得城市名字
                        String countryName = countryCode.getCountryName();
                        // 取得当前得城市名字拼音「中文环境可用」
                        String countryNamePinyin = countryCode.getCountryPinyin();
                        //取得当前返回得数据的小写信息·
                        String constraintLowerCase = constraint.toString().trim().toLowerCase();
                        if (countryName.trim().toLowerCase().contains(constraintLowerCase)) {
                            // 规则匹配的话就往集合中添加该数据
                            listNew.add(countryCode);
                        } else if (StringTool.notEmpty(countryNamePinyin)) {
                            //  比较拼音
                            if (countryNamePinyin.trim().toLowerCase().contains(constraintLowerCase)) {
                                listNew.add(countryCode);
                            }
                        }
                    }

                }
                results.values = listNew;
                results.count = listNew.size();
            }


            // 返回FilterResults对象
            return results;
        }

        /**
         * 该方法用来刷新用户界面，根据过滤后的数据重新展示列表
         */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // 获取过滤后的数据
            countryCodes = (List<CountryCodeBean.CountryCode>) results.values;
            // 如果接口对象不为空，那么调用接口中的方法获取过滤后的数据，具体的实现在new这个接口的时候重写的方法里执行
            if (filterListener != null) {
                filterListener.getFilterData(countryCodes);
            }
            // 刷新数据源显示
            notifyDataSetChanged();
        }

    }


}
