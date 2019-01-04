package io.bcaas.exchange.maker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.SafetyCenterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/4
 * <p>
 * 数据生成寄存器
 */
public class DataGenerationRegister {

    //底部tab的标题集合
    private List<String> tabTitles = new ArrayList<>();
    //底部tab的图标集合
    private List<Integer> tabDrawables = new ArrayList<>();
    //底部tab的选中图标集合
    private List<Integer> tabFocusDrawables = new ArrayList<>();
    //顶部币种标题集合
    private List<String> tabTopTitles = new ArrayList<>();
    //顶部订单标题集合
    private List<String> tabOrderTopTitles = new ArrayList<>();
    //底部tab的数量
    private int tabBottomTitleCount;
    //顶部tab的数量
    private int tabTopTitleCount;
    //顶部tab 订单页面的标题数量
    private int tabOrderTopTitleCount;

    //安全中心列表项
    private List<SafetyCenterBean> safetyCenterBeans = new ArrayList<>();

    /**
     * 初始化安全中心数据
     */
    private void initSafetyCenterData() {
        SafetyCenterBean safetyCenterBean = new SafetyCenterBean();
        safetyCenterBean.setTabType("登录密码");
    }

    public DataGenerationRegister() {
        super();
        initData();
    }

    private void initData() {
        //初始化底部栏文本数据
        tabTitles.add(BaseApplication.context().getResources().getString(R.string.buy_in));
        tabTitles.add(BaseApplication.context().getString(R.string.sell_out));
        tabTitles.add(BaseApplication.context().getString(R.string.order));
        tabTitles.add(BaseApplication.context().getString(R.string.account));
        tabBottomTitleCount = tabTitles.size();

        //初始化顶部栏数据
        tabTopTitles.add("BTC");
        tabTopTitles.add("ETH");
        tabTopTitles.add("ZBB");
        tabTopTitleCount = tabTopTitles.size();

        //初始化Order界面top标题集合
        tabOrderTopTitles.add("交易");
        tabOrderTopTitles.add("充值");
        tabOrderTopTitles.add("提现");
        tabOrderTopTitleCount = tabOrderTopTitles.size();

        //初始化底部栏图标数据
        tabDrawables.add(R.mipmap.icon_buy_in);
        tabDrawables.add(R.mipmap.icon_sell_out);
        tabDrawables.add(R.mipmap.icon_order);
        tabDrawables.add(R.mipmap.icon_account);
        tabFocusDrawables.add(R.mipmap.icon_buy_in_f);
        tabFocusDrawables.add(R.mipmap.icon_sell_out_f);
        tabFocusDrawables.add(R.mipmap.icon_order_f);
        tabFocusDrawables.add(R.mipmap.icon_account_f);
    }

    /**
     * 根据底部栏当前的位置返回当前位置信息
     *
     * @param position
     * @return
     */
    public String getTabTitle(int position) {

        if (position >= tabBottomTitleCount) {
            return "";
        }
        return tabTitles.get(position);
    }

    /**
     * 根据当前顶部栏的位置返回当前位置上的标题信息
     *
     * @param position
     * @return
     */
    public String getTabTopTitle(int position) {
        if (position >= tabTopTitles.size()) {
            return "";
        }
        return tabTopTitles.get(position);
    }

    /**
     * 根据当前底部栏的位置返回当前位置上图标信息
     *
     * @param position
     * @return
     */
    public int getTabDrawable(int position, boolean isSelect) {
        if (position >= tabBottomTitleCount) {
            return 0;
        }
        return isSelect ? tabFocusDrawables.get(position) : tabDrawables.get(position);
    }

    /**
     * 根据位置下标，是否是选中的状态
     *
     * @param i
     * @param isSelect
     * @return
     */
    public Drawable getDrawableTop(Context context, int i, boolean isSelect) {
        Drawable top = context.getResources().getDrawable(getTabDrawable(i, isSelect));
        return top;

    }

    /**
     * 返回当前订单页面的顶部标题
     *
     * @param position
     * @return
     */
    public String getOrderTopTitles(int position) {
        if (position >= tabOrderTopTitleCount) {
            return "";
        }
        return tabOrderTopTitles.get(position);
    }

    public int getTabTopTitleCount() {
        return tabTopTitleCount;
    }

    public int getTabOrderTopTitleCount() {
        return tabOrderTopTitleCount;
    }
}
