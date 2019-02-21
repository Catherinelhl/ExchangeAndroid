package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BasePresenterImp;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
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
 * 数据交互实现类：获取待出售列表
 */
public class ForSaleOrderListPresenterImp extends BasePresenterImp
        implements ForSaleOrderListContract.Presenter {

    private String TAG = ForSaleOrderListPresenterImp.class.getSimpleName();
    private ForSaleOrderListContract.View view;
    private TxInteractor txInteractor;

    private Disposable disposableGetOrderList;

    public ForSaleOrderListPresenterImp(ForSaleOrderListContract.View view) {
        super();
        this.view = view;
        this.txInteractor = new TxInteractor();
    }

    @Override
    public void getOrderList(String currencyUid, String currencyPaymentUid, String nextObjectId) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableGetOrderList);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
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
        boolean isRefresh = StringTool.equals(MessageConstants.DEFAULT_NEXT_OBJECT_ID, nextObjectId);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "getOrderList", requestJson);
        txInteractor.getOrderList(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableGetOrderList = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        LogTool.d(TAG, responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            PaginationVO paginationVOResponse = responseJson.getPaginationVO();
                            if (paginationVOResponse != null) {
                                view.getOrderListSuccess(paginationVOResponse, isRefresh);
                            } else {
                                view.getOrderListFailure(MessageConstants.EMPTY, isRefresh);
                            }
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2004) {
                                    view.getOrderListFailure(getString(R.string.no_more_info), isRefresh);
                                } else if (code == MessageConstants.CODE_2027) {
                                    view.getOrderListFailure(getString(R.string.data_format_exception), isRefresh);
                                } else if (code == MessageConstants.CODE_2041) {
                                    view.getOrderListFailure(getString(R.string.data_format_exception), isRefresh);
                                } else {
                                    view.getOrderListFailure(getString(R.string.get_data_failure), isRefresh);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.getOrderListFailure(getString(R.string.get_data_failure), isRefresh);
                        disposeDisposable(disposableGetOrderList);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableGetOrderList);
                    }
                });
    }

}
