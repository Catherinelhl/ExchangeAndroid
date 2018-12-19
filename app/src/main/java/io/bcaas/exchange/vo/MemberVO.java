package io.bcaas.exchange.vo;

import java.io.Serializable;
import java.sql.Timestamp;

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
	 * 郵箱驗證 (0:close, 1:open)
	 */
	private int emailVerify;
	/**
	 * 電話驗證 (0:close, 1:open)
	 */
	private int phoneVerify;
	/**
	 * 雙因素驗證(Google) (0:close, 1:open)
	 */
	private int twoFactorAuthVerify;
	/**
	 * 使用者狀態(0:停權, 1:激活)
	 */
	private int isActivation;
	/**
	 * 更新時間
	 */
	private Timestamp updateTime;
	/**
	 * 建立時間
	 */
	private Timestamp createTime;

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

	public int getEmailVerify() {
		return emailVerify;
	}

	public void setEmailVerify(int emailVerify) {
		this.emailVerify = emailVerify;
	}

	public int getPhoneVerify() {
		return phoneVerify;
	}

	public void setPhoneVerify(int phoneVerify) {
		this.phoneVerify = phoneVerify;
	}

	public int getTwoFactorAuthVerify() {
		return twoFactorAuthVerify;
	}

	public void setTwoFactorAuthVerify(int twoFactorAuthVerify) {
		this.twoFactorAuthVerify = twoFactorAuthVerify;
	}

	public int getIsActivation() {
		return isActivation;
	}

	public void setIsActivation(int isActivation) {
		this.isActivation = isActivation;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
