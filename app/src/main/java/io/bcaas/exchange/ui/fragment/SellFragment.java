package io.bcaas.exchange.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseActivity;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.SellDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.activity.MainActivity;
import io.bcaas.exchange.ui.activity.SellDetailActivity;
import io.bcaas.exchange.ui.view.SellView;
import io.bcaas.exchange.view.viewGroup.BaseTabLayout;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * Fragment：「售出」
 * Notice：拿到当前用户账户下面的各种币种的「可售余额」，根据点击TAB展现不同汇率数据，然后
 */
public class SellFragment extends BaseFragment {
    @BindView(R.id.srl_data)
    SwipeRefreshLayout srlData;
    private String TAG = SellFragment.class.getSimpleName();

    @BindView(R.id.tab_layout)
    BaseTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private TabViewAdapter tabViewAdapter;
    private List<View> views;
    // 当前tab的选中
    private int currentPosition = 0;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sell;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        views = new ArrayList<>();
        srlData.setEnabled(false);
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        refreshView();
    }

    @Override
    protected void cancelSubscribe() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.SELL_DETAIL_CODE:
                    if (data != null) {
                        boolean isBack = data.getBooleanExtra(Constants.KeyMaps.From, false);
                        if (!isBack) {
                            //重新请求getAllBalance 信息刷新当前的界面信息
                            if (activity != null) {
                                ((MainActivity) activity).getAllBalance();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {

            if (type == null) {
                return;
            }
            if (StringTool.notEmpty(from)) {
                switch (from) {
                    case Constants.From.SELL_SELECT_CURRENCY:
                        if (type instanceof CurrencyListVO) {
                            CurrencyListVO currencyListVO = (CurrencyListVO) type;
                            // 显示当前的所有币种信息
                            if (activity != null) {
                                ((BaseActivity) activity).showCurrencyListPopWindow(currencyListVO, onItemSelectListener);
                            }
                        }

                        break;
                    case Constants.From.SELL_SELECTED_CURRENCY:
                        MemberKeyVO memberKeyVO = (MemberKeyVO) type;
                        // 更新当前的界面信息
                        if (ListTool.noEmpty(views)) {
                            if (currentPosition < views.size()) {
                                ((SellView) views.get(currentPosition)).refreshExchangeCurrency(memberKeyVO);

                            }
                        }
                        break;
                    case Constants.From.SELL_VIEW:
                        SellDataBean sellDataBean = (SellDataBean) type;
                        if (sellDataBean == null) {
                            return;
                        }
                        Intent intent = new Intent();
                        //直接将SellDataBean数据类传递到下一个页面
                        intent.putExtra(Constants.KeyMaps.SELL_DATA_BEAN, sellDataBean);
                        intent.setClass(getContext(), SellDetailActivity.class);
                        startActivityForResult(intent, Constants.RequestCode.SELL_DETAIL_CODE);
                        break;

                }
            }

        }
    };

    /**
     * 重置当前界面
     */
    public void refreshView() {
        if (tabLayout == null && viewPager == null) {
            return;
        }
        tabLayout.removeTabLayout();
        viewPager.removeAllViews();
        views.clear();
        //刷新界面
        List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
        LogTool.d(TAG, "refreshView:" + memberKeyVOList);
        if (ListTool.noEmpty(memberKeyVOList)) {
            int size = memberKeyVOList.size();
            tabLayout.setTabSize(size);
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
                        SellView sellView = new SellView(getContext());
                        sellView.refreshData(memberKeyVO);
                        sellView.setHideSoftKeyBoardListener(hideSoftKeyBoardListener);
                        sellView.setOnItemSelectListener(onItemSelectListener);
                        views.add(sellView);

                    }
                }
            }
        }
        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
        tabLayout.setupWithViewPager(
                BaseApplication.getScreenWidth(),
                viewPager, new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        currentPosition = tab.getPosition();
                        refreshCurrentView();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
    }

    /**
     * 刷新当前显示的页面即可
     */
    public void refreshCurrentView() {
        if (ListTool.noEmpty(views) && currentPosition < views.size()) {
            //刷新界面
            List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
            if (ListTool.noEmpty(memberKeyVOList)) {
                ((SellView) views.get(currentPosition)).refreshData(memberKeyVOList.get(currentPosition));
            }
        }
    }


}
