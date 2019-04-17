package io.bcaas.exchange.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.BindView;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.otto.Subscribe;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.event.LogoutEvent;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.AccountSecurityContract;
import io.bcaas.exchange.ui.contracts.GetAllBalanceContract;
import io.bcaas.exchange.ui.contracts.GetCoinNameListContract;
import io.bcaas.exchange.ui.fragment.AccountFragment;
import io.bcaas.exchange.ui.fragment.MainFragment;
import io.bcaas.exchange.ui.fragment.OrderFragment;
import io.bcaas.exchange.ui.fragment.TransactionFragment;
import io.bcaas.exchange.ui.presenter.AccountSecurityPresenterImp;
import io.bcaas.exchange.ui.presenter.GetAllBalancePresenterImp;
import io.bcaas.exchange.ui.presenter.GetCoinNameListPresenterImp;
import io.bcaas.exchange.view.pop.ChooseCurrenciesPop;
import io.bcaas.exchange.view.pop.SideSlipPop;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * Activity： 首頁
 */
public class MainActivity extends BaseActivity
        implements AccountSecurityContract.View, GetAllBalanceContract.View, GetCoinNameListContract.View {
    @BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.home_container)
    FrameLayout homeContainer;


    //声明当前需要和底部栏搭配的所有fragment
    private List<Fragment> fragments;
    //得到当前显示的Fragment
    private Fragment currentFragment;

    private AccountSecurityContract.Presenter accountSecurityPresenter;
    private GetAllBalanceContract.Presenter getAllBalancePresenter;
    private GetCoinNameListContract.Presenter getCoinNamePresenter;
    //声明侧滑栏
    private SideSlipPop sideSlipPop;
    private ChooseCurrenciesPop chooseCurrenciesPop;


    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initView() {
        fragments = new ArrayList<>();
        //显示标题
        tvTitle.setVisibility(View.VISIBLE);
        //初始化侧滑栏
        sideSlipPop = new SideSlipPop(this);
        chooseCurrenciesPop = new ChooseCurrenciesPop(this);
        //设置侧滑栏的item点击时间监听回调
        sideSlipPop.setOnItemSelectListener(onItemSelectListener);
        chooseCurrenciesPop.setOnItemSelectListener(onItemSelectListener);

        //初始化「首页」页面
        MainFragment fragment = new MainFragment();
        fragments.add(fragment);
        //初始化「交易」页面
        TransactionFragment sellFragment = new TransactionFragment();
        fragments.add(sellFragment);
        //初始化「订单」页面
        OrderFragment orderFragment = new OrderFragment();
        fragments.add(orderFragment);
        //初始化「账户」页面
        AccountFragment accountFragment = new AccountFragment();
        fragments.add(accountFragment);
    }

    @Override
    public void initData() {
        accountSecurityPresenter = new AccountSecurityPresenterImp(this);
        getAllBalancePresenter = new GetAllBalancePresenterImp(this);
        getCoinNamePresenter = new GetCoinNameListPresenterImp(this);
// TODO: 2019/1/14   /*获取币种汇率*/
//        ExchangeBean exchangeBean = new ExchangeBean();
//        exchangeBean.setCurrency("BTC");
//        exchangeBean.setPriceCurrency("8");
//        mainPresenter.getCurrencyUSDPrice(exchangeBean);
        /*获取账户资讯*/
        accountSecurityPresenter.getAccountSecurity();
        //获取账户Token信息
        getAllBalance();

        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = bottomTabLayout.newTab();
            // method 自定义布局-----
            tab.setCustomView(R.layout.item_bottom_tab);
            TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
//            textView.getPaint().setShader(getShader(textView, false));
            textView.setTextColor(context.getResources().getColor(R.color.grey_999999));
            textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationManager.getDrawableTop(this, i, false), null, null);
            textView.setText(dataGenerationManager.getTabTitle(i));
            //自定义布局-----

            bottomTabLayout.addTab(tab);
            if (i == 0) {
                onTabItemSelected(i);
                tab.getCustomView().findViewById(R.id.ll_tab_item).setSelected(true);
                textView.setTextColor(context.getResources().getColor(R.color.blue_5B88FF));
                //method 2
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationManager.getDrawableTop(this, 0, true), null, null);
            }
        }
    }

    /**
     * 设置标题
     */
    private void setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void initListener() {
        hideSoftKeyBoardByTouchView(llMain);
        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //自定义:如果是自定义的tabItem，那么就需要调用这句来设置选中状态，虽然没有界面上的变化
                tab.getCustomView().findViewById(R.id.ll_tab_item).setSelected(true);
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
                textView.setTextColor(context.getResources().getColor(R.color.blue_5B88FF));
                //method 2：如果是直接就用一个TextView控件来表示了，那么就可以直接用下面这一句来表示
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationManager.getDrawableTop(MainActivity.this, tab.getPosition(), true), null, null);
                //改变当前中间content信息；Fragment变换
                onTabItemSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //自定义
                tab.getCustomView().findViewById(R.id.ll_tab_item).setSelected(false);
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
                textView.setTextColor(context.getResources().getColor(R.color.grey_999999));
                //method 2
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationManager.getDrawableTop(MainActivity.this, tab.getPosition(), false), null, null);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //自定义
                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_title);
                textView.setTextColor(context.getResources().getColor(R.color.blue_5B88FF));
                //method 2
                textView.setCompoundDrawablesWithIntrinsicBounds(null, dataGenerationManager.getDrawableTop(MainActivity.this, tab.getPosition(), true), null, null);

            }
        });

        sideSlipPop.setOnDismissListener(() -> setBackgroundAlpha(1f));
        chooseCurrenciesPop.setOnDismissListener(() -> {
            hideSoftKeyboard();
            setBackgroundAlpha(1f);
        });
        RxView.clicks(ibBack).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        showChooseCurrenciesPop(new MemberKeyVO());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 根据选中的position来切换选项卡
     *
     * @param position
     */
    private void onTabItemSelected(int position) {
        if (ListTool.noEmpty(fragments) && position < fragments.size()) {
            currentFragment = fragments.get(position);
            if (currentFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, currentFragment).commit();
            }
            if (getAllBalancePresenter != null) {
                getAllBalancePresenter.getAllBalance();
            }
            if (ibBack != null) {
                ibBack.setVisibility(View.GONE);
            }
            if (tvLeft != null) {
                tvLeft.setVisibility(View.GONE);
            }
            switch (position) {
                case 0:
                    setTitle(getString(R.string.main));
                    break;
                case 1:
                    // 显示右边过滤
                    if (ibBack != null) {
                        ibBack.setVisibility(View.VISIBLE);
                        ibBack.setImageResource(R.mipmap.icon_filter);
                    }
                    if (tvLeft != null) {
                        tvLeft.setText("ZBB/USDT");
                        tvLeft.setVisibility(View.VISIBLE);
                    }
                    setTitle(getString(R.string.transaction));
                    break;
                case 2:
                    setTitle(getString(R.string.order_title));
                    break;
                case 3:
                    setTitle(getString(R.string.amount_title));
                    break;
            }
        }

    }

    @Override
    public void getAllBalanceSuccess(List<MemberKeyVO> memberKeyVOList) {
//        if (currentFragment instanceof BuyFragment) {
//            //刷新标题
//            ((BuyFragment) currentFragment).refreshView();
//        } else if (currentFragment instanceof SellFragment) {
//            //刷新标题
//            ((SellFragment) currentFragment).refreshView();
//        }
    }

    @Override
    public void getAllBalanceFailure(String info) {
        showToast(info);
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            hideSoftKeyboard();
            //如果当前是从侧滑栏返回
            switch (from) {
                case Constants.From.SIDE_SLIP:
                    //通知BuyFragment 根据获取到的type过滤数据
                    MemberKeyVO memberKeyVO = (MemberKeyVO) type;
                    if (memberKeyVO == null) {
                        return;
                    }
                    CurrencyListVO currencyListVO = memberKeyVO.getCurrencyListVO();
                    if (currencyListVO == null) {
                        return;
                    }
//                    if (currentFragment instanceof BuyFragment) {
//                        ((BuyFragment) currentFragment).requestOrderList(MessageConstants.DEFAULT_NEXT_OBJECT_ID, currencyListVO.getCurrencyUid(), "onItemSelect:SIDE_SLIP");
//                    }
                    break;
                case Constants.From.SIDE_SLIP_RESET:
                    //侧滑栏重置当前数据
//                    if (currentFragment instanceof BuyFragment) {
//                        ((BuyFragment) currentFragment).requestOrderList(MessageConstants.DEFAULT_NEXT_OBJECT_ID, Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, "onItemSelect:SIDE_SLIP_RESET");
//                    }
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    public void getAccountSecuritySuccess(MemberVO memberVO) {
        LogTool.d(TAG, "getAccountSecuritySuccess:" + memberVO);

    }

    @Override
    public void getAccountSecurityFailure(String info) {
        LogTool.d(TAG, "getAccountSecurityFailure:" + info);
    }

    /**
     * 获取当前的所有钱包币种以及余额信息
     * 获取账户所有币种余额
     */
    public void getAllBalance() {
        getAllBalancePresenter.getAllBalance();
        //获取当前token相对应的CoinName信息
        getCoinNamePresenter.getCoinNameList();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

            }
        }
    }

    @Subscribe
    public void logoutEvent(LogoutEvent logoutEvent) {
        if (logoutEvent != null) {
            String info = logoutEvent.getInfo();
            if (StringTool.isEmpty(info)) {
                showLogoutDialog();

            } else {
                showLogoutDialog(info);
            }
        }
    }


    @Override
    public void getCoinNameListSuccess(List<CurrencyListVO> currencyListVOList) {
    }

    @Override
    public void getCoinNameListFailure(String info) {
        LogTool.e(TAG, info);

    }

    @Override
    public void onBackPressed() {
        if (multipleClickToDo(2)) {
            super.onBackPressed();
        } else {
            showToast(getString(R.string.double_click_for_exit));
        }
    }

    /**
     * 弹出侧滑栏
     *
     * @param memberKeyVO
     */
    public void showSlidePop(MemberKeyVO memberKeyVO) {
        if (sideSlipPop != null) {
            sideSlipPop.setData(memberKeyVO);
            //弹出侧滑栏
            sideSlipPop.showAtLocation(MainActivity.this.findViewById(R.id.ll_main), Gravity.RIGHT, 0, 0);
            setBackgroundAlpha(0.7f);
        }
    }

    /**
     * 弹出侧滑栏
     *
     * @param memberKeyVO
     */
    private void showChooseCurrenciesPop(MemberKeyVO memberKeyVO) {
        if (chooseCurrenciesPop != null) {
            chooseCurrenciesPop.setData(memberKeyVO);
            //弹出侧滑栏
            chooseCurrenciesPop.showAtLocation(MainActivity.this.findViewById(R.id.ll_main), Gravity.LEFT, 0, 0);
            setBackgroundAlpha(0.7f);
        }
    }
}
