package io.bcaas.exchange.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.MyFundDataAdapter;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.bean.MyFundBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * <p>
 * <p>
 * 「我的资产」
 */
public class MyFundActivity extends BaseActivity {

    private String TAG = MyFundActivity.class.getSimpleName();

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.rv_my_fund_data)
    RecyclerView rvMyFundData;
    @BindView(R.id.srl_my_fund_data)
    SwipeRefreshLayout srlMyFundData;

    private MyFundDataAdapter myFundDataAdapter;
    private List<MyFundBean> myFundBeanList;

    @Override
    public int getContentView() {
        return R.layout.activity_my_fund;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.my_fund);
        myFundBeanList = new ArrayList<>();

    }

    @Override
    public void initData() {
        initMyFundData();
        initRefreshLayout();
        initAdapter();
    }

    /**
     * 初始化「我的资产」数据信息
     */
    private void initMyFundData() {
        MyFundBean myFundBeanBTC = new MyFundBean("BTC", "15.2354252", "23.325425234");
        MyFundBean myFundBeanETH = new MyFundBean("ETH", "5.2354252", "2.325425234");
        MyFundBean myFundBeanZBB = new MyFundBean("ZBB", "10.2354252", "3.325425234");
        myFundBeanList.add(myFundBeanBTC);
        myFundBeanList.add(myFundBeanETH);
        myFundBeanList.add(myFundBeanZBB);
    }

    private void initRefreshLayout() {
        // 设置加载按钮的形态
        srlMyFundData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color

        );
        srlMyFundData.setSize(SwipeRefreshLayout.DEFAULT);
    }

    private void initAdapter() {
        myFundDataAdapter = new MyFundDataAdapter(this, myFundBeanList);
        myFundDataAdapter.setOnItemSelectListener(onItemSelectListener);
        rvMyFundData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false);
        rvMyFundData.setLayoutManager(linearLayoutManager);
        rvMyFundData.setAdapter(myFundDataAdapter);
    }


    @Override
    public void initListener() {
        srlMyFundData.setOnRefreshListener(() -> {
            srlMyFundData.setRefreshing(false);
            //判断如果当前没有币种，那么就暂时不能刷新数据
//            if (StringTool.isEmpty(Bas.getBlockService())) {
//                return;
//            }
//            onRefreshTransactionRecord("swipeRefreshLayout");
        });
        Disposable subscribe = RxView.clicks(ibBack).throttleFirst(Constants.ValueMaps.sleepTime800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        setResult(true);
                    }
                });
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            switch (from) {
                case MessageConstants.RECHARGE:
                    // 充值
                    break;
                case MessageConstants.WITHDRAW:
                    //提现
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(false);
    }

    /**
     * 关闭当前页面，返回上一个页面
     *
     * @param isBack
     */
    private void setResult(boolean isBack) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.KeyMaps.From, isBack);
        intent.putExtras(bundle);
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

}
