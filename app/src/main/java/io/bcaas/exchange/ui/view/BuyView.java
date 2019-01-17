package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.BuyDataAdapter;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.vo.MemberOrderVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「购买」页面视图
 */
public class BuyView extends BaseLinearLayout {
    @BindView(R.id.rv_buy_data)
    RecyclerView rvBuyData;
    @BindView(R.id.srl_buy_data)
    SwipeRefreshLayout srlBuyData;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rl_no_data)
    RelativeLayout rlNoData;
    private String TAG = BuyView.class.getSimpleName();
    private BuyDataAdapter buyDataAdapter;
    private OnItemSelectListener onItemSelectListenerTemp;

    public BuyView(Context context) {
        super(context);
    }

    @Override
    protected int setContentView() {
        return R.layout.view_buy;
    }

    @Override
    protected void initView() {
        // 设置加载按钮的形态
        srlBuyData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color

        );
        srlBuyData.setSize(SwipeRefreshLayout.DEFAULT);
        initAdapter();
    }

    @Override
    protected void initListener() {
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

    private void initAdapter() {
        buyDataAdapter = new BuyDataAdapter(this.context);
        buyDataAdapter.setOnItemSelectListener(onItemSelectListener);
        rvBuyData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
        rvBuyData.setLayoutManager(linearLayoutManager);
        rvBuyData.setAdapter(buyDataAdapter);
    }

    public void refreshData(List<MemberOrderVO> memberOrderVOS) {
        if (ListTool.noEmpty(memberOrderVOS)) {
            rlNoData.setVisibility(GONE);
            if (buyDataAdapter != null) {
                buyDataAdapter.refreshData(memberOrderVOS);
            }
        } else {
            //显示没有信息页面
            rlNoData.setVisibility(VISIBLE);
        }

    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            onItemSelectListenerTemp.onItemSelect(type, from);
        }
    };

}
