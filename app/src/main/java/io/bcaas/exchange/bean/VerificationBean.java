package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * 2FA 雙因素驗證<br>
 * Email驗證<br>
 * Phone驗證
 * 
 * @since 2018-12-17
 * 
 * @author Costa Peng
 * 
 * @version 1.0.0
 * 
 */
public class VerificationBean implements Serializable {

	private static final long serialVersionUID = 1L;


	/**
	 *	語言
	 */
	private String languageCode;
	/**
	 *	手機號碼
	 */
	private String phone;
	/**
	 *	郵箱
	 */
	private String mail;
	/**
	 *	驗證碼
	 */
	private String verifyCode;
	/**
	 *	雙因素驗證連結
	 */
	private String authenticatorUrl;
	/**
	 *	帳號
	 */
	private String account;
	/**
	 *	密鑰
	 */
	private String secret;
	/**
	 *	驗證類型(參考verifyCodeTool的定義)
	 */
	private String type;
	/**
	 * 關閉驗證(1:關閉)
	 */
	private String closeType;

	/**
	 * 雙因素驗證應用路徑
	 */
	private String authenticatorPath;

	public String getAuthenticatorPath() {
		return authenticatorPath;
	}

	public void setAuthenticatorPath(String authenticatorPath) {
		this.authenticatorPath = authenticatorPath;
	}

	public VerificationBean() {
		super();
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getAuthenticatorUrl() {
		return authenticatorUrl;
	}

	public void setAuthenticatorUrl(String authenticatorUrl) {
		this.authenticatorUrl = authenticatorUrl;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCloseType() {
		return closeType;
	}

	public void setCloseType(String closeType) {
		this.closeType = closeType;
	}

	@Override
	public String toString() {
		return "VerificationBean{" +
				"languageCode='" + languageCode + '\'' +
				", phone='" + phone + '\'' +
				", mail='" + mail + '\'' +
				", verifyCode='" + verifyCode + '\'' +
				", authenticatorUrl='" + authenticatorUrl + '\'' +
				", account='" + account + '\'' +
				", secret='" + secret + '\'' +
				", type='" + type + '\'' +
				", closeType='" + closeType + '\'' +
				", authenticatorPath='" + authenticatorPath + '\'' +
				'}';
	}
}