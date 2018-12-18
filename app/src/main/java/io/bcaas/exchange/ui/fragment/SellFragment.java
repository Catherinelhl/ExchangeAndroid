package io.bcaas.exchange.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseFragment;
import io.bcaas.exchange.maker.DataGenerationRegister;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.view.textview.RichTextView;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/10
 * <p>
 * 賣出
 */
public class SellFragment extends BaseFragment {
    private String TAG = SellFragment.class.getSimpleName();

    @BindView(R.id.tv_text)
    TextView tvText;
//    @BindView(R.id.rt_text)
//    RichTextView rtText;

    @Override
    protected void onFirstUserVisible() {
        LogTool.i(TAG, "onFirstUserVisible");
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
    public int getLayoutRes() {
        return R.layout.fragment_sell_layout;
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
