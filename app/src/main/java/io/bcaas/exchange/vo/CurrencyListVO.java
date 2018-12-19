package io.bcaas.exchange.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 幣種清單
 * 
 * @since 2018/12/11
 * 
 * @author Costa
 * 
 * @version 1.0.0
 * 
 */
public class CurrencyListVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 幣種清單Uid
	 */
	private int currencyUid;
	/**
	 * 幣種名稱(英文)
	 */
	private String enName;
	/**
	 * 幣種名稱(中文)
	 */
	private String cnName;
	/**
	 * 手續費
	 */
	private BigDecimal serviceCharge;
	/**
	 * 礦工費
	 */
	private BigDecimal gasFeeCharge;
	/**
	 * 手續費利率
	 */
	private BigDecimal rate;
	/**
	 * 更新時間
	 */
	private Timestamp updateTime;
	/**
	 * 建立時間
	 */
	private Timestamp createTime;

	public CurrencyListVO() {
		super();
	}

	public int getCurrencyUid() {
		return currencyUid;
	}

	public void setCurrencyUid(int currencyUid) {
		this.currencyUid = currencyUid;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public BigDecimal getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(BigDecimal serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public BigDecimal getGasFeeCharge() {
		return gasFeeCharge;
	}

	public void setGasFeeCharge(BigDecimal gasFeeCharge) {
		this.gasFeeCharge = gasFeeCharge;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
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
