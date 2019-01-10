package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.ForSaleOrderListContract;
import io.bcaas.exchange.ui.interactor.TxInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 */
public class ForSaleOrderListPresenterImp implements ForSaleOrderListContract.Presenter {

    private String TAG = ForSaleOrderListPresenterImp.class.getSimpleName();
    private ForSaleOrderListContract.View view;
    private TxInteractor txInteractor;

    public ForSaleOrderListPresenterImp(ForSaleOrderListContract.View view) {
        super();
        this.view = view;
        this.txInteractor = new TxInteractor();
    }

    @Override
    public void getOrderList(String currencyUid, String currencyPaymentUid, String nextObjectId) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        requestJson.setMemberVO(memberVO);

        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        MemberOrderVO memberOrderVO = new MemberOrderVO();
        CurrencyListVO currencyListVO = new CurrencyListVO();
        currencyListVO.setCurrencyUid(currencyUid);
        CurrencyListVO currencyListVOPayment = new CurrencyListVO();
        currencyListVOPayment.setCurrencyUid(currencyPaymentUid);
        memberOrderVO.setCurrencyListVO(currencyListVO);
        memberOrderVO.setPaymentCurrencyUid(currencyListVOPayment);
        requestJson.setMemberOrderVO(memberOrderVO);

        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setNextObjectId(nextObjectId);
        requestJson.setPaginationVO(paginationVO);
        LogTool.d(TAG, "getOrderList:" + requestJson);
        txInteractor.getOrderList(GsonTool.beanToRequestBody(requestJson))
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
                            view.getOrderListFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            PaginationVO paginationVOResponse = responseJson.getPaginationVO();
                            if (paginationVOResponse != null) {
                                view.getOrderListSuccess(paginationVO);
                            } else {
                                view.getOrderListFailure(MessageConstants.EMPTY);

                            }
                        } else {
                            int code = responseJson.getCode();
//                            if (code == MessageConstants.CODE_2019) {
                            //    {"success":false,"code":2019,"message":"AccessToken expire."}
                            view.getOrderListFailure(responseJson.getMessage());
//                            } else {

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getOrderListFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
