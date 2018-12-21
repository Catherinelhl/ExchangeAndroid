package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.ecc.Sha256Tool;
import io.bcaas.exchange.ui.constracts.RegisterConstract;
import io.bcaas.exchange.ui.interactor.LoginInteractor;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.security.NoSuchAlgorithmException;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 注册
 */
public class RegisterPresenterImp implements RegisterConstract.Presenter {
    private String TAG = RegisterPresenterImp.class.getSimpleName();
    private RegisterConstract.View view;
    private LoginInteractor loginInteractor;

    public RegisterPresenterImp(RegisterConstract.View view) {
        super();
        this.view = view;
        loginInteractor = new LoginInteractor();
    }


    @Override
    public void register(String memberId, String password, String realIp) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        try {
            memberVO.setPassword(Sha256Tool.doubleSha256ToString(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LogTool.e(TAG, e.getMessage());
        }
        memberVO.setRealIP(realIp);
        requestJson.setMemberVO(memberVO);
        LogTool.d(TAG, requestJson);

        loginInteractor.register(GsonTool.beanToRequestBody(requestJson))
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
                            view.registerFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.registerSuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2006) {
                                view.registerFailure(MessageConstants.CODE_2006_MSG);

                            } else {
                                view.registerFailure(responseJson.getMessage());

                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.registerFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
