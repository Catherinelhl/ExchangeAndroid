package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.AccountSecurityContract;
import io.bcaas.exchange.ui.contracts.GetAllBalanceContract;
import io.bcaas.exchange.ui.presenter.GetAllBalancePresenterImp;
import io.bcaas.exchange.ui.view.WithDrawView;
import io.bcaas.exchange.view.tablayout.BcaasTabLayout;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * <p>
 * Activity：「提现」
 */
public class WithDrawActivity extends BaseActivity
        implements AccountSecurityContract.View, GetAllBalanceContract.View {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.tab_layout)
    BcaasTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private List<View> views;
    private TabViewAdapter tabViewAdapter;

    private GetAllBalanceContract.Presenter getAllBalancePresenter;
    private int currentPosition;
    private List<MemberKeyVO> memberKeyVOList;
    //用来接收当前界面传输过来的值
    private String uIDFrom;

    @Override
    public int getContentView() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void getArgs(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        uIDFrom = bundle.getString(Constants.KeyMaps.From);
    }

    @Override
    public void initView() {
        views = new ArrayList<>();
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.with_draw);
        memberKeyVOList = new ArrayList<>();
    }

    @Override
    public void initData() {
        int selectItem = 0;

        getAllBalancePresenter = new GetAllBalancePresenterImp(this);
        //刷新界面
        memberKeyVOList = BaseApplication.getMemberKeyVOList();
        if (ListTool.noEmpty(memberKeyVOList)) {
            int size = memberKeyVOList.size();
            //加载数据
            for (int i = 0; i < size; i++) {
                //添加标题
                MemberKeyVO memberKeyVO = memberKeyVOList.get(i);
                if (memberKeyVO != null) {
                    CurrencyListVO currencyListVO = memberKeyVO.getCurrencyListVO();
                    if (currencyListVO != null) {
                        //比对当前传输过来的MemberKeyVo信息，然后选中相对应的item
                        if (StringTool.equals(uIDFrom, currencyListVO.getCurrencyUid())) {
                            selectItem = i;
                            LogTool.d(TAG, "selectItem:" + selectItem);
                        }
                        String name = currencyListVO.getEnName();
                        tabLayout.addTab(name, i);
                        //初始化数据
                        WithDrawView withDrawView = new WithDrawView(this);
                        withDrawView.setHideSoftKeyBoardListener(hideSoftKeyBoardListener);
                        withDrawView.refreshData(memberKeyVO);
                        withDrawView.setOnItemSelectListener(onItemSelectListener);
                        views.add(withDrawView);

                    }
                }
            }
        }

        //初始化顶部tab的数据以及相对应的界面信息
        if (tabLayout == null) {
            return;
        }

        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        tabLayout.selectTab(selectItem);
        viewPager.setCurrentItem(selectItem);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
        tabLayout.setupWithViewPager(viewPager, new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideSoftKeyboard();
                currentPosition = tab.getPosition();
                refreshView();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void initListener() {

        Disposable subscribe = RxView.clicks(ibBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        setResult(true);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(false);
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            Intent intent = new Intent();

            switch (from) {
                case Constants.EditTextFrom.WITHDRAW_SCAN:
                    //跳转扫描，同时也应该记录下当前返回此动作是第几个页面，方便刷新界面
                    requestCameraPermission();
                    break;
                case Constants.From.WITHDRAW_SURE:
                    if (type == null) {
                        return;
                    }
                    RequestJson requestJson = (RequestJson) type;
                    if (requestJson == null) {
                        return;
                    }
                    intent.setClass(WithDrawActivity.this, WithDrawDetailActivity.class);
                    intent.putExtra(Constants.KeyMaps.WITHDRAW_REQUEST_JSON, requestJson);
                    startActivityForResult(intent, Constants.RequestCode.WIDTH_DRAW_DETAIL);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            switch (requestCode) {
                case Constants.RequestCode.REQUEST_CODE_CAMERA_SCAN:
                    // 如果当前是照相机扫描回来
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        String scanInfo = bundle.getString(Constants.KeyMaps.RESULT);
                        LogTool.d(TAG, "scanInfo:" + scanInfo);
                        LogTool.d(TAG, "currentPosition:" + currentPosition);
                        //刷新当前界面
                        if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                            if (ListTool.noEmpty(memberKeyVOList)) {
                                ((WithDrawView) views.get(currentPosition)).setScanInfo(scanInfo);
                            }
                        }
                    }
                    break;
                case Constants.RequestCode.WIDTH_DRAW_DETAIL:
                    //刷新當前界面，重新請求餘額
                    getAllBalancePresenter.getAllBalance();
                    break;
            }
        }

    }

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {
        if (tabLayout != null) {
            if (ListTool.noEmpty(views)) {
                for (int i = 0; i < views.size(); i++) {
                    if (ListTool.noEmpty(memberKeyVOList)) {
                        ((WithDrawView) views.get(i)).refreshData(memberKeyVOList.get(i));

                    }
                }
            }
        }
    }

    @Override
    public void getAccountSecurityFailure(String info) {
        showToast(info);
    }

    @Override
    public void getAllBalanceSuccess(List<MemberKeyVO> memberKeyVOList) {
        //刷新当前的界面数据信息
        this.memberKeyVOList.clear();
        this.memberKeyVOList = memberKeyVOList;
        refreshView();
    }

    @Override
    public void getAllBalanceFailure(String info) {
        LogTool.e(TAG, info);
    }

    /**
     * 更新当前的界面数据信息
     */
    private void refreshView() {
        if (ListTool.noEmpty(views)
                && currentPosition < views.size()
                && ListTool.noEmpty(memberKeyVOList)
                && currentPosition < memberKeyVOList.size()) {
            ((WithDrawView) views.get(currentPosition)).refreshData(memberKeyVOList.get(currentPosition));
        }
    }
}
