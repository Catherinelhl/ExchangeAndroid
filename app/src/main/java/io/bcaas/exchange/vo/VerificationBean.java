package io.bcaas.exchange.vo;

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

	@Override
	public String toString() {
		return "VerificationBean{" +
				"languageCode='" + languageCode + '\'' +
				", phone='" + phone + '\'' +
				", mail='" + mail + '\'' +
				", verifyCode='" + verifyCode + '\'' +
				", authenticatorUrl='" + authenticatorUrl + '\'' +
				'}';
	}
}