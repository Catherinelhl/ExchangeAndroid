package io.bcaas.exchange.vo;

import java.io.Serializable;

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
	private Long memberOrderUid;
	/**
	 * 會員帳號 (Email format)
	 */
	private MemberVO memberVO;
	/**
	 * 訂單種類<br>
	 * (0:充值, 1:提現, 2:買, 3:賣)
	 */
	private Integer type;
	/**
	 * 訂單種類對應的狀態<br>
	 * 0:充值 ---> (0:失敗, 1:已完成)<br>
	 * 1:提現 ---> (0:失敗, 1:已完成, 2:待驗證)<br>
	 * 2:買 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
	 * 3:賣 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
	 */
	private Integer status;
	/**
	 * 幣種清單Uid
	 */
	private CurrencyListVO currencyListVO;
	/**
	 * 數量
	 */
	private String amount;
	/**
	 * 單價
	 */
	private String unitPrice;
	/**
	 * 手續費
	 */
	private String handlingFee;
	/**
	 * 總價
	 */
	private String price;
	/**
	 * 支付的幣種
	 */
	private CurrencyListVO paymentCurrencyUid;
	/**
	 * 備註(提現)
	 */
	private String mark;
	/**
	 * 更新時間
	 */
	private String updateTime;
	/**
	 * 建立時間
	 */
	private String createTime;

	/**
	 * 收入
	 */
	private String income;

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public MemberOrderVO() {
		super();
	}


	public Long getMemberOrderUid() {
		return memberOrderUid;
	}


	public void setMemberOrderUid(Long memberOrderUid) {
		this.memberOrderUid = memberOrderUid;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


	public MemberVO getMemberVO() {
		return memberVO;
	}


	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public CurrencyListVO getCurrencyListVO() {
		return currencyListVO;
	}


	public void setCurrencyListVO(CurrencyListVO currencyListVO) {
		this.currencyListVO = currencyListVO;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getUnitPrice() {
		return unitPrice;
	}


	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}


	public String getHandlingFee() {
		return handlingFee;
	}


	public void setHandlingFee(String handlingFee) {
		this.handlingFee = handlingFee;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public CurrencyListVO getPaymentCurrencyUid() {
		return paymentCurrencyUid;
	}


	public void setPaymentCurrencyUid(CurrencyListVO paymentCurrencyUid) {
		this.paymentCurrencyUid = paymentCurrencyUid;
	}


	public String getMark() {
		return mark;
	}


	public void setMark(String mark) {
		this.mark = mark;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MemberOrderVO{" +
				"memberOrderUid=" + memberOrderUid +
				", memberVO=" + memberVO +
				", type=" + type +
				", status=" + status +
				", currencyListVO=" + currencyListVO +
				", amount='" + amount + '\'' +
				", unitPrice='" + unitPrice + '\'' +
				", handlingFee='" + handlingFee + '\'' +
				", price='" + price + '\'' +
				", paymentCurrencyUid=" + paymentCurrencyUid +
				", mark='" + mark + '\'' +
				", updateTime='" + updateTime + '\'' +
				", createTime='" + createTime + '\'' +
				", income='" + income + '\'' +
				'}';
	}
}
