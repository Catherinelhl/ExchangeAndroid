package io.bcaas.exchange.vo;

import java.io.Serializable;

/**
 * 交易所會員
 *
 * @author Costa
 * @version 1.0.0
 * @since 2018/12/11
 */
public class MemberVO implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 會員帳號 (Email format)
     */
    private String memberId;
    /**
     * 會員密碼 (Double-SHA256)
     */
    private String password;
    /**
     * 會員資金密碼 (Double-SHA256)
     */
    private String txPassword;
    /**
     * 會員真實IP位置
     */
    private String realIP;
    /**
     * 會員電話
     */
    private String phone;
    /**
     * 會員身份字號
     */
    private String identityNumber;
    /**
     * 會員實名
     */
    private String identityName;
    /**
     * 雙因素驗證密鑰(Google)
     */
    private String twoFactorAuthSecret;
    /**
     * 郵箱驗證 (0:close, 1:open, 2:not yet bind)
     */
    private Integer emailVerify;
    /**
     * 電話驗證 (0:close, 1:open, 2:not yet bind)
     */
    private Integer phoneVerify;
    /**
     * 雙因素驗證(Google) (0:close, 1:open, 2:not yet bind)
     */
    private Integer twoFactorAuthVerify;
    /**
     * 會員狀態 (0:停權, 1:激活)
     */
    private Integer isActivation;
    /**
     * 自動匹配交易狀態 (0:手動, 1:自動)
     */
    private Integer isAutoMatchTx;
    /**
     * 支付方式綁定 (0:未綁定, 1:已綁定)
     */
    private Integer isPayWayBind;
    /**
     * 實名認證 (0:未認證, 1:已認證)
     */
    private Integer isIdentityVerify;
    /**
     * 更新時間
     */
    private String updateTime;
    /**
     * 建立時間
     */
    private String createTime;

    public MemberVO() {
        super();
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTxPassword() {
        return txPassword;
    }

    public void setTxPassword(String txPassword) {
        this.txPassword = txPassword;
    }

    public String getRealIP() {
        return realIP;
    }

    public void setRealIP(String realIP) {
        this.realIP = realIP;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String getTwoFactorAuthSecret() {
        return twoFactorAuthSecret;
    }

    public void setTwoFactorAuthSecret(String twoFactorAuthSecret) {
        this.twoFactorAuthSecret = twoFactorAuthSecret;
    }

    public Integer getEmailVerify() {
        return emailVerify;
    }

    public void setEmailVerify(Integer emailVerify) {
        this.emailVerify = emailVerify;
    }

    public void setEmailVerify(int emailVerify) {
        this.emailVerify = emailVerify;
    }

    public Integer getPhoneVerify() {
        return phoneVerify;
    }

    public void setPhoneVerify(Integer phoneVerify) {
        this.phoneVerify = phoneVerify;
    }

    public void setPhoneVerify(int phoneVerify) {
        this.phoneVerify = phoneVerify;
    }

    public Integer getTwoFactorAuthVerify() {
        return twoFactorAuthVerify;
    }

    public void setTwoFactorAuthVerify(int twoFactorAuthVerify) {
        this.twoFactorAuthVerify = twoFactorAuthVerify;
    }

    public void setTwoFactorAuthVerify(Integer twoFactorAuthVerify) {
        this.twoFactorAuthVerify = twoFactorAuthVerify;
    }

    public Integer getIsActivation() {
        return isActivation;
    }

    public void setIsActivation(Integer isActivation) {
        this.isActivation = isActivation;
    }

    public void setIsActivation(int isActivation) {
        this.isActivation = isActivation;
    }

    public Integer getIsAutoMatchTx() {
        return isAutoMatchTx;
    }

    public void setIsAutoMatchTx(Integer isAutoMatchTx) {
        this.isAutoMatchTx = isAutoMatchTx;
    }

    public Integer getIsPayWayBind() {
        return isPayWayBind;
    }

    public void setIsPayWayBind(Integer isPayWayBind) {
        this.isPayWayBind = isPayWayBind;
    }

    public Integer getIsIdentityVerify() {
        return isIdentityVerify;
    }

    public void setIsIdentityVerify(Integer isIdentityVerify) {
        this.isIdentityVerify = isIdentityVerify;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "MemberVO{" +
                "memberId='" + memberId + '\'' +
                ", password='" + password + '\'' +
                ", txPassword='" + txPassword + '\'' +
                ", realIP='" + realIP + '\'' +
                ", phone='" + phone + '\'' +
                ", identityNumber='" + identityNumber + '\'' +
                ", identityName='" + identityName + '\'' +
                ", twoFactorAuthSecret='" + twoFactorAuthSecret + '\'' +
                ", emailVerify=" + emailVerify +
                ", phoneVerify=" + phoneVerify +
                ", twoFactorAuthVerify=" + twoFactorAuthVerify +
                ", isActivation=" + isActivation +
                ", isAutoMatchTx=" + isAutoMatchTx +
                ", isPayWayBind=" + isPayWayBind +
                ", isIdentityVerify=" + isIdentityVerify +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
