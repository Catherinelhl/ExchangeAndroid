package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import com.google.gson.reflect.TypeToken;
import io.bcaas.exchange.R;
import io.bcaas.exchange.adapter.TabViewAdapter;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.gson.JsonTool;
import io.bcaas.exchange.listener.OnItemSelectListener;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.file.FilePathTool;
import io.bcaas.exchange.tools.file.ResourceTool;
import io.bcaas.exchange.ui.contracts.OrderRecordContract;
import io.bcaas.exchange.ui.presenter.OrderRecordPresenterImp;
import io.bcaas.exchange.ui.view.OrderView;
import io.bcaas.exchange.view.tablayout.BcaasTabLayout;
import io.bcaas.exchange.vo.MemberOrderVO;
import io.bcaas.exchange.vo.PaginationVO;
import io.bcaas.exchange.vo.ResponseJson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 訂單
 * 点击此页面，展现第一个页面的交易信息；然后点击tapTitle，进行网络的请求
 */
public class OrderFragment extends BaseFragment implements OrderRecordContract.View {

    private String TAG = OrderFragment.class.getSimpleName();

    @BindView(R.id.tab_layout)
    BcaasTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private OrderRecordContract.Presenter presenter;
    private OrderView orderViewOne, orderViewTwo, orderViewThree;


    private List<MemberOrderVO> orderTransactionBeans;
    private List<MemberOrderVO> orderRechargeBeans;
    private List<MemberOrderVO> orderWithDrawBeans;

