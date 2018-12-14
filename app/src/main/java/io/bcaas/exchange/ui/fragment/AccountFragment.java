package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 帳戶
 */
public class AccountFragment extends BaseFragment {

    @BindView(R.id.tv_text)
    TextView tvText;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_account_layout;
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
