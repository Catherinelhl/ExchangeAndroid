package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.BuyDataAdapter;
import io.bcaas.exchange.base.BaseLinearLayout;
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
    }

    @Override
    protected void initListener() {
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListenerTemp = onItemSelectListener;
    }

    public void refreshData(List<MemberOrderVO> memberOrderVOS) {
        if (ListTool.noEmpty(memberOrderVOS)) {
            rlNoData.setVisibility(GONE);
            if (buyDataAdapter == null) {
                buyDataAdapter = new BuyDataAdapter(context);
                buyDataAdapter.setOnItemSelectListener(onItemSelectListenerTemp);
                rvBuyData.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
                rvBuyData.setLayoutManager(linearLayoutManager);
                buyDataAdapter.refreshData(memberOrderVOS);
                rvBuyData.setAdapter(buyDataAdapter);
            } else {
                buyDataAdapter.refreshData(memberOrderVOS);
            }
        } else {
            //显示没有信息页面
            rlNoData.setVisibility(VISIBLE);
        }

    }

}
