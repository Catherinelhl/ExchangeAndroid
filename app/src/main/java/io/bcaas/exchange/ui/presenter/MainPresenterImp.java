package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.ExchangeBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.MainContract;
import io.bcaas.exchange.ui.interactor.MainInteractor;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 注册
 */
public class MainPresenterImp extends AccountSecurityPresenterImp implements MainContract.Presenter {
    private String TAG = MainPresenterImp.class.getSimpleName();
    private MainContract.View view;
    private MainInteractor mainInteractor;
    private SafetyCenterInteractor safetyCenterInteractor;

    public MainPresenterImp(MainContract.View view) {
        super(view);
        this.view = view;
        mainInteractor = new MainInteractor();
        safetyCenterInteractor = new SafetyCenterInteractor();
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
        memberVO.setMemberId(BaseApplication.getMemberId());
        requestJson.setMemberVO(memberVO);

        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        requestJson.setExchangeBean(exchangeBean);
        GsonTool.logInfo(TAG,MessageConstants.LogInfo.REQUEST_JSON,"getCurrencyUSDPrice",requestJson);

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

    @Override
    public void getAllBalance() {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);
        GsonTool.logInfo(TAG,MessageConstants.LogInfo.REQUEST_JSON,"getAllBalance",requestJson);

        mainInteractor.getAllBalance(GsonTool.beanToRequestBody(requestJson))
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
                            view.getAllBalanceFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            List<MemberKeyVO> memberKeyVOList = responseJson.getMemberKeyVOList();
                            if (ListTool.noEmpty(memberKeyVOList)) {
                                BaseApplication.setMemberKeyVOList(memberKeyVOList);
                                view.getAllBalanceSuccess(responseJson.getMemberKeyVOList());
                            } else {
                                view.getAllBalanceFailure(responseJson.getMessage());
                            }

                        } else {
                            int code = responseJson.getCode();
                            view.getAllBalanceFailure(responseJson.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getAllBalanceFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
