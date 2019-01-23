package io.bcaas.exchange.view.viewGroup;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import io.bcaas.exchange.R;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/23
 * <p>
 * 一个自带加载过程的图片
 */
public class ImageViewWithLoading extends RelativeLayout {

    private Context context;
    public ImageViewWithLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_safety_center, this, true);
        ButterKnife.bind(view);
    }
}
