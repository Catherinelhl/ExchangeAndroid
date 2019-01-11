package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.WithDrawContract;
import io.bcaas.exchange.ui.interactor.TxInteractor;
import io.bcaas.exchange.vo.*;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/10
 * <p>
 * 提现
 */
public class WithDrawPresenterImp extends AccountSecurityPresenterImp implements WithDrawContract.Presenter {

    private String TAG = WithDrawPresenterImp.class.getSimpleName();
    private WithDrawContract.View view;
    private TxInteractor txInteractor;

    public WithDrawPresenterImp(WithDrawContract.View view) {
        super(view);
        this.view = view;
        txInteractor = new TxInteractor();
    }

    @Override
    public void withDraw(String txPassword, RequestJson requestJson) {
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        memberVO.setTxPassword(txPassword);
        requestJson.setMemberVO(memberVO);

        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        LogTool.d(TAG, "withDraw:" + GsonTool.string(requestJson));
        txInteractor.withDraw(GsonTool.beanToRequestBody(requestJson))
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
                            view.withDrawFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.withDrawSuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
//                            if (code == MessageConstants.CODE_2019) {
                            //    {"success":false,"code":2019,"message":"AccessToken expire."}
                            view.withDrawFailure(responseJson.getMessage());
//                            } else {

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.withDrawFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
