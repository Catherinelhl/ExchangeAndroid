package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.Constants;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.LogoutContract;
import io.bcaas.exchange.ui.interactor.LoginInteractor;
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
 * @since 2018/12/21
 * 登出
 */
public class LogoutPresenterImp implements LogoutContract.Presenter {
    private String TAG = LogoutPresenterImp.class.getSimpleName();
    private LogoutContract.View view;
    private LoginInteractor loginInteractor;

    public LogoutPresenterImp(LogoutContract.View view) {
        super();
        this.view = view;
        loginInteractor = new LoginInteractor();
    }

    @Override
    public void logout(String memberId) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setMemberVO(memberVO);
        requestJson.setLoginInfoVO(loginInfoVO);
        LogTool.d(TAG, requestJson);
        loginInteractor.logout(GsonTool.beanToRequestBody(requestJson))
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
                            view.logoutFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.logoutSuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2019) {
                                //    {"success":false,"code":2019,"message":"AccessToken expire."}
                                view.logoutSuccess(responseJson.getMessage());
                            } else {
                                view.logoutFailure(MessageConstants.EMPTY);

                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.logoutFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
