package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.CoinMarketCapBean;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.GetCoinMarketCapContract;
import io.bcaas.exchange.ui.interactor.MainInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/18
 */
public class GetCoinMarketCapPresenterImp implements GetCoinMarketCapContract.Presenter {
    private String TAG = GetCoinMarketCapPresenterImp.class.getSimpleName();

    private GetCoinMarketCapContract.View view;
    private MainInteractor interactor;

    public GetCoinMarketCapPresenterImp(GetCoinMarketCapContract.View view) {
        super();
        this.view = view;
        this.interactor = new MainInteractor();
    }


    @Override
    public void getCoinNameList() {
        interactor.getCoinNameList(GsonTool.beanToRequestBody(new RequestJson()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getCoinNameList:", responseJson);
                        if (responseJson == null) {
                            view.getCoinNameListFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            List<CurrencyListVO> currencyListVOS = responseJson.getCurrencyListVOList();
                            if (ListTool.noEmpty(currencyListVOS)) {
                                view.getCoinNameListSuccess(currencyListVOS);
                            } else {
                                view.getCoinNameListFailure(responseJson.getMessage());
                            }

                        } else {
                            int code = responseJson.getCode();
                            view.getCoinNameListFailure(responseJson.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getCoinNameListFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getCoinMarketCap(String coinName, long startTime, long endTime) {
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

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getCoinMarketCap:", responseJson);
                        if (responseJson == null) {
                            view.getCoinMarketCapFailure(MessageConstants.EMPTY);
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
                            view.getCoinMarketCapFailure(responseJson.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getCoinMarketCapFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
