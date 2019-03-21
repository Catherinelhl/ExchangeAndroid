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
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.AccountSecurityContract;
import io.bcaas.exchange.ui.presenter.AccountSecurityPresenterImp;
import io.bcaas.exchange.ui.view.TurnInView;
import io.bcaas.exchange.view.viewGroup.BcaasTabLayout;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberVO;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * <p>
 * Activity：「转入」
 * <p>
 * Notice：此页面有两种展现形式，如果当前还没有设置资金密码，那么就需要展现设置资金密码的页面，如果当前已经设置了，那么就直接展现用户的账户页面
 */
public class TurnInActivity extends BaseActivity implements AccountSecurityContract.View {

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
    private AccountSecurityContract.Presenter presenter;

    private List<MemberKeyVO> memberKeyVOList;
    private int currentPosition;
    //用来接收当前界面传输过来的值
    private String uIDFrom;


    @Override
    public int getContentView() {
        return R.layout.activity_recharge;
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
        ibBack.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.turn_in);
        views = new ArrayList<>();
        memberKeyVOList = new ArrayList<>();
    }

    @Override
    public void initData() {
        int selectItem = 0;
        presenter = new AccountSecurityPresenterImp(this);
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
                        TurnInView turnInView = new TurnInView(this);
                        turnInView.refreshData(memberKeyVO);
                        turnInView.setOnItemSelectListener(onItemSelectListener);
                        views.add(turnInView);

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

                currentPosition = tab.getPosition();
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPosition = tab.getPosition();
                if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                    if (ListTool.noEmpty(memberKeyVOList)) {
                        ((TurnInView) views.get(currentPosition)).refreshData(memberKeyVOList.get(currentPosition));

                    }
                }
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
    public void onBackPressed() {
        super.onBackPressed();
        setResult(false);
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            //跳转界面
            Intent intent = new Intent();
            intent.setClass(context, SetFundPasswordActivity.class);
            startActivityForResult(intent, Constants.RequestCode.FUND_PASSWORD);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case Constants.RequestCode.FUND_PASSWORD:
                //如果从「设置资金密码」页面跳转回来，那么需要重新请求账户资讯
                presenter.getAccountSecurity();
                break;
        }
    }

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {
        if (tabLayout != null) {
            if (ListTool.noEmpty(views)) {
                for (int i = 0; i < views.size(); i++) {
                    if (ListTool.noEmpty(memberKeyVOList)) {
                        ((TurnInView) views.get(i)).refreshData(memberKeyVOList.get(i));

                    }
                }
            }
        }
    }

    @Override
    public void getAccountSecurityFailure(String info) {
        showToast(info);
    }
}
