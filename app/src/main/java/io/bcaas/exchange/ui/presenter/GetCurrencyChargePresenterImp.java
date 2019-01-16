package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.GetCurrencyChargeContract;
import io.bcaas.exchange.ui.interactor.TxInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/11
 * <p>
 * 取得幣種的手續費
 */
public class GetCurrencyChargePresenterImp implements GetCurrencyChargeContract.Presenter {
    private String TAG = GetCurrencyChargePresenterImp.class.getSimpleName();

    private GetCurrencyChargeContract.View view;
    private TxInteractor txInteractor;

    public GetCurrencyChargePresenterImp(GetCurrencyChargeContract.View view) {
        super();
        this.view = view;
        txInteractor = new TxInteractor();
    }

    /**
     * 取得币种的手续费
     */
    @Override
    public void getCurrencyCharge(String currencyUid) {
        if (StringTool.isEmpty(currencyUid)) {
            return;
        }
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        CurrencyListVO currencyListVORequest = new CurrencyListVO();
        currencyListVORequest.setCurrencyUid(currencyUid);
        requestJson.setCurrencyListVO(currencyListVORequest);

        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "getCurrencyCharge", requestJson);

        txInteractor.getCurrencyCharge(GsonTool.beanToRequestBody(requestJson))
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
                            view.getCurrencyChargeFailure(MessageConstants.EMPTY);

                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            CurrencyListVO currencyListVOResponse = responseJson.getCurrencyListVO();
                            view.getCurrencyChargeSuccess(currencyListVOResponse);
                        } else {
                            view.httpException(responseJson);
                            view.getCurrencyChargeFailure(responseJson.getMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        LogTool.e(TAG, e.getMessage());
                        view.getCurrencyChargeFailure(e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();

                    }
                });
    }
}
