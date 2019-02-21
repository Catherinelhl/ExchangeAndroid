package io.bcaas.exchange.view.viewGroup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * @author catherine.brainwilliam
 * @since 2019/2/15
 */
public class LoadingSwipeRefreshLayout extends SwipeRefreshLayout {
    public LoadingSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public LoadingSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }
}
