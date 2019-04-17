package io.bcaas.exchange.view.viewGroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/7
 * 自定义交易界面TabLayout视图
 */
public class TransactionTabLayout extends FrameLayout {
    private String TAG = TransactionTabLayout.class.getSimpleName();
    private TabLayout mTabLayout;
    private List<String> mTabList;
    private List<View> mCustomViewList;
    private int mSelectIndicatorColor;
    private int mSelectTextColor;
    private int mUnSelectTextColor;
    private int mIndicatorHeight;
    private int mIndicatorWidth;
    private int mTabMode;
    private boolean fixThree;//是否鎖定三個
    private int mTabTextSize;
    private Context context;
    private int tabSize = 3;


    public TransactionTabLayout(@NonNull Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public TransactionTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public TransactionTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TransactionTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void readAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseTabLayout);
        mSelectIndicatorColor = typedArray.getColor(R.styleable.BaseTabLayout_tabIndicatorColor, context.getResources().getColor(R.color.button_color));
        mUnSelectTextColor = typedArray.getColor(R.styleable.BaseTabLayout_tabTextColor, context.getResources().getColor(R.color.black_333333));
        mSelectTextColor = typedArray.getColor(R.styleable.BaseTabLayout_tabSelectTextColor, context.getResources().getColor(R.color.button_color));
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.BaseTabLayout_tabIndicatorHeight, 1);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.BaseTabLayout_tabIndicatorWidth, 0);
        mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.BaseTabLayout_tabTextSize, 16);
        mTabMode = typedArray.getInt(R.styleable.BaseTabLayout_tab_Mode, 2);
        fixThree = typedArray.getBoolean(R.styleable.BaseTabLayout_fixThree, false);
        typedArray.recycle();
    }

    private void init(Context context, AttributeSet attrs) {
        readAttr(context, attrs);

        mTabList = new ArrayList<>();
        mCustomViewList = new ArrayList<>();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tablayout_bcaas, this, true);
        mTabLayout = view.findViewById(R.id.bcaas_tab_view);//


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    if (view == null) {
                        return;
                    }
                    TextView text = (TextView) view.findViewById(R.id.tab_item_text);
                    View indicator = view.findViewById(R.id.tab_item_indicator);
                    if (i == tab.getPosition()) { // 选中状态
                        if (i == 1) {
                            int color=BaseApplication.context().getResources().getColor(R.color.button_color);
                            indicator.setBackgroundColor(color);
                            text.setTextColor(color);
                        } else {
                            int color=BaseApplication.context().getResources().getColor(R.color.green_18ac22);
                            indicator.setBackgroundColor(color);
                            text.setTextColor(color);
                        }
                        indicator.setVisibility(View.VISIBLE);
                    } else {// 未选中状态
                        text.setTextColor(mUnSelectTextColor);
                        indicator.setVisibility(View.INVISIBLE);

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

    public List<View> getCustomViewList() {
        return mCustomViewList;
    }

//    private TabLayout.OnTabSelectedListener onTabSelectedListener;

    public void addOnTabSelectedListener(TabLayout.OnTabSelectedListener onTabSelectedListener) {
        mTabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    /**
     * 与TabLayout 联动
     *
     * @param viewPager
     */
    public void setupWithViewPager(@Nullable ViewPager viewPager, TabLayout.OnTabSelectedListener onTabSelectedListener) {
        this.setupWithViewPager(BaseApplication.getScreenWidth(), viewPager, onTabSelectedListener);
    }

    /**
     * 与TabLayout 联动
     *
     * @param viewPager
     */
    public void setupWithViewPager(int width, @Nullable ViewPager viewPager, TabLayout.OnTabSelectedListener onTabSelectedListener) {
        mTabLayout.addOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager, this, onTabSelectedListener));
        measureLayoutParams(fixThree, width);
    }

    /**
     * 重新测量布局参数
     *
     * @param fixThree
     */
    public void measureLayoutParams(boolean fixThree, int screenWidth) {
        Class<?> tabLayout = mTabLayout.getClass();
        int widthTemp;
        if (fixThree) {
            if (tabSize <= 3) {
                widthTemp = screenWidth / tabSize;
            } else {
                widthTemp = (int) (screenWidth / 3.5);
            }
        } else {
            widthTemp = screenWidth / tabSize;
        }

        Field tabStrip = null;
        try {
            //INVALID_WIDTH
            tabStrip = tabLayout.getDeclaredField(Constants.TabLayout.SLIDING_TAB_INDICATOR);
            tabStrip.setAccessible(true);
            LinearLayout ll_tab = (LinearLayout) tabStrip.get(mTabLayout);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                LinearLayout.LayoutParams params;
                if (fixThree) {
                    if (tabSize <= 3) {
                        params = new LinearLayout.LayoutParams(widthTemp, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    } else {
                        if (i == ll_tab.getChildCount() - 1) {
                            params = new LinearLayout.LayoutParams(widthTemp + 25, LinearLayout.LayoutParams.MATCH_PARENT);
                        } else {
                            params = new LinearLayout.LayoutParams(widthTemp, LinearLayout.LayoutParams.MATCH_PARENT);
                        }
                    }
                } else {
                    params = new LinearLayout.LayoutParams(widthTemp, LinearLayout.LayoutParams.MATCH_PARENT, 1);

                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
    }


    /**
     * retrive TabLayout Instance
     *
     * @return
     */
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    /**
     * 添加tab
     *
     * @param tab
     */
    public void addTab(String tab, int index) {
        mTabList.add(tab);
        View customView = getTabView(getContext(), tab, mIndicatorWidth, mIndicatorHeight, mTabTextSize);
        mCustomViewList.add(customView);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(customView), index == 0);
        // 根据当前显示的个数来判断是否需要滚动
        mTabLayout.setTabMode(mTabList.size() <= tabSize ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
    }

    /**
     * 如果当前tabLayout不为空，那么移除内部已经加载的所有的View
     */
    public void removeTabLayout() {
        if (mTabLayout != null) {
            mTabLayout.removeAllTabs();
        }
    }

    public static class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

        private final ViewPager mViewPager;
        private final WeakReference<TransactionTabLayout> mTabLayoutRef;
        private final TabLayout.OnTabSelectedListener onTabSelectedListener;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager, TransactionTabLayout bcaasTabLayout, TabLayout.OnTabSelectedListener tabSelectedListener) {
            mViewPager = viewPager;
            mTabLayoutRef = new WeakReference<TransactionTabLayout>(bcaasTabLayout);
            this.onTabSelectedListener = tabSelectedListener;
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            if (this.onTabSelectedListener != null) {
                onTabSelectedListener.onTabSelected(tab);
            }
            mViewPager.setCurrentItem(tab.getPosition());
            TransactionTabLayout mTabLayout = mTabLayoutRef.get();
            if (mTabLayoutRef != null) {
                List<View> customViewList = mTabLayout.getCustomViewList();
                if (customViewList == null || customViewList.size() == 0) {
                    return;
                }
                for (int i = 0; i < customViewList.size(); i++) {
                    View view = customViewList.get(i);
                    if (view == null) {
                        return;
                    }
                    TextView text = (TextView) view.findViewById(R.id.tab_item_text);
                    View indicator = view.findViewById(R.id.tab_item_indicator);
                    if (i == tab.getPosition()) { // 选中状态
                        if (i == 1) {
                            int color=BaseApplication.context().getResources().getColor(R.color.button_color);
                            indicator.setBackgroundColor(color);
                            text.setTextColor(color);
                        } else {
                            int color=BaseApplication.context().getResources().getColor(R.color.green_18ac22);
                            indicator.setBackgroundColor(color);
                            text.setTextColor(color);
                        }
                        indicator.setVisibility(View.VISIBLE);
                    } else {// 未选中状态
                        text.setTextColor(mTabLayout.mUnSelectTextColor);
                        indicator.setVisibility(View.INVISIBLE);

                    }
                }
            }

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // No-op
            if (this.onTabSelectedListener != null) {
                onTabSelectedListener.onTabUnselected(tab);
            }
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // No-op
            if (this.onTabSelectedListener != null) {
                onTabSelectedListener.onTabReselected(tab);
            }
        }
    }

    /**
     * 获取Tab 显示的内容
     *
     * @param context
     * @param
     * @return
     */
    public View getTabView(Context context, String text, int indicatorWidth, int indicatorHeight, int textSize) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaction_tab_layout, null);
        TextView tabText = (TextView) view.findViewById(R.id.tab_item_text);
        View indicator = view.findViewById(R.id.tab_item_indicator);
        if (indicatorWidth > 0) {
            ViewGroup.LayoutParams layoutParams = indicator.getLayoutParams();
            layoutParams.width = indicatorWidth;
            layoutParams.height = indicatorHeight;
            indicator.setLayoutParams(layoutParams);
        }
        tabText.setTextSize(textSize);
        tabText.setText(text);
        return view;
    }

    /**
     * 设置选中的tab
     *
     * @param position
     */
    public void selectTab(int position) {
        if (mTabLayout != null) {
            mTabLayout.getTabAt(position).select();
        }
    }

    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }
}
