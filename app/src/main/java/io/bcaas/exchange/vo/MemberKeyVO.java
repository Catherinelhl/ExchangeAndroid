package io.bcaas.exchange.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 會員平台私鑰, 公鑰, 地址資訊
 * 
 * @since 2018/12/11
 * 
 * @author Costa
 * 
 * @version 1.0.0
 * 
 */
public class MemberKeyVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 會員平台資訊Uid
	 */
	private int memberKeyUid;
	/**
	 * 私鑰
	 */
	private String privateKey;
	/**
	 * 公鑰
	 */
	private String publicKey;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 總餘額
	 */
	private BigDecimal balanceTotal;
	/**
	 * 凍結金額
	 */
	private BigDecimal balanceBlocked;
	/**
	 * 可提現餘額
	 */
	private BigDecimal balanceAvailable;
	/**
	 * 會員帳號 (Email format)
	 */
	private MemberVO memberVO;
	/**
	 * 幣種清單Uid
	 */
	private CurrencyListVO currencyListVO;
	/**
	 * 更新時間
	 */
	private Timestamp updateTime;
	/**
	 * 建立時間
	 */
	private Timestamp createTime;

	public MemberKeyVO() {
		super();
	}

	public int getMemberKeyUid() {
		return memberKeyUid;
	}

	public void setMemberKeyUid(int memberKeyUid) {
		this.memberKeyUid = memberKeyUid;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getBalanceTotal() {
		return balanceTotal;
	}

	public void setBalanceTotal(BigDecimal balanceTotal) {
		this.balanceTotal = balanceTotal;
	}

	public BigDecimal getBalanceBlocked() {
		return balanceBlocked;
	}

	public void setBalanceBlocked(BigDecimal balanceBlocked) {
		this.balanceBlocked = balanceBlocked;
	}

	public BigDecimal getBalanceAvailable() {
		return balanceAvailable;
	}

	public void setBalanceAvailable(BigDecimal balanceAvailable) {
		this.balanceAvailable = balanceAvailable;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public CurrencyListVO getCurrencyListVO() {
		return currencyListVO;
	}

	public void setCurrencyListVO(CurrencyListVO currencyListVO) {
		this.currencyListVO = currencyListVO;
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
