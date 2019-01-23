package io.bcaas.exchange.tools.otto;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;
import io.bcaas.exchange.tools.LogTool;

/**
 * @author catherine.brainwilliam
 * @since 2018/8/17
 * <p>
 * 工具類：事件監聽訂閱
 */
public class OttoTool extends Bus {
    private String TAG = OttoTool.class.getSimpleName();

    /**
     * 通过单例模式返回唯一的bus对象,而且重写父类的post方法,通过handler实现任意线程可以调用
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private volatile static Bus bus = null;

    private OttoTool() {
        super();
    }

    @Override
    public void post(Object event) {
        LogTool.d(TAG, "post:" + event);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(() -> OttoTool.super.post(event));
        }
    }

    public static Bus getInstance() {
        if (bus == null) {
            synchronized (OttoTool.class) {
                bus = new Bus();
            }
        }
        return bus;
    }
}
