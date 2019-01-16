package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
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
 * 订单记录
 */
public class OrderRecordPresenterImp implements OrderRecordContract.Presenter {
    private String TAG = OrderRecordPresenterImp.class.getSimpleName();
    private OrderRecordContract.View view;
    private TxInteractor txInteractor;

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
        //显示加载框
        view.showLoading();
        //组装数据
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
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
        GsonTool.logInfo(TAG,MessageConstants.LogInfo.REQUEST_JSON,"getRecord",requestJson);

        txInteractor.getRecord(GsonTool.beanToRequestBody(requestJson))
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
                            view.getRecordFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            PaginationVO paginationVOResponse = responseJson.getPaginationVO();
                            if (paginationVOResponse != null) {
                                view.getRecordSuccess(paginationVOResponse);
                            } else {
                                view.getRecordFailure(responseJson.getMessage());
                            }

                        } else {
                            int code = responseJson.getCode();
                            view.getRecordFailure(responseJson.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        LogTool.e(TAG, e.getMessage());
                        view.getRecordFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
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
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);


        MemberOrderVO memberOrderVO = new MemberOrderVO();
        memberOrderVO.setMemberOrderUid(memberOrderUid);
        requestJson.setMemberOrderVO(memberOrderVO);
        GsonTool.logInfo(TAG,MessageConstants.LogInfo.REQUEST_JSON,"cancelOrder:",requestJson);
        txInteractor.cancelOrder(GsonTool.beanToRequestBody(requestJson))
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
                            view.cancelOrderFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            MemberOrderVO memberOrderVOResponse = responseJson.getMemberOrderVO();
                            if (memberOrderVOResponse != null) {
                                view.cancelOrderSuccess(memberOrderVOResponse);
                            } else {
                                view.cancelOrderFailure(responseJson.getMessage());
                            }

                        } else {
                            int code = responseJson.getCode();
                            view.cancelOrderFailure(responseJson.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        LogTool.e(TAG, e.getMessage());
                        view.cancelOrderFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                    }
                });
    }
}
