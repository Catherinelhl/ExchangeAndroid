package io.bcaas.exchange.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.BindView;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding2.view.RxView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.listener.LoadingDataListener;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.activity.BuyDetailActivity;
import io.bcaas.exchange.ui.activity.MainActivity;
import io.bcaas.exchange.ui.contracts.ForSaleOrderListContract;
import io.bcaas.exchange.ui.presenter.ForSaleOrderListPresenterImp;
import io.bcaas.exchange.ui.view.BuyView;
import io.bcaas.exchange.view.viewGroup.BaseTabLayout;
import io.bcaas.exchange.view.viewGroup.StickHeadScrollView;
import io.bcaas.exchange.view.viewpager.BaseViewPager;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.MemberKeyVO;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.PaginationVO;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * Fragment：「買進」
 */

@Deprecated
public class BuyFragment extends BaseFragment
        implements ForSaleOrderListContract.View {

    private String TAG = BuyFragment.class.getSimpleName();

    @BindView(R.id.shsv)
    StickHeadScrollView shsv;
    @BindView(R.id.tab_layout)
    BaseTabLayout tabLayout;
    @BindView(R.id.viewpager)
    BaseViewPager viewPager;
    @BindView(R.id.srl_data)
    SwipeRefreshLayout srlData;
    @BindView(R.id.iv_bg_banner)
    ImageView ivBgBanner;
    @BindView(R.id.ib_right)
    ImageButton ibRight;
    @BindView(R.id.v_line)
    View vLine;
    private TabViewAdapter tabViewAdapter;
    private List<View> views;
    private ForSaleOrderListContract.Presenter presenter;
    //标记当前选中的位置，默认为0
    private int currentPosition = 0;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    //得到当前各页面的nextObjectId,默认是1
    private String nextObjectId = MessageConstants.DEFAULT_NEXT_OBJECT_ID;//"0";
    private List<MemberKeyVO> memberKeyVOListTitle;
    //得到当前「买进」页面当前展示的token，用于过滤器过滤
    private MemberKeyVO currentDisplayType;
    //定义一个存储不同数据的map
    private Map<Integer, List<MemberOrderVO>> memberOrderVOSMap;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_buy;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        //显示右边过滤器
        ibRight.setVisibility(View.VISIBLE);
        vLine.setVisibility(View.VISIBLE);
        presenter = new ForSaleOrderListPresenterImp(this);
        views = new ArrayList<>();
        memberOrderVOSMap = new HashMap<>();
        memberKeyVOListTitle = new ArrayList<>();
        // 设置加载按钮的形态
        srlData.setColorSchemeResources(
                R.color.button_color,
                R.color.button_color
        );
        //设置下拉进度条的背景颜色，默认白色
        srlData.setProgressBackgroundColorSchemeResource(R.color.transparent);
        srlData.setSize(SwipeRefreshLayout.DEFAULT);
        ivBgBanner.setVisibility(View.VISIBLE);
        //获取到当前屏幕的宽度，然后重新设置banner的宽高
        int width = BaseApplication.getScreenWidth();
        int height = width / 26 * 9;
        ViewGroup.LayoutParams layoutParams = ivBgBanner.getLayoutParams();
        layoutParams.height = height;
        ivBgBanner.setLayoutParams(layoutParams);

        //避免自动滑动到底部
        tabLayout.setFocusable(true);
        tabLayout.setFocusableInTouchMode(true);
        tabLayout.requestFocus();
        //2.set height
        shsv.resetHeight(tabLayout, srlData);
        viewPager.setCanSlide(false);

    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {
        srlData.setOnRefreshListener(() -> {
            srlData.setRefreshing(false);
            requestOrderList(MessageConstants.DEFAULT_NEXT_OBJECT_ID, Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, "initListener");
        });
        RxView.clicks(ibRight).throttleFirst(Constants.Time.sleep800, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {
                        //刷新侧滑栏的值
                        if (activity != null) {
                            ((MainActivity) activity).showSlidePop(currentDisplayType);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
        presenter.cancelSubscribe();
    }

    /**
     * 请求订单列表
     */
    public void requestOrderList(String nextObjectId, String paymentCurrencyUid, String type) {
        LogTool.d(TAG, "[ requestOrderList ]:" + type);
        // 1：刷新当前购买页面的待出售数据
        if (presenter != null && ListTool.noEmpty(memberKeyVOListTitle)) {
            this.nextObjectId = nextObjectId;
            // 如果当前paymentCurrencyList为-1，那么就是请求全部的数据
            presenter.getOrderList(memberKeyVOListTitle.get(currentPosition).getCurrencyListVO().getCurrencyUid(),
                    paymentCurrencyUid, nextObjectId);
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
                            requestOrderList(MessageConstants.DEFAULT_NEXT_OBJECT_ID, Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, "onActivityResult");
                            //2：刷新账户的GetAllBalance
                            if (activity != null) {
                                ((MainActivity) activity).getAllBalance();
                            }
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
            LogTool.d(TAG, "onItemSelect:" + from);
            switch (from) {
                case Constants.From.BUY:
                    MemberOrderVO memberOrderVO = (MemberOrderVO) type;
                    LogTool.d(TAG, memberOrderVO);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KeyMaps.BUY_DETAIL, memberOrderVO);
                    intent.putExtras(bundle);
                    intent.setClass(getContext(), BuyDetailActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.BUY_DETAIL_CODE);
                    break;
            }

        }
    };

    /**
     * 刷新所有界面
     */
    public void refreshView() {
        if (tabLayout == null && viewPager == null) {
            return;
        }
        int width = BaseApplication.getScreenWidth() - getContext().getResources().getDimensionPixelOffset(R.dimen.d48);
        tabLayout.removeTabLayout();
        //得到当前的所有钱包信息
        memberKeyVOListTitle = BaseApplication.getMemberKeyVOList();
        if (ListTool.noEmpty(memberKeyVOListTitle)) {
            int size = memberKeyVOListTitle.size();
            tabLayout.setTabSize(size);
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
            }
            tabLayout.measureLayoutParams(true, width);

        }

        tabLayout.removeTabLayout();
        viewPager.removeAllViews();
        views.clear();
        //得到当前的所有钱包信息
        memberKeyVOListTitle = BaseApplication.getMemberKeyVOList();
        if (ListTool.noEmpty(memberKeyVOListTitle)) {
            int size = memberKeyVOListTitle.size();
            tabLayout.setTabSize(size);
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
                BuyView buyView = new BuyView(activity);
                buyView.refreshData(getCurrentMemberOrderInfo(i), true);
                buyView.setOnItemSelectListener(onItemSelectListener);
                buyView.setLoadingDataListener(loadingDataListener);
                views.add(buyView);
            }
            tabViewAdapter = new TabViewAdapter(views);
            viewPager.setAdapter(tabViewAdapter);
            viewPager.setCurrentItem(0);
            //将当前选中的token type返回给MainActivity
            setCurrentDisplayType(memberKeyVOListTitle.get(0));
            viewPager.setOffscreenPageLimit(size);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
            tabLayout.setupWithViewPager(width,
                    viewPager, new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            // 得到当前的position
                            currentPosition = tab.getPosition();
                            //将当前选中的token type返回给MainActivity
                            if (currentPosition < size) {
                                setCurrentDisplayType(memberKeyVOListTitle.get(currentPosition));
                            }
                            // 刷新界面信息
                            if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                                List<MemberOrderVO> memberOrderVOS = memberOrderVOSMap.get(currentPosition);
                                if (ListTool.isEmpty(memberOrderVOS)) {
                                    //如果当前没有这个position的数据，那么需要重新请求数据
                                    requestOrderList(MessageConstants.DEFAULT_NEXT_OBJECT_ID, Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, "setupWithViewPager");
                                }
                                ((BuyView) views.get(currentPosition)).refreshData(memberOrderVOS, true);
                            }
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
            requestOrderList(MessageConstants.DEFAULT_NEXT_OBJECT_ID, Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, "refreshView default");
        }
    }

    /**
     * 根据当前的下标取得相应的数据信息
     *
     * @param position
     * @return
     */
    private List<MemberOrderVO> getCurrentMemberOrderInfo(int position) {
        //根据position获取order信息
        List<MemberOrderVO> memberOrderVOS = new ArrayList<>();
        if (memberOrderVOSMap != null) {
            memberOrderVOS = memberOrderVOSMap.get(position);
        }
        return memberOrderVOS;
    }

    @Override
    public void getOrderListSuccess(PaginationVO paginationVO, boolean isRefresh) {
        if (isRefresh) {
            //停止下拉刷新的视图
            if (srlData != null) {
                srlData.setRefreshing(false);
            }
        }
        List<MemberOrderVO> memberOrderVOS = new ArrayList<>();
        if (memberOrderVOSMap != null) {
            memberOrderVOS = memberOrderVOSMap.get(currentPosition);
        }
        if (paginationVO != null) {
            boolean canLoadingMore = false;
            //得到当前接口的页面信息
            String objectID = paginationVO.getNextObjectId();
            //判断当前是否需要继续加载
            if (StringTool.equals(MessageConstants.NEXT_PAGE_IS_EMPTY, objectID)) {
                nextObjectId = MessageConstants.NEXT_PAGE_IS_EMPTY_SYMBOL;
            } else {
                nextObjectId = objectID;
                canLoadingMore = true;
            }
            List<Object> objects = paginationVO.getObjectList();
            GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getOrderListSuccess:", objects);
            if (isRefresh) {
                //如果当前是需要刷新的 ，那么直接得到最新的数据
                if (ListTool.noEmpty(objects)) {
                    memberOrderVOS = GsonTool.convert(GsonTool.string(paginationVO.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                    }.getType());
                }
            } else {
                //如果当前是不需要刷新的，那么就直接追加数据
                if (ListTool.noEmpty(objects)) {
                    List<MemberOrderVO> memberOrderVOSTemp = GsonTool.convert(GsonTool.string(paginationVO.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                    }.getType());
                    memberOrderVOS.addAll(memberOrderVOSTemp);
                }
            }
            //将当前的数据再填充进去
            if (memberOrderVOSMap != null) {
                memberOrderVOSMap.put(currentPosition, memberOrderVOS);
            }
            if (ListTool.noEmpty(views) && currentPosition < views.size()) {
                ((BuyView) views.get(currentPosition)).refreshData(memberOrderVOS, canLoadingMore);
            }
        }
    }

    @Override
    public void getOrderListFailure(String info, boolean isRefresh) {
        //提示其信息
        showToast(info);
        if (isRefresh) {
            //停止下拉刷新的视图
            if (srlData != null) {
                srlData.setRefreshing(false);
            }
        }
    }

    private LoadingDataListener loadingDataListener = () -> requestOrderList(nextObjectId, Constants.ValueMaps.ALL_FOR_SALE_ORDER_LIST, "loadingDataListener");

    /**
     * 当前显示的Token type
     *
     * @param memberKeyVO
     */
    public void setCurrentDisplayType(MemberKeyVO memberKeyVO) {
        this.currentDisplayType = memberKeyVO;
        GsonTool.logInfo(TAG, "setCurrentDisplayType:", memberKeyVO);
    }
}
