package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.GetCoinNameListContract;
import io.bcaas.exchange.ui.interactor.MainInteractor;
import io.bcaas.exchange.vo.CurrencyListVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/21
 */
public class GetCoinNameListPresenterImp extends BasePresenterImp
        implements GetCoinNameListContract.Presenter {

    private String TAG = GetCoinNameListPresenterImp.class.getSimpleName();
    private GetCoinNameListContract.View view;
    private MainInteractor interactor;
    private Disposable disposableGetCoinNameList;

    public GetCoinNameListPresenterImp(GetCoinNameListContract.View view) {
        super();
        this.view = view;
        this.interactor = new MainInteractor();
    }

    @Override
    public void getCoinNameList() {
        disposeDisposable(disposableGetCoinNameList);
        interactor.getCoinNameList(GsonTool.beanToRequestBody(new RequestJson()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableGetCoinNameList = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getCoinNameList:", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            List<CurrencyListVO> currencyListVOS = responseJson.getCurrencyListVOList();
                            if (ListTool.noEmpty(currencyListVOS)) {
                                //将当前获取到的值存储起来
                                BaseApplication.setCurrencyListWithCoinName(currencyListVOS);
                                view.getCoinNameListSuccess(currencyListVOS);
                            } else {
                                view.getCoinNameListFailure(getString(R.string.no_data_info));
                            }

                        } else {
                            view.getCoinNameListFailure(responseJson.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getCoinNameListFailure(e.getMessage());
                        disposeDisposable(disposableGetCoinNameList);
                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableGetCoinNameList);

                    }
                });

    }
}
