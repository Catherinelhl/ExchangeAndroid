package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import butterknife.Unbinder;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.BuyDataAdapter;
import io.bcaas.exchange.bean.BuyDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.activity.BuyDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 */
public class BuyView extends View {
    private String TAG = "BuyView";
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

    public BuyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_buy, null, true);
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

    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListenerTemp = onItemSelectListener;
    }

    public void refreshData(List<BuyDataBean> buyDataBeans) {
        buyDataAdapter = new BuyDataAdapter(this.context, buyDataBeans);
        buyDataAdapter.setOnItemSelectListener(onItemSelectListener);
        rvBuyData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
        rvBuyData.setLayoutManager(linearLayoutManager);
        rvBuyData.setAdapter(buyDataAdapter);
        buyDataAdapter.refreshData(buyDataBeans);
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            onItemSelectListenerTemp.onItemSelect(type, from);
        }
    };

}
