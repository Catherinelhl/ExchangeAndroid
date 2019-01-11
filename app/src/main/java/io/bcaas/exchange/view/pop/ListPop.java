package io.bcaas.exchange.view.pop;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.CountryCodeAdapter;
import io.bcaas.exchange.adapter.PopListAdapter;
import io.bcaas.exchange.bean.CountryCodeBean;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/30
 * <p>
 * 自定義PopWindow：显示「帳戶地址」列表
 */
public class ListPop extends PopupWindow {

    private View popWindow;
    private RecyclerView recyclerView;//显示当前列表
    private OnItemSelectListener itemSelectListener;
    private Context context;

    public ListPop(Context context) {
        super(context);
        this.context = context;
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popWindow = inflater.inflate(R.layout.pop_show_list, null);
        setContentView(popWindow);
        recyclerView = popWindow.findViewById(R.id.rv_list);
    }

    public void addList(OnItemSelectListener onItemSelectListener, List<MemberKeyVO> list) {
        this.itemSelectListener = onItemSelectListener;
        PopListAdapter adapter = new PopListAdapter(context, list);
        adapter.setOnItemSelectListener(popItemSelectListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false));

    }

    private OnItemSelectListener popItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            dismiss();
            itemSelectListener.onItemSelect(type, from);
        }
    };
}
