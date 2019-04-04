package io.bcaas.exchange.base;

import android.content.Context;
import android.content.res.Resources;
import io.reactivex.disposables.Disposable;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/16
 * 数据交互实现类：所有数据交互类的实现类的基类
 */
public abstract class BasePresenterImp implements BaseContract.Presenter {
    private String TAG = BasePresenterImp.class.getSimpleName();
    protected Context context;

    public BasePresenterImp() {
        this.context = BaseApplication.context();

    }

    protected String getString(int resId) {
        return context.getString(resId);
    }

    protected Resources getString() {
        return context.getResources();
    }
//    // 处理点击事件的方法
//    protected <T> Observable.Transformer<T, T> timer() {
//        return observable -> observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    //    protected <T> Observable.Transformer<T, T> applyIoSchedulers() {
//        return tObservable -> tObservable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
    protected void disposeDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
