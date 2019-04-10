package io.bcaas.exchange.http.observer;
/*
+--------------+---------------------------------
+ author       +   Catherine Liu
+--------------+---------------------------------
+ since        +   2019/4/9 18:01
+--------------+---------------------------------
+ projectName  +   ExchangeAndroid
+--------------+---------------------------------
+ packageName  +   io.bcaas.exchange.http.observer
+--------------+---------------------------------
+ description  +  自定义一个网络请求共享处理的观察者
+--------------+---------------------------------
+ version      +  
+--------------+---------------------------------
*/


import android.content.Context;
import io.bcaas.exchange.R;
import io.bcaas.exchange.view.dialog.LoadingDialog;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseHttpObserver<T> implements Observer<T> {
    private LoadingDialog loadingDialog;
    private Context context;

    public BaseHttpObserver(Context context) {
        this.context = context;
        showLoading();
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onError(Throwable e) {
        hideLoading();
    }

    @Override
    public void onComplete() {
        hideLoading();
    }

    private void showLoading() {
        hideLoading();
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setProgressBarColor(context.getResources().getColor(R.color.button_color));
        loadingDialog.show();
    }

    private void hideLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog.cancel();
            loadingDialog = null;

        }
    }
}
