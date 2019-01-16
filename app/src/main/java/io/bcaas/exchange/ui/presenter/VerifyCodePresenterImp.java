package io.bcaas.exchange.ui.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.VerifyCodeContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * <p>
 * 验证码的获取Presenter
 */
public class VerifyCodePresenterImp implements VerifyCodeContract.Presenter {
    private String TAG = VerifyCodePresenterImp.class.getSimpleName();
    private VerifyCodeContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;

    private Disposable disposableImageVerifyCode;

    public VerifyCodePresenterImp(VerifyCodeContract.View view) {
        super();
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();

    }

    @Override
    public void emailVerify(String memberId, String languageCode, String mail) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(memberId);
        requestJson.setMemberVO(memberVO);
        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setLanguageCode(languageCode);
        verificationBean.setMail(mail);
        requestJson.setVerificationBean(verificationBean);
        LogTool.d(TAG, requestJson);

        safetyCenterInteractor.emailVerify(GsonTool.beanToRequestBody(requestJson))
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
                            view.getEmailVerifyFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.getEmailVerifySuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2028) {
                                //LanguageCode format invalid.
                            }
                            view.getEmailVerifyFailure(responseJson.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getEmailVerifyFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getImageVerifyCode() {
        safetyCenterInteractor.getImageVerifyCode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ResponseBody::byteStream)
                .map(BitmapFactory::decodeStream)
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableImageVerifyCode = d;

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (bitmap == null) {
                            view.getImageVerifyCodeFailure(MessageConstants.EMPTY);

                            return;
                        }
                        view.getImageVerifyCodeSuccess(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
