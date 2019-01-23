package io.bcaas.exchange.view.viewGroup;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.bcaas.exchange.R;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/23
 * <p>
 * 一个自带加载过程的图片
 */
public class ImageViewWithLoading extends RelativeLayout {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private Context context;

    public ImageViewWithLoading(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.imageview_with_loading, this, true);
        ButterKnife.bind(view);
    }

    /**
     * 显示进度条
     */
    public void showLoading() {
        if (pbLoading != null) {
            pbLoading.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏进度条
     */
    public void hideLoading() {
        if (pbLoading != null) {
            pbLoading.setVisibility(GONE);
        }
    }

    /**
     * 设置图片
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        if (img != null) {
            img.setImageBitmap(bitmap);
        }
        hideLoading();
    }


    /**
     * 设置图片
     *
     * @param res
     */
    public void setSrc(int res) {
        if (img != null) {
            img.setImageResource(res);
        }
        hideLoading();
    }

    /**
     * 设置图片的宽高
     *
     * @param width
     * @param height
     */
    public void setImageMeasure(int width, int height) {
        if (img != null) {
            ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            img.setLayoutParams(layoutParams);
        }
    }
}
