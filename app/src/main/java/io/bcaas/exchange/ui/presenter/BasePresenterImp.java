package io.bcaas.exchange.ui.presenter;

import android.content.Context;
import android.content.res.Resources;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.ui.contracts.BaseContract;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/16
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

}
