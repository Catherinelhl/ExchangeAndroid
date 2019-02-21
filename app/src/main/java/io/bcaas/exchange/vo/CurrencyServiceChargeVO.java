package io.bcaas.exchange.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 幣種服務費
 * 
 * @since 2018/12/11
 * 
 * @author Costa
 * 
 * @version 1.0.0
 * 
 */
public class CurrencyServiceChargeVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 幣種服務費Uid
	 */
	private int currencyServiceChargeUid;
	/**
	 * 幣種清單Uid
	 */
	private CurrencyListVO currencyListVO;
	/**
	 * 充值
	 */
	private BigDecimal recharge;
	/**
	 * 買
	 */
	private BigDecimal buy;
	/**
	 * 賣
	 */
	private BigDecimal sell;
	/**
	 * 更新時間
	 */
	private Timestamp updateTime;
	/**
	 * 建立時間
	 */
	private Timestamp createTime;

	public CurrencyServiceChargeVO() {
		super();
	}

	public int getCurrencyServiceChargeUid() {
		return currencyServiceChargeUid;
	}

	public void setCurrencyServiceChargeUid(int currencyServiceChargeUid) {
		this.currencyServiceChargeUid = currencyServiceChargeUid;
	}

	public CurrencyListVO getCurrencyListVO() {
		return currencyListVO;
	}

	public void setCurrencyListVO(CurrencyListVO currencyListVO) {
		this.currencyListVO = currencyListVO;
	}

	public BigDecimal getRecharge() {
		return recharge;
	}

	public void setRecharge(BigDecimal recharge) {
		this.recharge = recharge;
	}

	public BigDecimal getBuy() {
		return buy;
	}

	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}

	public BigDecimal getSell() {
		return sell;
	}

	public void setSell(BigDecimal sell) {
		this.sell = sell;
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
