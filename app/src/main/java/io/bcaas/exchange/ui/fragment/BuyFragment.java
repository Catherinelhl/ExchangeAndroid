package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.BuyDataAdapter;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.BuyDataBean;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 買進
 */
public class BuyFragment extends BaseFragment {
    private String TAG = BuyFragment.class.getSimpleName();

    @BindView(R.id.rv_buy_data)
    RecyclerView rvBuyData;
    @BindView(R.id.srl_buy_data)
    SwipeRefreshLayout srlBuyData;
    private BuyDataAdapter buyDataAdapter;
    private List<BuyDataBean> buyDataBeans;

    @Override
    protected void onUserVisible() {
        LogTool.i(TAG, "onUserVisible");

    }

    @Override
    protected void onUserInvisible() {
        LogTool.i(TAG, "onUserInvisible");

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_buy;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        buyDataBeans = new ArrayList<>();
        initBuyData();
        initRefreshLayout();
        initBuyDataAdapter();
    }

    private void initBuyData() {
        for (int i = 0; i < 4; i++) {
            BuyDataBean buyDataBean = new BuyDataBean();
            buyDataBean.setPersonName("Alice");
            buyDataBean.setBuyMethod("支付方式ETH");
            buyDataBean.setPrice("2345.02387000 ETH");
            buyDataBean.setNumber("1.00000000 BTC");
            buyDataBean.setTotalAccount("2345.02387000 ETH");
            buyDataBean.setFee("0.00001 ETH");
            buyDataBeans.add(buyDataBean);
        }
    }

    private void initBuyDataAdapter() {
        buyDataAdapter = new BuyDataAdapter(this.context, buyDataBeans);
        buyDataAdapter.setOnItemSelectListener(onItemSelectListener);
        rvBuyData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
        rvBuyData.setLayoutManager(linearLayoutManager);
        rvBuyData.setAdapter(buyDataAdapter);
    }

    private void initRefreshLayout() {
        // 设置加载按钮的形态
        srlBuyData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color

        );
        srlBuyData.setSize(SwipeRefreshLayout.DEFAULT);
    }


    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {
        srlBuyData.setOnRefreshListener(() -> {
            srlBuyData.setRefreshing(false);
            //判断如果当前没有币种，那么就暂时不能刷新数据
//            if (StringTool.isEmpty(Bas.getBlockService())) {
//                return;
//            }
//            onRefreshTransactionRecord("swipeRefreshLayout");
        });
    }


    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            if (type == null) {
                return;
            }
            BuyDataBean buyDataBean = (BuyDataBean) type;
            LogTool.d(TAG, buyDataBean);
        }
    };

}
