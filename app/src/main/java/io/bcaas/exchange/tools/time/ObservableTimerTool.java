package io.bcaas.exchange.tools.time;

import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.listener.ObservableTimerListener;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static io.reactivex.Observable.timer;

/**
 * @author catherine.brainwilliam
 * @since 2018/10/20
 * <p>
 * 工具類：时间倒计时、定时管理工具
 */
public class ObservableTimerTool {

    private static String TAG = ObservableTimerTool.class.getSimpleName();

    /*关闭通知显示的观察者*/
    private static Disposable countDownDisposable;

    /**
     * 关闭计时，通过给定的时间
     *
     * @param stayTime                停留的时间
     * @param observableTimerListener
     */
    public static void countDownTimerBySetTime(long stayTime, ObservableTimerListener observableTimerListener) {
        countDownTimerBySetTime(stayTime, TimeUnit.SECONDS, observableTimerListener);
    }

    public static void countDownTimerBySetTime(long stayTime, TimeUnit timeUnit, ObservableTimerListener observableTimerListener) {
        timer(stayTime, timeUnit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        countDownDisposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (observableTimerListener != null) {
                            observableTimerListener.timeUp(Constants.TimerType.BrandTimer);
                        }
                        //关闭计时
                        if (countDownDisposable != null) {
                            countDownDisposable.dispose();
                        }
                    }
                });
    }

}
