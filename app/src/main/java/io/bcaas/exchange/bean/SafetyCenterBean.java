package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/28
 * 安全中心的列表项
 */
public class SafetyCenterBean implements Serializable {
    //选项卡的名字
    private String tabType;
    // 资金密码
    private String fundPassword;
    //是否设置资金密码
    private boolean noFundPassword;
    //邮箱验证
    private String emailVerify;
    //是否开启邮箱验证
    private boolean noEmailVerify;
    //手机验证
    private String phoneVerify;
    //是否开启手机验证
    private boolean noPhoneVerify;
    //google验证
    private String gmailVerify;
    //是否开启google 验证
    private boolean noGmailVerify;

    public SafetyCenterBean() {
    }


    public String getTabType() {
        return tabType;
    }

    public void setTabType(String tabType) {
        this.tabType = tabType;
    }

    public String getFundPassword() {
        return fundPassword;
    }

    public void setFundPassword(String fundPassword) {
        this.fundPassword = fundPassword;
    }

    public boolean isNoFundPassword() {
        return noFundPassword;
    }

    public void setNoFundPassword(boolean noFundPassword) {
        this.noFundPassword = noFundPassword;
    }

    public String getEmailVerify() {
        return emailVerify;
    }

    public void setEmailVerify(String emailVerify) {
        this.emailVerify = emailVerify;
    }

    public boolean isNoEmailVerify() {
        return noEmailVerify;
    }

    public void setNoEmailVerify(boolean noEmailVerify) {
        this.noEmailVerify = noEmailVerify;
    }

    public String getPhoneVerify() {
        return phoneVerify;
    }

    public void setPhoneVerify(String phoneVerify) {
        this.phoneVerify = phoneVerify;
    }

    public boolean isNoPhoneVerify() {
        return noPhoneVerify;
    }

    public void setNoPhoneVerify(boolean noPhoneVerify) {
        this.noPhoneVerify = noPhoneVerify;
    }

    public String getGmailVerify() {
        return gmailVerify;
    }

    public void setGmailVerify(String gmailVerify) {
        this.gmailVerify = gmailVerify;
    }

    public boolean isNoGmailVerify() {
        return noGmailVerify;
    }

    public void setNoGmailVerify(boolean noGmailVerify) {
        this.noGmailVerify = noGmailVerify;
    }

}
