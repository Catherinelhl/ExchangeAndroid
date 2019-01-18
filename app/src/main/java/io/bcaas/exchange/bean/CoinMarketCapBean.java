package io.bcaas.exchange.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 貨幣市價資訊
 */
public class CoinMarketCapBean implements Serializable {

	private static final long serialVersionUID = 1L;
	// 貨幣名稱
	private String coinName;
	// startTime long 起始時間 ( timeZone = Greenwich )
	private Long startTime;
	// endTime 結束時間 ( timeZone = Greenwich )
	private Long endTime;
	// 該貨幣對應的BTC單價, [時間 long, 價值(BTC)]
	private List<List<Object>> price_btc;
	// 該貨幣對應的USD單價, [時間 long, 價值(USD)]
	private List<List<Object>> price_usd;
	// 交易量, [時間 long, 價值(USD)]
	private List<List<Object>> volume_usd;
	// 市值, [時間 long, 價值(USD)]
	private List<List<Object>> market_cap_by_available_supply;
	// authorizeKey 
	private String authorizeKey;
	
	public CoinMarketCapBean() {
		super();
	}

	public String getCoinName() {
		return coinName;
	}

	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public List<List<Object>> getPrice_btc() {
		return price_btc;
	}

	public void setPrice_btc(List<List<Object>> price_btc) {
		this.price_btc = price_btc;
	}

	public List<List<Object>> getPrice_usd() {
		return price_usd;
	}

	public void setPrice_usd(List<List<Object>> price_usd) {
		this.price_usd = price_usd;
	}

	public List<List<Object>> getVolume_usd() {
		return volume_usd;
	}

	public void setVolume_usd(List<List<Object>> volume_usd) {
		this.volume_usd = volume_usd;
	}

	public List<List<Object>> getMarket_cap_by_available_supply() {
		return market_cap_by_available_supply;
	}

	public void setMarket_cap_by_available_supply(List<List<Object>> market_cap_by_available_supply) {
		this.market_cap_by_available_supply = market_cap_by_available_supply;
	}

	public String getAuthorizeKey() {
		return authorizeKey;
	}

	public void setAuthorizeKey(String authorizeKey) {
		this.authorizeKey = authorizeKey;
	}

	@Override
	public String toString() {
		return "CoinMarketCapBean{" +
				"coinName='" + coinName + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", price_btc=" + price_btc +
				", price_usd=" + price_usd +
				", volume_usd=" + volume_usd +
				", market_cap_by_available_supply=" + market_cap_by_available_supply +
				", authorizeKey='" + authorizeKey + '\'' +
				'}';
	}
}
