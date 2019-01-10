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
import io.bcaas.exchange.bean.BuyDataBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.activity.BuyDetailActivity;
import io.bcaas.exchange.ui.contracts.BuyContract;
import io.bcaas.exchange.ui.contracts.ForSaleOrderListContract;
import io.bcaas.exchange.ui.presenter.BuyPresenterImp;
import io.bcaas.exchange.ui.presenter.ForSaleOrderListPresenterImp;
import io.bcaas.exchange.ui.view.BuyView;
import io.bcaas.exchange.view.tablayout.BcaasTabLayout;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.PaginationVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 買進
 */
public class BuyFragment extends BaseFragment implements ForSaleOrderListContract.View {
    private String TAG = BuyFragment.class.getSimpleName();


    @BindView(R.id.tab_layout)
    BcaasTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private List<BuyDataBean> buyDataBeansETH;
    private List<BuyDataBean> buyDataBeansBTC;
    private List<BuyDataBean> buyDataBeansZBB;


    private TabViewAdapter tabViewAdapter;
    private List<View> views;
    private BuyView buyViewOne, buyViewTwo, buyViewThree;
    private ForSaleOrderListContract.Presenter presenter;
    //标记当前选中的位置，默认为0
    private int currentPosition = 0;

    //得到当前各页面的nextObjectId,默认是1
    private String nextObjectId = "1";

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_content;
    }

    @Override
    public void initViews(View view) {
        presenter = new ForSaleOrderListPresenterImp(this);
        isPrepared = true;
        buyDataBeansETH = new ArrayList<>();
        buyDataBeansBTC = new ArrayList<>();
        buyDataBeansZBB = new ArrayList<>();
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
                // 添加相对应的数据
                BuyDataBean buyDataBean = new BuyDataBean();
                buyDataBean.setPersonName("Alice");
                buyDataBean.setBuyMethod("支付方式ETH");
                buyDataBean.setPrice("2345.02387000 ETH");
                buyDataBean.setNumber("1.00000000 BTC");
                buyDataBean.setTotalAccount("2345.02387000 ETH");
                buyDataBean.setFee("0.00001 ETH");
                buyDataBeansETH.add(buyDataBean);


                BuyDataBean buyDataBeanBTC = new BuyDataBean();
                buyDataBeanBTC.setPersonName("Catherine");
                buyDataBeanBTC.setBuyMethod("支付方式BTC");
                buyDataBeanBTC.setPrice("345.02387000 BTC");
                buyDataBeanBTC.setNumber("1.00000000 ETH");
                buyDataBeanBTC.setTotalAccount("345.02387000 BTC");
                buyDataBeanBTC.setFee("0.00001 BTC");
                buyDataBeansBTC.add(buyDataBeanBTC);


                BuyDataBean buyDataBeanZBB = new BuyDataBean();
                buyDataBeanZBB.setPersonName("Lucifer");
                buyDataBeanZBB.setBuyMethod("支付方式ZBB");
                buyDataBeanZBB.setPrice("45.02387000 ZBB");
                buyDataBeanZBB.setNumber("1.00000000 BTC");
                buyDataBeanZBB.setTotalAccount("45.02387000 ZBB");
                buyDataBeanZBB.setFee("0.00001 ZBB");
                buyDataBeansZBB.add(buyDataBeanZBB);


            }
            buyViewOne = new BuyView(getContext());
            buyViewOne.refreshData(buyDataBeansETH);
            buyViewOne.setOnItemSelectListener(onItemSelectListener);
            views.add(buyViewOne);

            buyViewTwo = new BuyView(getContext());
            buyViewTwo.refreshData(buyDataBeansETH);
            buyViewTwo.setOnItemSelectListener(onItemSelectListener);
            views.add(buyViewTwo);


            buyViewThree = new BuyView(getContext());
            buyViewThree.refreshData(buyDataBeansETH);
            buyViewThree.setOnItemSelectListener(onItemSelectListener);
            views.add(buyViewThree);

            tabViewAdapter = new TabViewAdapter(views);
            viewPager.setAdapter(tabViewAdapter);
            viewPager.setCurrentItem(0);
            viewPager.setOffscreenPageLimit(size > 3 ? 3 : size);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
            tabLayout.setupWithViewPager(viewPager, new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    currentPosition = tab.getPosition();
                    //刷新当前界面下的内容
                    if (presenter != null) {
                        // 如果当前paymentCurrencyList为-1，那么就是请求全部的数据
                        presenter.getOrderList(memberKeyVOList.get(currentPosition).getCurrencyListVO().getCurrencyUid(), Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, nextObjectId);
                    }
                    switch (currentPosition) {
                        case 0:
                            buyViewOne.refreshData(buyDataBeansETH);
                            break;
                        case 1:
                            buyViewTwo.refreshData(buyDataBeansBTC);

                            break;
                        case 2:
                            buyViewThree.refreshData(buyDataBeansZBB);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            tabLayout.resetSelectedTab(0);
            if (presenter != null) {
                // 如果当前paymentCurrencyList为-1，那么就是请求全部的数据
                presenter.getOrderList(memberKeyVOList.get(currentPosition).getCurrencyListVO().getCurrencyUid(), Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, nextObjectId);
            }
        } else {
            //不加载数据
        }

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
            switch (requestCode) {

            }
        }
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            if (type == null) {
                return;
            }
            BuyDataBean buyDataBean = (BuyDataBean) type;
            LogTool.d(TAG, buyDataBean);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KeyMaps.BUY_DETAIL, buyDataBean);
            intent.putExtras(bundle);
            intent.setClass(context, BuyDetailActivity.class);
            startActivityForResult(intent, Constants.RequestCode.BUY_DETAIL_CODE);
        }
    };

    /**
     * 重置当前界面
     */
    public void resetView() {
        initTopTabData();
    }

    /**
     * 根据传入的支付方式过滤数据
     *
     * @param paymentCurrencyUid
     */
    public void requestForSaleOrderList(String paymentCurrencyUid) {
        LogTool.d(TAG, "requestForSaleOrderList：" + paymentCurrencyUid);
        if (presenter != null) {
            List<MemberKeyVO> memberKeyVOList = BaseApplication.getMemberKeyVOList();
            if (ListTool.noEmpty(memberKeyVOList)) {
                presenter.getOrderList(memberKeyVOList.get(currentPosition).getCurrencyListVO().getCurrencyUid(), paymentCurrencyUid, nextObjectId);

            }
        }
    }


    @Override
    public void getOrderListSuccess(PaginationVO paginationVO) {
        if (paginationVO != null) {

        }
    }

    @Override
    public void getOrderListFailure(String info) {
        showToast(info);
    }

    //刷新标题
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
