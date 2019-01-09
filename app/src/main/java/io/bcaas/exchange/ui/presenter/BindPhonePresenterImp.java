package io.bcaas.exchange.ui.presenter;

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
import io.bcaas.exchange.ui.interactor.VerifyCodeInteractor;
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
 */
public class BindPhonePresenterImp implements BindPhoneContract.Presenter {
    private String TAG = BindPhonePresenterImp.class.getSimpleName();
    private BindPhoneContract.View view;
    private VerifyCodeInteractor verifyCodeInteractor;

    public BindPhonePresenterImp(BindPhoneContract.View view) {
        super();
        this.view = view;
        verifyCodeInteractor = new VerifyCodeInteractor();
    }

    @Override
    public void phoneVerify(String phone, String languageCode) {
        RequestJson requestJson = new RequestJson();
        MemberVO memberVO = new MemberVO();
        memberVO.setMemberId(BaseApplication.getMemberId());
        VerificationBean verificationBean = new VerificationBean();
        verificationBean.setPhone(phone);
        verificationBean.setLanguageCode(languageCode);
        requestJson.setMemberVO(memberVO);
        requestJson.setVerificationBean(verificationBean);
        LogTool.d(TAG, requestJson);
        verifyCodeInteractor.phoneVerify(GsonTool.beanToRequestBody(requestJson))
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
                            view.bindPhoneFailure(MessageConstants.EMPTY);
                            return;
                        }
                        boolean isSuccess = responseJson.isSuccess();
                        if (isSuccess) {
                            view.bindPhoneSuccess(responseJson.getMessage());
                        } else {
                            int code = responseJson.getCode();
                            if (code == MessageConstants.CODE_2019) {
                                //    {"success":false,"code":2019,"message":"AccessToken expire."}
                                view.bindPhoneFailure(responseJson.getMessage());
                            } else {
                                view.bindPhoneFailure(MessageConstants.EMPTY);

                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogTool.e(TAG, e.getMessage());
                        view.bindPhoneFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

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
                        if (StringTool.isEmpty(name) || StringTool.isEmpty(code)) {
                            return;
                        }
                        countryCodes.add(countryCode);
                    }
                    view.getCountryCodeSuccess(countryCodes);
                } else {
                    view.getCountryCodeFailure();
                }

            }else{
                view.getCountryCodeFailure();

            }

        }
    }
}
