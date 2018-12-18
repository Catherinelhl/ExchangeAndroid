package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.view.View;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.tools.LogTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 *  訂單
 */
public class OrderFragment extends BaseFragment {
    private String TAG = OrderFragment.class.getSimpleName();

    @Override
    protected void onFirstUserVisible() {
        LogTool.i(TAG,"onFirstUserVisible");
        isFirstVisible=true;
    }
    @Override
    protected void onFirstUserInvisible() {
        LogTool.d(TAG, "onFirstUserInvisible");
        isFirstInvisible = true;
    }
    @Override
    protected void onUserVisible() {
        LogTool.i(TAG,"onUserVisible");

    }

    @Override
    protected void onUserInvisible() {
        LogTool.i(TAG,"onUserInvisible");

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_order_layout;
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
