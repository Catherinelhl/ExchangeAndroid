package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BasePresenterImp;
import io.bcaas.exchange.bean.CoinMarketCapBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.GetCoinMarketCapContract;
import io.bcaas.exchange.ui.interactor.MainInteractor;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/18
 * 数据交互实现类：获取币种的市场信息形成Chart图
 */
public class GetCoinMarketCapPresenterImp extends BasePresenterImp
        implements GetCoinMarketCapContract.Presenter {
    private String TAG = GetCoinMarketCapPresenterImp.class.getSimpleName();

    private GetCoinMarketCapContract.View view;
    private MainInteractor interactor;
    private Disposable disposableGetCoinMarketCap;

    public GetCoinMarketCapPresenterImp(GetCoinMarketCapContract.View view) {
        super();
        this.view = view;
        this.interactor = new MainInteractor();
    }

    @Override
    public void getCoinMarketCap(String coinName, long startTime, long endTime) {
        disposeDisposable(disposableGetCoinMarketCap);
        RequestJson requestJson = new RequestJson();
        CoinMarketCapBean coinMarketCapBean = new CoinMarketCapBean();
        coinMarketCapBean.setCoinName(coinName);
        coinMarketCapBean.setStartTime(startTime);
        coinMarketCapBean.setEndTime(endTime);
        requestJson.setCoinMarketCapBean(coinMarketCapBean);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "getCoinMarketCap", requestJson);

        interactor.getCoinMarketCap(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableGetCoinMarketCap = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getCoinMarketCap:", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            CoinMarketCapBean coinMarketCapBean = responseJson.getCoinMarketCapBean();
                            if (coinMarketCapBean != null) {
                                view.getCoinMarketCapSuccess(coinMarketCapBean);
                            } else {
                                view.getCoinMarketCapFailure(responseJson.getMessage());
                            }
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2087) {
                                view.getCoinMarketCapFailure(MessageConstants.EMPTY);
                            } else if (code == MessageConstants.CODE_2077
                                    || code == MessageConstants.CODE_2078
                                    || code == MessageConstants.CODE_2079) {
                                view.getCoinMarketCapFailure(getString(R.string.data_format_exception));
                            } else {
                                view.getCoinMarketCapFailure(responseJson.getMessage());

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getCoinMarketCapFailure(e.getMessage());
                        disposeDisposable(disposableGetCoinMarketCap);
                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableGetCoinMarketCap);

                    }
                });
    }
    @Override
    public void cancelSubscribe() {
        disposeDisposable(disposableGetCoinMarketCap);
    }
}
