package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.CloseVerifyCodeContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
import io.bcaas.exchange.vo.LoginInfoVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/8
 */
public class CloseVerifyPresenterImp
        extends AccountSecurityPresenterImp
        implements CloseVerifyCodeContract.Presenter {

    private String TAG = CloseVerifyPresenterImp.class.getSimpleName();
    private CloseVerifyCodeContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;

    public CloseVerifyPresenterImp(CloseVerifyCodeContract.View view) {
        super(view);
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();
    }

    /**
     * 可一次验证多组验证码,验证成功后会关闭该项
     * 安全验证功能,依照verificationBean中是否有帶
     * closeType做判断依据：
     * <p>
     * type：
     * "0“：邮箱
     * "1“：手机
     * "2“：双因素
     * <p>
     * closeType：
     * "1"：關閉
     *
     * @param verificationBeans
     */
    @Override
    public void closeVerifyCode(List<VerificationBean> verificationBeans) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        requestJson.setMemberVO(memberVO);


        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setLoginInfoVO(loginInfoVO);

        requestJson.setVerificationBeanList(verificationBeans);
        LogTool.d(TAG, GsonTool.string(requestJson));
        safetyCenterInteractor.closeSecurityVerify(GsonTool.beanToRequestBody(requestJson))
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
                            view.closeVerifyCodeFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.closeVerifyCodeSuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2019) {
                                //    {"success":false,"code":2019,"message":"AccessToken expire."}
                                view.closeVerifyCodeFailure(responseJson.getMessage());
                            } else {
                                view.closeVerifyCodeFailure(responseJson.getMessage());

                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogTool.e(TAG, e.getMessage());
                        view.closeVerifyCodeFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
