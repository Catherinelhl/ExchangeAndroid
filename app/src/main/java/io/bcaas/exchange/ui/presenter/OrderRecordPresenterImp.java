package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BasePresenterImp;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.ui.contracts.OrderRecordContract;
import io.bcaas.exchange.ui.interactor.TxInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * 数据交互实现类：订单记录
 */
public class OrderRecordPresenterImp extends BasePresenterImp implements OrderRecordContract.Presenter {
    private String TAG = OrderRecordPresenterImp.class.getSimpleName();
    private OrderRecordContract.View view;
    private TxInteractor txInteractor;
    private Disposable disposableGetRecord, disposableCancelOrder;

    public OrderRecordPresenterImp(OrderRecordContract.View view) {
        super();
        this.view = view;
        txInteractor = new TxInteractor();
    }


    @Override
    public void getRecord(int type, String nextObjectId) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableGetRecord);
        //显示加载框
        view.showLoading();
        //组装数据
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);


        MemberOrderVO memberOrderVO = new MemberOrderVO();
        memberOrderVO.setType(type);
        requestJson.setMemberOrderVO(memberOrderVO);

        PaginationVO paginationVO = new PaginationVO();
        paginationVO.setNextObjectId(nextObjectId);
        requestJson.setPaginationVO(paginationVO);
        boolean isRefresh = StringTool.equals(nextObjectId, MessageConstants.DEFAULT_NEXT_OBJECT_ID);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "getRecord", requestJson);

        txInteractor.getRecord(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableGetRecord = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "getRecord", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            PaginationVO paginationVOResponse = responseJson.getPaginationVO();
                            if (paginationVOResponse != null) {
                                view.getRecordSuccess(paginationVOResponse, isRefresh);
                            } else {
                                view.getRecordFailure(getString(R.string.no_data_info), isRefresh);
                            }

                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2004) {
                                    view.getRecordFailure(getString(R.string.no_more_info), isRefresh);
                                } else if (code == MessageConstants.CODE_2027) {
                                    view.getRecordFailure(getString(R.string.data_format_exception), isRefresh);
                                } else {
                                    view.getRecordFailure(getString(R.string.get_data_failure), isRefresh);

                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        LogTool.e(TAG, e.getMessage());
                        view.getRecordFailure(getString(R.string.get_data_failure), isRefresh);
                        disposeDisposable(disposableGetRecord);

                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableGetRecord);

                    }
                });
    }

    @Override
    public void cancelOrder(long memberOrderUid) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableCancelOrder);
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
        memberOrderVO.setMemberOrderUid(memberOrderUid);
        requestJson.setMemberOrderVO(memberOrderVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "cancelOrder:", requestJson);
        txInteractor.cancelOrder(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableCancelOrder=d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        LogTool.d(TAG, responseJson);
                        if (responseJson == null) {
                            view.cancelOrderFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            MemberOrderVO memberOrderVOResponse = responseJson.getMemberOrderVO();
                            if (memberOrderVOResponse != null) {
                                view.cancelOrderSuccess(memberOrderVOResponse);
                            } else {
                                view.cancelOrderFailure(getString(R.string.no_data_info));
                            }

                        } else {
                            view.cancelOrderFailure(getString(R.string.cancel_order_failure_please_try_again));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        LogTool.e(TAG, e.getMessage());
                        view.cancelOrderFailure(getString(R.string.cancel_order_failure_please_try_again));
                        disposeDisposable(disposableCancelOrder);

                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableCancelOrder);

                    }
                });
    }
}
