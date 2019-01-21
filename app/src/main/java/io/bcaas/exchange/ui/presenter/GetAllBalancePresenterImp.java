package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.GetAllBalanceContract;
import io.bcaas.exchange.ui.interactor.MainInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/17
 */
public class GetAllBalancePresenterImp implements GetAllBalanceContract.Presenter {
    private String TAG = GetAllBalancePresenterImp.class.getSimpleName();
    private GetAllBalanceContract.View view;
    private MainInteractor interactor;

    public GetAllBalancePresenterImp(GetAllBalanceContract.View view) {
        super();
        this.view = view;
        this.interactor = new MainInteractor();
    }

    @Override
    public void getAllBalance() {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "getAllBalance", requestJson);

        interactor.getAllBalance(GsonTool.beanToRequestBody(requestJson))
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
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                view.getAllBalanceFailure(responseJson.getMessage());
                            }
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
