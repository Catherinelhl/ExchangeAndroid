package io.bcaas.exchange.vo;

import java.io.Serializable;

/**
 * 交易所會員
 *
 * @since 2018/12/11
 *
 * @author Costa
 *
 * @version 1.0.0
 *
 */
public class MemberVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 會員帳號 (Email format)
	 */
	private String memberId;
	/**
	 * 會員密碼 (Double-sha256)
	 */
	private String password;
	/**
	 * 會員交易密碼 (Double-sha256)
	 */
	private String txPassword;
	/**
	 * 使用者真實IP位置
	 */
	private String realIP;
	/**
	 * 使用者電話
	 */
	private String phone;
	/**
	 * 郵箱驗證 (0:CLOSE, 1:open)
	 */
	private Integer emailVerify;
	/**
	 * 電話驗證 (0:CLOSE, 1:open)
	 */
	private Integer phoneVerify;
	/**
	 * 雙因素驗證(Google) (0:CLOSE, 1:open)
	 */
	private Integer twoFactorAuthVerify;
	/**
	 * 使用者狀態(0:停權, 1:激活)
	 */
	private Integer isActivation;
	/**
	 * 更新時間
	 */
	private String updateTime;
	/**
	 * 建立時間
	 */
	private String createTime;
	/**
	 * 新密碼 (Double-sha256)
	 */
	private String newPassword;

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

	public Integer getEmailVerify() {
		return emailVerify;
	}

	public void setEmailVerify(Integer emailVerify) {
		this.emailVerify = emailVerify;
	}

	public Integer getPhoneVerify() {
		return phoneVerify;
	}

	public void setPhoneVerify(Integer phoneVerify) {
		this.phoneVerify = phoneVerify;
	}

	public Integer getTwoFactorAuthVerify() {
		return twoFactorAuthVerify;
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

	public void setEmailVerify(int emailVerify) {
		this.emailVerify = emailVerify;
	}

	public void setPhoneVerify(int phoneVerify) {
		this.phoneVerify = phoneVerify;
	}

	public void setTwoFactorAuthVerify(int twoFactorAuthVerify) {
		this.twoFactorAuthVerify = twoFactorAuthVerify;
	}

	public void setIsActivation(int isActivation) {
		this.isActivation = isActivation;
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

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