    private TabViewAdapter tabViewAdapter;
    private List<View> views;
    private String nextObjectIdRecharge = "1";
    private String nextObjectIdWithDraw = "1";
    private String nextObjectIdTx = "1";
    //得到当前选中的列表信息
    private PaginationVO paginationVO;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_content;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
        views = new ArrayList<>();
        orderTransactionBeans = new ArrayList<>();
        orderRechargeBeans = new ArrayList<>();
        orderWithDrawBeans = new ArrayList<>();
        presenter = new OrderRecordPresenterImp(this);
        initTopTabData();
    }

    /**
     * 初始化顶部tab的数据以及相对应的界面信息
     */
    private void initTopTabData() {
        if (tabLayout == null) {
            return;
        }
        // 获取「交易」页面的内容
        if (presenter != null) {
            presenter.getRecord(Constants.OrderType.TX, nextObjectIdTx);
        }
        // 移除所有的view
        tabLayout.removeTabLayout();
        for (int i = 0; i < 3; i++) {
            //显示标题
            tabLayout.addTab(dataGenerationRegister.getOrderTopTitles(i), i);
        }
        orderViewOne = new OrderView(getContext());
        orderViewOne.setOrderTransactionAdapter(orderTransactionBeans);
        orderViewOne.setOnItemSelectListener(onItemSelectListener);
        views.add(orderViewOne);

        orderViewTwo = new OrderView(getContext());
        orderViewTwo.setOrderRechargeAdapter(orderRechargeBeans);
        orderViewTwo.setOnItemSelectListener(onItemSelectListener);
        views.add(orderViewTwo);


        orderViewThree = new OrderView(getContext());
        orderViewThree.setOrderWithDrawAdapter(orderWithDrawBeans);
        orderViewThree.setOnItemSelectListener(onItemSelectListener);
        views.add(orderViewThree);

        tabViewAdapter = new TabViewAdapter(views);
        viewPager.setAdapter(tabViewAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout.getTabLayout()));
        tabLayout.setupWithViewPager(viewPager, new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        if (presenter != null) {
                            presenter.getRecord(Constants.OrderType.TX, nextObjectIdTx);
                        }
                        orderViewOne.setOrderTransactionAdapter(orderTransactionBeans);
                        break;
                    case 1:
                        if (presenter != null) {
                            presenter.getRecord(Constants.OrderType.RECHARGE, nextObjectIdRecharge);
                        }

                        orderViewTwo.setOrderRechargeAdapter(orderRechargeBeans);

                        break;
                    case 2:
                        if (presenter != null) {
                            presenter.getRecord(Constants.OrderType.WITHDRAW, nextObjectIdWithDraw);
                        }
                        orderViewThree.setOrderWithDrawAdapter(orderWithDrawBeans);

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
    }

    private OnItemSelectListener onItemSelectListener = new OnItemSelectListener() {
        @Override
        public <T> void onItemSelect(T type, String from) {
            LogTool.d(TAG, from);
            if (type == null) {
                return;
            }
            if (type instanceof Long) {
                long memberOrderUid = (Long) type;
                switch (from) {
                    case Constants.From.ORDER_TRANSACTION:
                        presenter.cancelOrder(memberOrderUid);
                        break;
                    case Constants.From.ORDER_RECHARGE:
                        presenter.cancelOrder(memberOrderUid);
                        break;
                    case Constants.From.ORDER_WITHDRAW:
                        presenter.cancelOrder(memberOrderUid);
                        break;
                }
            }


        }
    };

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {
    }

    /**
     * 重置当前界面
     */
    public void resetView() {
        initTopTabData();
    }

    @Override
    public void getRecordFailure(String info) {
        showToast(info);
    }

    @Override
    public void getRecordSuccess(PaginationVO paginationVO) {
        LogTool.d(TAG, "PaginationVO:" + paginationVO);
        if (paginationVO != null) {
            this.paginationVO = paginationVO;
            //得到当前接口的页面信息
            String nextObjectId = paginationVO.getNextObjectId();
            int totalPageNumber = paginationVO.getTotalPageNumber();
            long totalObjectNumber = paginationVO.getTotalObjectNumber();
            int type = JsonTool.getInt(GsonTool.string(paginationVO.getObjectList()), "type", 0);
            LogTool.d(TAG, "当前的Type为：" + type);

            List<Object> objects = paginationVO.getObjectList();
            if (ListTool.isEmpty(objects)) {
                // TODO: 2019/1/11 暂时解析本地数据
                String json = ResourceTool.getJsonFromAssets(FilePathTool.getJsonFileContent());
                if (StringTool.notEmpty(json)) {
                    ResponseJson responseJson = GsonTool.convert(json, ResponseJson.class);
                    if (responseJson != null) {
                        PaginationVO paginationVO1 = responseJson.getPaginationVO();
                        if (paginationVO1 != null) {
                            switch (type) {
                                case Constants.OrderType.RECHARGE:
                                    orderRechargeBeans = GsonTool.convert(GsonTool.string(paginationVO1.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                                    }.getType());
                                    orderViewTwo.setOrderTransactionAdapter(orderRechargeBeans);
                                    nextObjectIdRecharge = paginationVO.getNextObjectId();
                                    break;
                                case Constants.OrderType.WITHDRAW:
                                    orderWithDrawBeans = GsonTool.convert(GsonTool.string(paginationVO1.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                                    }.getType());
                                    orderViewThree.setOrderWithDrawAdapter(orderWithDrawBeans);
                                    nextObjectIdWithDraw = paginationVO.getNextObjectId();
                                    break;
                                case Constants.OrderType.TX:
                                    orderTransactionBeans = GsonTool.convert(GsonTool.string(paginationVO1.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                                    }.getType());
                                    orderViewOne.setOrderTransactionAdapter(orderTransactionBeans);
                                    nextObjectIdTx = paginationVO.getNextObjectId();
                                    break;
                            }


                        }
                    }
                }
            }else{
                switch (type) {
                    case Constants.OrderType.RECHARGE:
                        orderRechargeBeans = GsonTool.convert(GsonTool.string(paginationVO.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                        }.getType());
                        orderViewTwo.setOrderTransactionAdapter(orderRechargeBeans);
                        nextObjectIdRecharge = paginationVO.getNextObjectId();
                        break;
                    case Constants.OrderType.WITHDRAW:
                        orderWithDrawBeans = GsonTool.convert(GsonTool.string(paginationVO.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                        }.getType());
                        orderViewThree.setOrderWithDrawAdapter(orderWithDrawBeans);
                        nextObjectIdWithDraw = paginationVO.getNextObjectId();
                        break;
                    case Constants.OrderType.TX:
                        orderTransactionBeans = GsonTool.convert(GsonTool.string(paginationVO.getObjectList()), new TypeToken<List<MemberOrderVO>>() {
                        }.getType());
                        orderViewOne.setOrderTransactionAdapter(orderTransactionBeans);
                        nextObjectIdTx = paginationVO.getNextObjectId();
                        break;
                }
            }




        }
    }

    @Override
    public void cancelOrderFailure(String info) {
        showToast(info);
    }

    @Override
    public void cancelOrderSuccess(MemberOrderVO memberOrderVO) {
        LogTool.d(TAG, "cancelOrderSuccess:" + memberOrderVO);
    }
}
