package io.bcaas.exchange.tools.timer;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/25
 * 一个管理时间间隔的工具
 */
public class IntervalTimerTool {

    private static String TAG = IntervalTimerTool.class.getSimpleName();

    private static Disposable disposableTimer;

    public static Observable<Integer> countDownTimer(int time) {
        if (time < 0) {
            time = 0;
        }
        int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(increaseTime -> countTime - increaseTime.intValue()).take(countTime + 1);
    }

    /**
     * 关闭背景執行獲取帳戶餘額
     */

    public static void disposeRequest(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
