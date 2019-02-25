package io.bcaas.exchange.bean;

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
public class MemberBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 新密碼 (Double-SHA256)
	 */
	private String newPassword;

	public MemberBean() {
		super();
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
