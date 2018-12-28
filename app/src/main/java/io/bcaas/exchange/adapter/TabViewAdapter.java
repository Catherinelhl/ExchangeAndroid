package io.bcaas.exchange.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import io.bcaas.exchange.maker.DataGenerationRegister;
import io.bcaas.exchange.tools.StringTool;

import java.util.List;

public class TabViewAdapter extends PagerAdapter {
    private List<View> views;
    private String type;

    public TabViewAdapter(List<View> views, String type) {
        this.views = views;
        this.type = type;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        ((ViewPager) container).addView(view, 0);
        return view;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    //设置tablayout标题
    @Override
    public CharSequence getPageTitle(int position) {
        if (StringTool.equals(type, "2")) {
            return new DataGenerationRegister().getOrderTopTitles(position);

        } else
            return new DataGenerationRegister().getTabTopTitle(position);

    }
}