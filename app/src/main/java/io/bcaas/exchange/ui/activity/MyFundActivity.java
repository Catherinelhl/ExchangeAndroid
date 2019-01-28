package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.GetAllBalanceContract;
import io.bcaas.exchange.ui.presenter.GetAllBalancePresenterImp;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
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
public class MyFundActivity extends BaseActivity
        implements GetAllBalanceContract.View {

    private String TAG = MyFundActivity.class.getSimpleName();

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.rv_my_fund_data)
    RecyclerView rvMyFundData;
    @BindView(R.id.srl_my_fund_data)
    SwipeRefreshLayout srlMyFundData;

    private MyFundDataAdapter myFundDataAdapter;
    private GetAllBalanceContract.Presenter getAllBalancePresenter;


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

    }

    @Override
    public void initData() {
        getAllBalancePresenter = new GetAllBalancePresenterImp(this);
        getAllBalancePresenter.getAllBalance();
        initRefreshLayout();
        initAdapter();
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
        myFundDataAdapter = new MyFundDataAdapter(this);
        myFundDataAdapter.refreshData(BaseApplication.getMemberKeyVOList());
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
            //刷新当前的资金信息
            if (getAllBalancePresenter != null) {
                getAllBalancePresenter.getAllBalance();

            }
        });
        Disposable subscribe = RxView.clicks(ibBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
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
            if (type == null) {
                return;
            }
            if (StringTool.isEmpty(from)) {
                return;
            }
            //接收当前传回来的MemberKeyVO信息
            MemberKeyVO memberKeyVO = null;
            if (type instanceof MemberKeyVO) {
                memberKeyVO = (MemberKeyVO) type;
            }
            if (memberKeyVO == null) {
                return;
            }
            CurrencyListVO currencyListVO=memberKeyVO.getCurrencyListVO();
            if (currencyListVO==null){
             return;
            }
            String uid=currencyListVO.getCurrencyUid();
            if (StringTool.isEmpty(uid)){
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(Constants.KeyMaps.From, uid);
            switch (from) {
                case Constants.From.RECHARGE:
                    intent.setClass(MyFundActivity.this, RechargeActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.RECHARGE);
                    // 充值
                    break;
                case Constants.From.WITHDRAW:
                    intent.setClass(MyFundActivity.this, WithDrawActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.WITH_DRAW);
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

    @Override
    public void getAllBalanceSuccess(List<MemberKeyVO> memberKeyVOList) {
        if (ListTool.noEmpty(memberKeyVOList)) {
            if (myFundDataAdapter != null) {
                myFundDataAdapter.refreshData(memberKeyVOList);
            }
        }
    }

    @Override
    public void getAllBalanceFailure(String info) {
        showToast(info);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.RECHARGE:
                case Constants.RequestCode.WITH_DRAW:
                    if (getAllBalancePresenter != null) {
                        getAllBalancePresenter.getAllBalance();
                    }
                    break;
            }
        }
    }
}
