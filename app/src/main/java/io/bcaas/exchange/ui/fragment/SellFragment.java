package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.view.textview.RichText;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 賣出
 */
public class SellFragment extends BaseFragment {
    @BindView(R.id.top_tab_layout)
    TabLayout topTabLayout;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.rt_text)
    RichText rtText;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sell_layout;
    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {

    }

}
