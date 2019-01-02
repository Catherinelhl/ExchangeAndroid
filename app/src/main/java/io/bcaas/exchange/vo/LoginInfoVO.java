package io.bcaas.exchange.vo;

import java.io.Serializable;


/**
 * 登入資訊
 *
 * @since 2018/12/11
 *
 * @author Costa
 *
 * @version 1.0.0
 *
 */
public class LoginInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 會員帳號 (Email format)
	 */
	private String memberId;
	/**
	 * 登入驗證碼 (Double-sha256)
	 */
	private String accessToken;
	/**
	 * accessToken失效時間
	 */
	private String expireTime;
	/**
	 * 登入時間
	 */
	private String loginTime;
	/**
	 * 登入狀態(0:登出, 1:登入)
	 */
	private Integer status;

	public LoginInfoVO() {
		super();
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
