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
import io.bcaas.exchange.bean.UserInfoBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.AccountSecurityContract;
import io.bcaas.exchange.ui.presenter.AccountSecurityPresenterImp;
import io.bcaas.exchange.ui.view.RechargeView;
import io.bcaas.exchange.ui.view.WithDrawView;
import io.bcaas.exchange.view.tablayout.BcaasTabLayout;
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
 * 「提现」
 */
public class WithDrawActivity extends BaseActivity implements AccountSecurityContract.View {

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

    private WithDrawView withDrawViewOne, withDrawViewTwo, withDrawViewThree;
    private UserInfoBean userInfoBeanBTC, userInfoBeanETH, userInfoBeanZBB;
    private List<View> views;
    private TabViewAdapter tabViewAdapter;

    private AccountSecurityContract.Presenter presenter;
    private int currentPosition;
    private List<MemberKeyVO> memberKeyVOList;

    @Override
    public int getContentView() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void getArgs(Bundle bundle) {

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
                        String name = currencyListVO.getEnName();
                        tabLayout.addTab(name, i);
                        //初始化数据
                        RechargeView rechargeView = new RechargeView(this);
                        rechargeView.refreshData(memberKeyVO);
                        rechargeView.setOnItemSelectListener(onItemSelectListener);
                        views.add(rechargeView);

                    }
                }
            }
        }
        String info = "请勿将ETH/ZBA发送至您的比特币(BTC)地址,否则资金将会遗失。比特币的交易需要六个区块的确认,可能会花费1个小时以上才能完成。";
        userInfoBeanBTC = new UserInfoBean("BTC", info, "39LKDBERWWRH343T34VSRG434V43F4G5GT5H");
        userInfoBeanETH = new UserInfoBean("ETH", info, "sdkjfhakssssjdfkasjdbfnaksdjfblniauksj");
        userInfoBeanZBB = new UserInfoBean("ZBB", info, "q234bv41v2b34m3b24mj12b34jm13hb4jffy1h");

        //初始化顶部tab的数据以及相对应的界面信息
        if (tabLayout == null) {
            return;
        }
        withDrawViewOne = new WithDrawView(this);
        withDrawViewOne.refreshData(userInfoBeanBTC);
        withDrawViewOne.setOnItemSelectListener(onItemSelectListener);
        views.add(withDrawViewOne);

        withDrawViewTwo = new WithDrawView(this);
        withDrawViewTwo.refreshData(userInfoBeanETH);
        withDrawViewTwo.setOnItemSelectListener(onItemSelectListener);
        views.add(withDrawViewTwo);


        withDrawViewThree = new WithDrawView(this);
        withDrawViewThree.refreshData(userInfoBeanZBB);
        withDrawViewThree.setOnItemSelectListener(onItemSelectListener);
        views.add(withDrawViewThree);

        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
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
                    intent.setClass(WithDrawActivity.this, WithDrawDetailActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.WIDTH_DRAW_DETAIL);
                    break;
                default:
                    intent.setClass(context, SetFundPasswordActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.FUND_PASSWORD);
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
                        //刷新当前界面
                        switch (currentPosition) {
                            case 0:
                                withDrawViewOne.setScanInfo(scanInfo);
                                break;
                            case 1:
                                withDrawViewTwo.setScanInfo(scanInfo);

                                break;
                            case 2:
                                withDrawViewThree.setScanInfo(scanInfo);

                                break;
                        }
                    }
                    break;
                case Constants.RequestCode.FUND_PASSWORD:
                    //如果从「设置资金密码」页面跳转回来，那么需要重新请求账户资讯
                    presenter.getAccountSecurity();
                    break;
                case Constants.RequestCode.WIDTH_DRAW_DETAIL:
                    break;
            }
        }

    }

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {
        if (tabLayout != null) {
            withDrawViewOne.refreshData(userInfoBeanBTC);
            withDrawViewTwo.refreshData(userInfoBeanETH);
            withDrawViewThree.refreshData(userInfoBeanZBB);
        }
    }

    @Override
    public void getAccountSecurityFailure(String info) {
        showToast(info);
    }
}
