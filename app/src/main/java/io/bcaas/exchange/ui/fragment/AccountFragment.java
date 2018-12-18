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
import io.bcaas.exchange.tools.LogTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 帳戶
 */
public class AccountFragment extends BaseFragment {
    private String TAG = AccountFragment.class.getSimpleName();

    @BindView(R.id.tv_text)
    TextView tvText;

    @Override
    protected void onFirstUserVisible() {
        LogTool.d(TAG, "onFirstUserVisible");
        isFirstVisible = true;
    }

    @Override
    protected void onFirstUserInvisible() {
        LogTool.d(TAG, "onFirstUserInvisible");
        isFirstInvisible = true;
    }

    @Override
    protected void onUserVisible() {
        LogTool.i(TAG, "onUserVisible");

    }

    @Override
    protected void onUserInvisible() {
        LogTool.i(TAG, "onUserInvisible");

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogTool.d(TAG, "setUserVisibleHint:" + isVisibleToUser);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_account_layout;
    }

    @Override
    public void initViews(View view) {
        isPrepared = true;
    }

    @Override
    public void getArgs(Bundle bundle) {

    }

    @Override
    public void initListener() {

    }
}
