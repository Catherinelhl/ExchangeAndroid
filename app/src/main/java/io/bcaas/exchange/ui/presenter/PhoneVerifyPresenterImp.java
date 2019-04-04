package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.base.BasePresenterImp;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.ui.contracts.PhoneVerifyContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/14
 * 数据交互实现类：手机验证
 */
public class PhoneVerifyPresenterImp extends BasePresenterImp implements PhoneVerifyContract.Presenter {

    private String TAG = PhoneVerifyPresenterImp.class.getSimpleName();
    private PhoneVerifyContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;
    private Disposable disposableGetPhoneCode;

    public PhoneVerifyPresenterImp(PhoneVerifyContract.View view) {
        super();
        this.view = view;
        this.safetyCenterInteractor = new SafetyCenterInteractor();

    }

    @Override
    public void getPhoneCode(String phone, String languageCode) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableGetPhoneCode);
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setPhone(phone);
        verificationBean.setLanguageCode(languageCode);
        requestJson.setMemberVO(memberVO);
        requestJson.setVerificationBean(verificationBean);
        LogTool.d(TAG, requestJson);
        safetyCenterInteractor.phoneVerify(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableGetPhoneCode = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        LogTool.d(TAG, responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.getPhoneCodeSuccess(responseJson.getMessage());
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2009
                                        || code == MessageConstants.CODE_400) {
                                    view.getPhoneCodeFailure(getString(R.string.send_phone_message_fail));
                                } else if (code == MessageConstants.CODE_2010) {
                                    view.getPhoneCodeFailure(getString(R.string.verify_code_fail));
                                } else if (code == MessageConstants.CODE_2067) {
                                    view.getPhoneCodeFailure(getString(R.string.verify_code_format_invalid));
                                } else {
                                    view.getPhoneCodeFailure(getString(R.string.failure_get_phone_verify_code));
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.getPhoneCodeFailure(getString(R.string.failure_get_phone_verify_code));
                        disposeDisposable(disposableGetPhoneCode);

                    }

                    @Override
                    public void onComplete() {
                        disposeDisposable(disposableGetPhoneCode);

                    }
                });
    }

    @Override
    public void cancelSubscribe() {
        disposeDisposable(disposableGetPhoneCode);
    }
}
