package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.AccountSecurityContract;
import io.bcaas.exchange.ui.interactor.AccountSecurityInteractor;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
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
 * @since 2019/1/8
 * 获取账户信息
 */
public class AccountSecurityPresenterImp implements AccountSecurityContract.Presenter {

    private String TAG = AccountSecurityPresenterImp.class.getSimpleName();
    private AccountSecurityContract.View view;
    private AccountSecurityInteractor accountSecurityInteractor;

    public AccountSecurityPresenterImp(AccountSecurityContract.View view) {
        super();
        this.view = view;
        accountSecurityInteractor = new AccountSecurityInteractor();
    }

    @Override
    public void getAccountSecurity() {
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setMemberVO(memberVO);
        requestJson.setLoginInfoVO(loginInfoVO);
        LogTool.d(TAG, "getAccountSecurity:" + requestJson);
        accountSecurityInteractor.getAccountSecurity(GsonTool.beanToRequestBody(requestJson))
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
                            view.getAccountSecurityFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            MemberVO memberVOResponse = responseJson.getMemberVO();
                            if (memberVOResponse != null) {
                                //将这些参数存储起来
                                BaseApplication.setMemberVO(memberVOResponse);
                                view.getAccountSecuritySuccess(memberVOResponse);
                            } else {
                                view.getAccountSecurityFailure(MessageConstants.EMPTY);

                            }
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2019) {
                                //    {"success":false,"code":2019,"message":"AccessToken expire."}
                                view.getAccountSecurityFailure(responseJson.getMessage());
                            } else {
                                view.getAccountSecurityFailure(MessageConstants.EMPTY);

                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoading();
                        LogTool.e(TAG, e.getMessage());
                        view.getAccountSecurityFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();

                    }
                });
    }

}
