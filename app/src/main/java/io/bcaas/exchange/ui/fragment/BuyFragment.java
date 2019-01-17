package io.bcaas.exchange.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import butterknife.BindView;
import com.google.gson.reflect.TypeToken;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.activity.BuyDetailActivity;
import io.bcaas.exchange.ui.activity.MainActivity;
import io.bcaas.exchange.ui.contracts.ForSaleOrderListContract;
import io.bcaas.exchange.ui.presenter.ForSaleOrderListPresenterImp;
import io.bcaas.exchange.ui.view.BuyView;
import io.bcaas.exchange.view.tablayout.BcaasTabLayout;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.PaginationVO;

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
    @BindView(R.id.srl_data)
    SwipeRefreshLayout srlData;

    private TabViewAdapter tabViewAdapter;
    private List<View> views;
    private ForSaleOrderListContract.Presenter presenter;
    //标记当前选中的位置，默认为0
    private int currentPosition = 0;

    //得到当前各页面的nextObjectId,默认是1
    private String nextObjectId = MessageConstants.DEFAULT_NEXT_OBJECT_ID;
    private List<MemberOrderVO> memberOrderVOS;
    private PaginationVO paginationVO;
    private List<MemberKeyVO> memberKeyVOListTitle;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_content;
    }

    @Override
    public void initViews(View view) {
        presenter = new ForSaleOrderListPresenterImp(this);
        isPrepared = true;
        views = new ArrayList<>();
        memberOrderVOS = new ArrayList<>();
        memberKeyVOListTitle = new ArrayList<>();
        // 设置加载按钮的形态
        srlData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color

        );
        srlData.setSize(SwipeRefreshLayout.DEFAULT);
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
        memberKeyVOListTitle = BaseApplication.getMemberKeyVOList();
        if (ListTool.noEmpty(memberKeyVOListTitle)) {
            int size = memberKeyVOListTitle.size();
            //加载数据
            for (int i = 0; i < size; i++) {
                //添加标题
                MemberKeyVO memberKeyVO = memberKeyVOListTitle.get(i);
                if (memberKeyVO != null) {
                    CurrencyListVO currencyListVO = memberKeyVO.getCurrencyListVO();
                    if (currencyListVO != null) {
                        String name = currencyListVO.getEnName();
                        tabLayout.addTab(name, i);
                    }
                }
                BuyView buyView = new BuyView(getContext());
                buyView.refreshData(memberOrderVOS);
                buyView.setOnItemSelectListener(onItemSelectListener);
                views.add(buyView);
            }

            tabViewAdapter = new TabViewAdapter(views);
            viewPager.setAdapter(tabViewAdapter);
            viewPager.setCurrentItem(0);
            //将当前选中的token type返回给MainActivity
            if (activity != null) {
                ((MainActivity) activity).setCurrentDisplayType(memberKeyVOListTitle.get(0));
            }
            viewPager.setOffscreenPageLimit(size > 3 ? 3 : size);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
            tabLayout.setupWithViewPager(viewPager, new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    // 得到当前的position
                    currentPosition = tab.getPosition();
                    //将当前选中的token type返回给MainActivity
                    if (activity != null && currentPosition < size) {
                        ((MainActivity) activity).setCurrentDisplayType(memberKeyVOListTitle.get(currentPosition));
                    }
                    // 清空当前的界面信息
                    memberOrderVOS.clear();
                    // 刷新界面信息
                    if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                        ((BuyView) views.get(currentPosition)).refreshData(memberOrderVOS);
                    }
                    //重新请求数据
                    if (presenter != null) {
                        // 如果当前paymentCurrencyList为-1，那么就是请求全部的数据
                        presenter.getOrderList(memberKeyVOListTitle.get(currentPosition).getCurrencyListVO().getCurrencyUid(),
                                Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, nextObjectId);
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
                presenter.getOrderList(memberKeyVOListTitle.get(currentPosition).getCurrencyListVO().getCurrencyUid(), Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, nextObjectId);
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
        srlData.setOnRefreshListener(() -> {
            srlData.setRefreshing(false);
            requestOrderList();
        });
    }

    private void requestOrderList() {
        // 1：刷新当前购买页面的待出售数据

        if (presenter != null) {
            // 如果当前paymentCurrencyList为-1，那么就是请求全部的数据
            presenter.getOrderList(memberKeyVOListTitle.get(currentPosition).getCurrencyListVO().getCurrencyUid(), Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, nextObjectId);
        }
    }

    private void getAllBalance() {
        //2：刷新账户的GetAllBalance
        if (activity != null) {
            ((MainActivity) activity).getAllBalance();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.BUY_DETAIL_CODE:
                    //1：判断当前是否是点击「返回」按钮返回
                    if (data != null) {
                        boolean isBack = data.getBooleanExtra(Constants.KeyMaps.From, false);
                        if (!isBack) {
                            requestOrderList();
                            getAllBalance();
                        }
                    }

                    break;
                default:

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
            MemberOrderVO memberOrderVO = (MemberOrderVO) type;
            LogTool.d(TAG, memberOrderVO);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KeyMaps.BUY_DETAIL, memberOrderVO);
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
        if (srlData != null) {
            srlData.setRefreshing(false);
        }
        if (paginationVO != null) {
            this.paginationVO = paginationVO;
            //得到当前接口的页面信息
            String nextObjectId = paginationVO.getNextObjectId();
            Integer totalPageNumber = paginationVO.getTotalPageNumber();
            Long totalObjectNumber = paginationVO.getTotalObjectNumber();
            List<Object> objects = paginationVO.getObjectList();
            LogTool.d(TAG, objects);
            if (ListTool.isEmpty(objects)) {
                memberOrderVOS.clear();
            } else {
                memberOrderVOS = GsonTool.convert(GsonTool.string(paginationVO.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                }.getType());
            }
            if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                ((BuyView) views.get(currentPosition)).refreshData(memberOrderVOS);
            }
        }
    }

    @Override
    public void getOrderListFailure(String info) {

        if (srlData != null) {
            srlData.setRefreshing(false);
        }
        showToast(info);
    }

}
