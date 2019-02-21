package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.ExchangeBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.GetCurrencyUSDPriceContract;
import io.bcaas.exchange.ui.interactor.MainInteractor;
import io.bcaas.exchange.vo.LoginInfoVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/17
 * 数据交互实现类：获取当前币种与USD之间的汇率数据
 */

@Deprecated
public class GetCurrencyUSDPricePresenterImp implements GetCurrencyUSDPriceContract.Presenter {
    private String TAG = GetCurrencyUSDPricePresenterImp.class.getSimpleName();
    private GetCurrencyUSDPriceContract.View view;
    private MainInteractor interactor;

    public GetCurrencyUSDPricePresenterImp(GetCurrencyUSDPriceContract.View view) {
        super();
        this.view = view;
        interactor = new MainInteractor();
    }

    @Override
    public void getCurrencyUSDPrice(ExchangeBean exchangeBean) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);

        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        requestJson.setExchangeBean(exchangeBean);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "getCurrencyUSDPrice", requestJson);

        interactor.getCurrencyUSDPrice(GsonTool.beanToRequestBody(requestJson))
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
                            //得到当前返回的数值
                            ExchangeBean exchangeBeanResponse = responseJson.getExchangeBean();
                            if (exchangeBeanResponse == null) {
                                view.getCurrencyUSDPriceFailure(responseJson.getMessage());
                            } else {
                                view.getCurrencyUSDPriceSuccess(exchangeBeanResponse);

                            }
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2006) {
                                view.getCurrencyUSDPriceFailure(MessageConstants.ERROR_EMAIL_ALREADY_REGISTER);

                            } else {
                                view.getCurrencyUSDPriceFailure(responseJson.getMessage());
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.getCurrencyUSDPriceFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }

}
