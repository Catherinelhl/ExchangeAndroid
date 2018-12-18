package io.bcaas.exchange.view.textview;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.TextView;
import io.bcaas.exchange.R;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/18
 * <p>
 * 实现渐变的TextView
 */
public class GradientTextView extends TextView {
    private Context context;
    private int width;
    /**
     * 移动距离
     */
    private int translateWidth;
    private Paint paint;
    private Matrix matrix;
    private LinearGradient linearGradient;

    public GradientTextView(Context context) {
        super(context);
        this.context = context;
    }

    public GradientTextView(Context context,
                            AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public GradientTextView(Context context,
                            AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    protected void onLayout(boolean changed,
//                            int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        if (changed) {
//            getPaint().setShader(new LinearGradient(
//                    0, 0, 0, getHeight(),
//                    context.getResources().getColor(R.color.theme_FDD400),
//                    context.getResources().getColor(R.color.theme_FF6400),
//                    Shader.TileMode.MIRROR));
//        }
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != 0) {
            width = w;
        } else {
            width = getMeasuredWidth();
        }
        paint = getPaint();
        linearGradient = new LinearGradient(-width, 0, 0, 0,
                new int[]{R.color.theme_FDD400,
                        R.color.theme_FF6400},
                new float[]{0, 0.5f},
                Shader.TileMode.CLAMP);
        //添加渲染
        paint.setShader(linearGradient);
        paint.setColor(context.getResources().getColor(R.color.theme_FF6400));
        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (matrix == null)
            return;
        //每次移动原来宽的15分之一
        translateWidth += width / 15;
        //表示刚刚移动 了width个宽度 即 正好包含了整个textview 的时候还原
        if (translateWidth > width * 2) {
            translateWidth -= width * 2;
        }
        //移动
        matrix.setTranslate(translateWidth, 0);
        linearGradient.setLocalMatrix(matrix);
        postInvalidateDelayed(100);
    }

}
