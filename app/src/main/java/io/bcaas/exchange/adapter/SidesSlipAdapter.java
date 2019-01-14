package io.bcaas.exchange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import io.bcaas.exchange.R;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.device.DensityTool;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/4
 * 侧滑栏数据适配器
 */
public class SidesSlipAdapter extends BaseAdapter {
    private Context context;
    private List<MemberKeyVO> memberKeyVOList;
    private OnItemSelectListener onItemSelectListener;
    private int currentPosition = -1;

    public SidesSlipAdapter(Context c) {
        context = c;
    }

    public void addList(List<MemberKeyVO> memberKeyVOList) {
        this.memberKeyVOList = memberKeyVOList;
        notifyDataSetChanged();
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public int getCount() {
        return ListTool.isEmpty(memberKeyVOList) ? 0 : memberKeyVOList.size();
    }

    public MemberKeyVO getItem(int position) {
        return memberKeyVOList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            button = new Button(context);
            button.setLayoutParams(new ViewGroup.LayoutParams(DensityTool.dip2px(context, 70), DensityTool.dip2px(context, 40)));
//            button.setPadding(8, 8, 8, 8);
        } else {
            button = (Button) convertView;
        }
        button.setBackgroundResource(getBg(position));
        button.setTextColor(getColor(position));
        MemberKeyVO memberKeyVO = memberKeyVOList.get(position);
        if (memberKeyVO != null) {
            CurrencyListVO currencyListVO = memberKeyVO.getCurrencyListVO();
            if (currencyListVO != null) {
                button.setText(currencyListVO.getEnName());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPosition = position;
                        notifyDataSetChanged();
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onItemSelect(memberKeyVO, Constants.From.SIDE_SLIP);
                        }
                    }
                });
                return button;
            }

        }
        return null;

    }

    private int getBg(int position) {
        if (position == currentPosition) {
            return R.drawable.solid_border_red;
        }
        return R.drawable.solid_border_grey;
    }

    private int getColor(int position) {
        if (position == currentPosition) {
            return context.getResources().getColor(R.color.white);
        }
        return context.getResources().getColor(R.color.grey_999999);
    }

    /**
     * 重置当前数据
     */
    public void resetData() {
        currentPosition = -1;
        notifyDataSetChanged();
    }
}
