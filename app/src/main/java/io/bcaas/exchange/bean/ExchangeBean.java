package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * 個人交易Info
 *
 * @author Costa Peng
 * @version 1.0.0
 * @since 2018-12-17
 */
public class ExchangeBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 交易hash
     */
    private String txHash;
    /**
     * 發送的地址 (自己)
     */
    private String sendAddress;
    /**
     * 接收的地址 (對方)
     */
    private String receiveAddress;
    /**
     * 餘額
     */
    private String balance;
    /**
     * 交易狀態
     */
    private String txStatus;
    /**
     * 幣種
     */
    private String currency;
    /**
     * 虛擬貨幣數量
     */
    private String coinNumber;
    /**
     * 美金市值
     */
    private String priceUSD;

    /**
     * 貨幣數額
     *
     * @return
     */

    private String priceCurrency;

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCoinNumber() {
        return coinNumber;
    }

    public void setCoinNumber(String coinNumber) {
        this.coinNumber = coinNumber;
    }

    public String getPriceUSD() {
        return priceUSD;
    }

    public void setPriceUSD(String priceUSD) {
        this.priceUSD = priceUSD;
    }

    @Override
    public String toString() {
        return "ExchangeBean{" +
                "txHash='" + txHash + '\'' +
                ", sendAddress='" + sendAddress + '\'' +
                ", receiveAddress='" + receiveAddress + '\'' +
                ", balance='" + balance + '\'' +
                ", txStatus='" + txStatus + '\'' +
                ", currency='" + currency + '\'' +
                ", coinNumber='" + coinNumber + '\'' +
                ", priceUSD='" + priceUSD + '\'' +
                ", priceCurrency='" + priceCurrency + '\'' +
                '}';
    }
}