package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.MainContract;
import io.bcaas.exchange.ui.interactor.MainInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 注册
 */
public class MainPresenterImp implements MainContract.Presenter {
    private String TAG = MainPresenterImp.class.getSimpleName();
    private MainContract.View view;
    private MainInteractor mainInteractor;

    public MainPresenterImp(MainContract.View view) {
        super();
        this.view = view;
        mainInteractor = new MainInteractor();
    }

    @Override
    public void getCurrencyUSDPrice(String memberId, ExchangeBean exchangeBean) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        requestJson.setMemberVO(memberVO);
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setMemberVO(memberVO);
        requestJson.setLoginInfoVO(loginInfoVO);
        requestJson.setExchangeBean(exchangeBean);
        LogTool.d(TAG, requestJson);

        mainInteractor.getCurrencyUSDPrice(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        LogTool.d(TAG, responseJson);
                        if (responseJson == null) {
                            view.getCurrencyUSDPriceFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.getCurrencyUSDPriceSuccess(responseJson.getExchangeBean().getPriceUSD());
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2006) {
                                view.getCurrencyUSDPriceFailure(MessageConstants.CODE_2006_MSG);

                            } else {
                                view.getCurrencyUSDPriceFailure(responseJson.getMessage());

                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getCurrencyUSDPriceFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
