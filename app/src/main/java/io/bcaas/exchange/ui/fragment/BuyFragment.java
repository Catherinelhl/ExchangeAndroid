package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 買進
 */
public class BuyFragment extends BaseFragment {
    @BindView(R.id.tv_text)
    TextView tvText;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_buy_layout;
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
