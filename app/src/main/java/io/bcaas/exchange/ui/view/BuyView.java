package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.BuyDataAdapter;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberOrderVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「购买」页面视图
 */
public class BuyView extends LinearLayout {
    private String TAG = BuyView.class.getSimpleName();
    RecyclerView rvBuyData;
    SwipeRefreshLayout srlBuyData;
    private Context context;
    private BuyDataAdapter buyDataAdapter;
    private OnItemSelectListener onItemSelectListenerTemp;

    public BuyView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_buy, this, true);
        rvBuyData = view.findViewById(R.id.rv_buy_data);
        srlBuyData = view.findViewById(R.id.srl_buy_data);
        // 设置加载按钮的形态
        srlBuyData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color

        );
        srlBuyData.setSize(SwipeRefreshLayout.DEFAULT);
        srlBuyData.setOnRefreshListener(() -> {
            srlBuyData.setRefreshing(false);
            //判断如果当前没有币种，那么就暂时不能刷新数据
//            if (StringTool.isEmpty(Bas.getBlockService())) {
//                return;
//            }
//            onRefreshTransactionRecord("swipeRefreshLayout");
        });
        initAdapter();
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListenerTemp = onItemSelectListener;
    }

    private void initAdapter() {
        buyDataAdapter = new BuyDataAdapter(this.context);
        buyDataAdapter.setOnItemSelectListener(onItemSelectListener);
        rvBuyData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
        rvBuyData.setLayoutManager(linearLayoutManager);
        rvBuyData.setAdapter(buyDataAdapter);
    }

    public void refreshData(List<MemberOrderVO> memberOrderVOS) {
        if (buyDataAdapter != null) {
            buyDataAdapter.refreshData(memberOrderVOS);
        }
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            onItemSelectListenerTemp.onItemSelect(type, from);
        }
    };

}
