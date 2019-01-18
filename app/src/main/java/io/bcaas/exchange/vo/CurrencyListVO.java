package io.bcaas.exchange.vo;

import java.io.Serializable;

/**
 * 幣種清單
 *
 * @author Costa
 * @version 1.0.0
 * @since 2018/12/11
 */

public class CurrencyListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 幣種清單Uid
     */
    private String currencyUid;
    /**
     * 幣種名稱(英文)
     */
    private String enName;
    /**
     * 幣種名稱(中文)
     */
    private String cnName;
    /**
     * 币种的coinName,用于交易所币种名字列表显示
     */
    private String coinName;

    /**
     * 提領 手續費
     */
    private String withdrawCharge;
    /**
     * 購買 手續費
     */
    private String buyCharge;
    /**
     * 銷售 手續費
     */
    private String sellCharge;
    /**
     * 礦工費
     */
    private String gasFeeCharge;
    /**
     * 手續費利率
     */
    private String rate;
    /**
     * 更新時間
     */
    private String updateTime;
    /**
     * 建立時間
     */
    private String createTime;

    public CurrencyListVO() {
        super();
    }

    public String getCurrencyUid() {
        return currencyUid;
    }

    public void setCurrencyUid(String currencyUid) {
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

    public String getWithdrawCharge() {
        return withdrawCharge;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public void setWithdrawCharge(String withdrawCharge) {
        this.withdrawCharge = withdrawCharge;
    }

    public String getBuyCharge() {
        return buyCharge;
    }

    public void setBuyCharge(String buyCharge) {
        this.buyCharge = buyCharge;
    }

    public String getSellCharge() {
        return sellCharge;
    }

    public void setSellCharge(String sellCharge) {
        this.sellCharge = sellCharge;
    }

    public String getGasFeeCharge() {
        return gasFeeCharge;
    }

    public void setGasFeeCharge(String gasFeeCharge) {
        this.gasFeeCharge = gasFeeCharge;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "CurrencyListVO{" +
                "currencyUid='" + currencyUid + '\'' +
                ", enName='" + enName + '\'' +
                ", cnName='" + cnName + '\'' +
                ", coinName='" + coinName + '\'' +
                ", withdrawCharge='" + withdrawCharge + '\'' +
                ", buyCharge='" + buyCharge + '\'' +
                ", sellCharge='" + sellCharge + '\'' +
                ", gasFeeCharge='" + gasFeeCharge + '\'' +
                ", rate='" + rate + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
