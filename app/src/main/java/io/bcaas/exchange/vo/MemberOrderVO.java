package io.bcaas.exchange.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 會員訂單 type : (0:充值, 1:提現, 2:買, 3:賣)
 * 
 * @since 2018/12/11
 * 
 * @author Costa
 * 
 * @version 1.0.0
 * 
 */
public class MemberOrderVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 訂單Uid
	 */
	private int memberOrderUid;
	/**
	 * 會員帳號 (Email format)
	 */
	private MemberVO memberVO;
	/**
	 * 幣種清單Uid
	 */
	private CurrencyListVO currencyListVO;
	/**
	 * 訂單種類<br>
	 * (0:充值, 1:提現, 2:買, 3:賣)
	 */
	private int type;
	/**
	 * 訂單種類對應的狀態<br>
	 * 0:充值 ---> (0:失敗, 1:已完成)<br>
	 * 1:提現 ---> (0:失敗, 1:已完成, 2:待驗證)<br>
	 * 2:買 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
	 * 3:賣 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
	 */
	private int status;
	/**
	 * 數量
	 */
	private BigDecimal amount;
	/**
	 * 支出
	 */
	private BigDecimal expenditure;
	/**
	 * 收入
	 */
	private BigDecimal income;
	/**
	 * 備註(提現)
	 */
	private String mark;
	/**
	 * 更新時間
	 */
	private Timestamp updateTime;
	/**
	 * 建立時間
	 */
	private Timestamp createTime;

	public MemberOrderVO() {
		super();
	}

	public int getMemberOrderUid() {
		return memberOrderUid;
	}

	public void setMemberOrderUid(int memberOrderUid) {
		this.memberOrderUid = memberOrderUid;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(BigDecimal expenditure) {
		this.expenditure = expenditure;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
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
