package io.bcaas.exchange.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.BuyDataAdapter;
import io.bcaas.exchange.base.BaseLinearLayout;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.LoadingDataListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.vo.MemberOrderVO;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「购买」页面视图
 */
public class BuyView extends BaseLinearLayout {
    private String TAG = BuyView.class.getSimpleName();

    @BindView(R.id.rv_buy_data)
    RecyclerView rvBuyData;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rl_no_data)
    RelativeLayout rlNoData;
    @BindView(R.id.pb_loading_more)
    ProgressBar pbLoadingMore;
    private BuyDataAdapter buyDataAdapter;
    private OnItemSelectListener onItemSelectListenerTemp;
    private boolean canLoadingMore;//得到当前的是否可以加载更多
    private LoadingDataListener loadingDataListener;

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

    public void setLoadingDataListener(LoadingDataListener loadingDataListener) {
        this.loadingDataListener = loadingDataListener;
    }

    public void refreshData(List<MemberOrderVO> memberOrderVOS, boolean canLoadingMore) {
        this.canLoadingMore = canLoadingMore;
        if (ListTool.noEmpty(memberOrderVOS)) {
            rlNoData.setVisibility(GONE);
            if (buyDataAdapter == null) {
                buyDataAdapter = new BuyDataAdapter(context);
                buyDataAdapter.setOnItemSelectListener(onItemSelectListenerTemp);
                rvBuyData.setHasFixedSize(true);
                rvBuyData.addOnScrollListener(scrollListener);
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
        hideLoadingMoreView();

    }

    public void hideLoadingMoreView() {
        if (pbLoadingMore != null) {
            pbLoadingMore.setVisibility(View.GONE);
        }
    }


    private int mLastVisibleItemPosition;
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                mLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (buyDataAdapter != null) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && mLastVisibleItemPosition + 1 == buyDataAdapter.getItemCount()) {
                    LogTool.d(TAG, "canLoadingMore is:" + canLoadingMore);
                    //发送网络请求获取更多数据
                    if (canLoadingMore) {
                        //显示当前加载的图片
                        if (pbLoadingMore != null) {
                            pbLoadingMore.setVisibility(View.VISIBLE);
                        }
                        //通知「购买」的主界面还是继续加载数据
                        if (loadingDataListener != null) {
                            loadingDataListener.onLoadingData();
                        }
                    }
                }
            }
        }
    };

}
