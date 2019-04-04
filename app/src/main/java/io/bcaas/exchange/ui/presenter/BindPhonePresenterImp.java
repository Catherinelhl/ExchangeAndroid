package io.bcaas.exchange.ui.presenter;

import io.bcaas.exchange.R;
import io.bcaas.exchange.base.BaseApplication;
import io.bcaas.exchange.bean.CountryCodeBean;
import io.bcaas.exchange.bean.VerificationBean;
import io.bcaas.exchange.constants.MessageConstants;
import io.bcaas.exchange.gson.GsonTool;
import io.bcaas.exchange.tools.ListTool;
import io.bcaas.exchange.tools.LogTool;
import io.bcaas.exchange.tools.StringTool;
import io.bcaas.exchange.tools.file.FilePathTool;
import io.bcaas.exchange.tools.file.ResourceTool;
import io.bcaas.exchange.ui.contracts.BindPhoneContract;
import io.bcaas.exchange.ui.interactor.SafetyCenterInteractor;
import io.bcaas.exchange.vo.LoginInfoVO;
import io.bcaas.exchange.vo.MemberVO;
import io.bcaas.exchange.vo.RequestJson;
import io.bcaas.exchange.vo.ResponseJson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/8
 * 数据交互实现类：绑定手机号
 */
public class BindPhonePresenterImp extends PhoneVerifyPresenterImp implements BindPhoneContract.Presenter {
    private String TAG = BindPhonePresenterImp.class.getSimpleName();
    private BindPhoneContract.View view;
    private SafetyCenterInteractor safetyCenterInteractor;
    private Disposable disposableSecurityPhone;

    public BindPhonePresenterImp(BindPhoneContract.View view) {
        super(view);
        this.view = view;
        safetyCenterInteractor = new SafetyCenterInteractor();
    }

    @Override
    public void securityPhone(String phone, String verifyCode) {
        //判断当前是否有网路
        if (!BaseApplication.isRealNet()) {
            view.noNetWork();
            return;
        }
        disposeDisposable(disposableSecurityPhone);
        //显示加载框
        view.showLoading();
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberID());
        memberVO.setPhone(phone);
        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setVerifyCode(verifyCode);
        LoginInfoVO loginInfoVO = new LoginInfoVO();
        loginInfoVO.setAccessToken(BaseApplication.getToken());
        requestJson.setMemberVO(memberVO);
        requestJson.setLoginInfoVO(loginInfoVO);
        requestJson.setVerificationBean(verificationBean);
        GsonTool.logInfo(TAG, MessageConstants.LogInfo.REQUEST_JSON, "securityPhone:", requestJson);
        safetyCenterInteractor.securityPhone(GsonTool.beanToRequestBody(requestJson))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableSecurityPhone = d;
                    }

                    @Override
                    public void onNext(ResponseJson responseJson) {
                        GsonTool.logInfo(TAG, MessageConstants.LogInfo.RESPONSE_JSON, "securityPhone:", responseJson);
                        if (responseJson == null) {
                            view.noData();
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.securityPhoneSuccess(responseJson.getMessage());
                        } else {
                            if (!view.httpExceptionDisposed(responseJson)) {
                                int code = responseJson.getCode();
                                if (code == MessageConstants.CODE_2086) {
                                    view.securityPhoneFailure(getString(R.string.phone_number_had_bind));
                                } else if (code == MessageConstants.CODE_2047) {
                                    //    {"success":false,"code":2047,"message":"Verify phone code fail."}
                                    view.securityPhoneFailure(getString(R.string.verify_phone_code_fail));
                                } else {
                                    view.securityPhoneFailure(getString(R.string.failure_to_bind_phone));

                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.hideLoading();
                        view.securityPhoneFailure(e.getMessage());
                        disposeDisposable(disposableSecurityPhone);
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoading();
                        disposeDisposable(disposableSecurityPhone);

                    }
                });
    }

    @Override
    public void getCountryCode(String language) {
        String json = ResourceTool.getJsonFromAssets(FilePathTool.getCountryCodeFilePath(language));
        if (StringTool.notEmpty(json)) {
            CountryCodeBean countryCodeBean = GsonTool.convert(json, CountryCodeBean.class);
            if (countryCodeBean != null) {
                List<CountryCodeBean.CountryCode> countryCodesTemp = countryCodeBean.getData();
                if (ListTool.noEmpty(countryCodesTemp)) {
                    List<CountryCodeBean.CountryCode> countryCodes = new ArrayList<>();
                    for (CountryCodeBean.CountryCode countryCode : countryCodesTemp) {
                        String name = countryCode.getCountryName();
                        String code = countryCode.getPhoneCode();
                        if (StringTool.isEmpty(name)
                                || StringTool.isEmpty(code)
                                || StringTool.equals(code, MessageConstants.NULL)
                                || StringTool.equals(name, MessageConstants.NULL)) {
                            continue;
                        }
                        countryCodes.add(countryCode);
                    }
                    view.getCountryCodeSuccess(countryCodes);
                } else {
                    view.getCountryCodeFailure();
                }

            } else {
                view.getCountryCodeFailure();

            }

        }
    }

    @Override
    public void cancelSubscribe() {
        disposeDisposable(disposableSecurityPhone);
    }
}
