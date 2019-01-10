package io.bcaas.exchange.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.bcaas.exchange.maker.DataGenerationRegister;
import io.bcaas.exchange.manager.SoftKeyBroadManager;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.otto.OttoTool;
import io.bcaas.exchange.ui.contracts.BaseContract;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/4
 * 所有Phone's Fragment 的基類
 */
public abstract class BaseFragment extends Fragment implements BaseContract {

    private String TAG = BaseFragment.class.getSimpleName();
    private View rootView;
    protected Context context;
    protected Activity activity;
    private Unbinder unbinder;
    protected SoftKeyBroadManager softKeyBroadManager;
    /*监管当前fragment的状态：是否准备好；是否第一次可见；是否第一次不可见*/
    protected boolean isPrepared, isFirstVisible, isFirstInvisible;
    protected DataGenerationRegister dataGenerationRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        OttoTool.getInstance().register(this);
        context = getContext();
        activity = getActivity();
        if (activity != null) {
            getArgs(activity.getIntent().getExtras());
        }
        dataGenerationRegister = new DataGenerationRegister();
        initViews(view);
        initListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    private synchronized void initPrepare() {
        if (!isPrepared) {
            isPrepared = true;
        }
    }


    public abstract int getLayoutRes();//得到当前的layoutRes

    public abstract void initViews(View view);

    public abstract void getArgs(Bundle bundle);

    public abstract void initListener();

//    protected abstract void DestroyViewAndThing();


    public void showToast(String info) {
        if (!checkActivityState()) {
            return;
        }
        ((BaseActivity) activity).showToast(info);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OttoTool.getInstance().unregister(this);
    }


    public void intentToActivity(Class classTo) {//跳转到另外一个界面
        intentToActivity(null, classTo, false);
    }

    public void intentToActivity(Bundle bundle, Class classTo, Boolean finishFrom) {//跳转到另外一个界面
        if (activity == null) {
            return;
        }
        ((BaseActivity) activity).intentToActivity(bundle, classTo, finishFrom);
    }


    /*隐藏当前键盘*/
    public void hideSoftKeyboard() {
        if (activity != null) {
            ((BaseActivity) activity).hideSoftKeyboard();
        }
    }


    protected SoftKeyBroadManager.SoftKeyboardStateListener softKeyboardStateListener = new SoftKeyBroadManager.SoftKeyboardStateListener() {
        @Override
        public void onSoftKeyboardOpened(int keyboardHeightInPx, int bottom) {
            LogTool.d(TAG, keyboardHeightInPx);
        }

        @Override
        public void onSoftKeyboardClosed() {

        }
    };

    @Override
    public void onDestroy() {
//        DestroyViewAndThing();
        if (softKeyBroadManager != null && softKeyboardStateListener != null) {
            softKeyBroadManager.removeSoftKeyboardStateListener(softKeyboardStateListener);
        }
        unbinder.unbind();
        super.onDestroy();
    }


    public boolean checkActivityState() {
        return activity != null
                && !activity.isFinishing()
                && isAdded();
    }

    // 检测当前的activity fragment 是否被销毁
    protected boolean checkActivityAndFragmentState() {
        return activity != null
                && !activity.isFinishing()
                && isAdded()
                && (getParentFragment() != null && getParentFragment().getUserVisibleHint());
    }

    @Override
    public void showLoading() {
        if (activity != null) {
            ((BaseActivity) activity).showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (activity != null) {
            ((BaseActivity) activity).hideLoading();
        }
    }
}
