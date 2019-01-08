package io.bcaas.exchange.ui.contracts;

import android.graphics.Bitmap;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 验证码的Contract
 */
public interface VerifyCodeContract {

    interface View {
        //获取图片验证码成功
        void getImageVerifyCodeSuccess(Bitmap bitmap);

        //获取图片验证码失败
        void getImageVerifyCodeFailure(String info);

        //获取email验证码成功
        void getEmailVerifySuccess(String info);

        //获取email验证码失败
        void getEmailVerifyFailure(String info);

        void bindPhoneSuccess(String info);

        void bindPhoneFailure(String info);
    }

    interface Presenter {
        void phoneVerify(String phone, String languageCode);

        //获取邮件验证码
        void emailVerify(String memberId, String languageCode, String mail);

        //获取图片验证码
        void getImageVerifyCode();

    }

}
