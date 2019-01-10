package io.bcaas.exchange.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.bean.SellDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.ui.activity.SellDetailActivity;
import io.bcaas.exchange.ui.view.BuyView;
import io.bcaas.exchange.ui.view.SellView;
import io.bcaas.exchange.view.tablayout.BcaasTabLayout;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 「售出」
 * 拿到当前用户账户下面的各种币种的「可售余额」，根据点击TAB展现不同汇率数据，然后
 */
public class SellFragment extends BaseFragment {
    private String TAG = SellFragment.class.getSimpleName();

    @BindView(R.id.tab_layout)
    BcaasTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private TabViewAdapter tabViewAdapter;
    private List<View> views;

    private SellView sellViewOne, sellViewTwo, sellViewThree;

    private SellDataBean sellDataBeanETH, sellDataBeanBTC, sellDataBeanZBB;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_content;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        views = new ArrayList<>();
        initTopTabData();
    }

    /**
     * 初始化顶部tab的数据以及相对应的界面信息
     */
    private void initTopTabData() {
        if (tabLayout == null) {
            return;
        }
        tabLayout.removeTabLayout();
        //得到当前的所有钱包信息
        RefreshTitle();
        sellDataBeanETH = new SellDataBean("ETH", "BTC", "9.234314", "4235.234234");
        sellDataBeanBTC = new SellDataBean("BTC", "ETH", "8.234314", "3235.234234");
        sellDataBeanZBB = new SellDataBean("ZBB", "BTC", "7.234314", "5.234234");

        sellViewOne = new SellView(getContext());
        sellViewOne.refreshData(sellDataBeanETH);
        sellViewOne.setOnItemSelectListener(onItemSelectListener);
        views.add(sellViewOne);

        sellViewTwo = new SellView(getContext());
        sellViewTwo.refreshData(sellDataBeanBTC);
        sellViewTwo.setOnItemSelectListener(onItemSelectListener);
        views.add(sellViewTwo);


        sellViewThree = new SellView(getContext());
        sellViewThree.refreshData(sellDataBeanZBB);
        sellViewThree.setOnItemSelectListener(onItemSelectListener);
        views.add(sellViewThree);

        tabViewAdapter = new TabViewAdapter(views, "1");
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
        tabLayout.setupWithViewPager(viewPager, new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.resetSelectedTab(0);
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
        }
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {

            if (type == null) {
                return;
            }
            SellDataBean sellDataBean = (SellDataBean) type;
            Intent intent = new Intent();
            intent.putExtra(Constants.KeyMaps.SELL_CURRENCY_UID, sellDataBean.getCurrency());
            intent.putExtra(Constants.KeyMaps.SELL_CURRENCY_PAYMENT_UID, sellDataBean.getExchangeCurrency());
            intent.putExtra(Constants.KeyMaps.SELL_AMOUNT, sellDataBean.getSalableBalance());
            intent.putExtra(Constants.KeyMaps.SELL_UNIT_PRICE, sellDataBean.getExchangeRate());
            intent.setClass(context, SellDetailActivity.class);
            startActivityForResult(intent, Constants.RequestCode.SELL_DETAIL_CODE);
        }
    };

    /**
     * 重置当前界面
     */
    public void resetView() {
        initTopTabData();
    }

    public void RefreshTitle() {
        //得到当前的所有钱包信息
        List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
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

                    }
                }
            }
        }
    }
}
