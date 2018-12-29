package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/29
 * 「售出」界面的传递数据类
 */
public class SellDataBean implements Serializable {

    //当前币种
    private String currency;
    //可售余额
    private String salableBalance;
    //汇率
    private String exchangeRate;
    //默认交换币种
    private String exchangeCurrency;

    public SellDataBean(String salableBalance, String exchangeRate) {
        super();
        this.salableBalance = salableBalance;
        this.exchangeRate = exchangeRate;
    }


    public SellDataBean(String currency, String exchangeCurrency, String salableBalance, String exchangeRate) {
        super();
        this.currency = currency;
        this.salableBalance = salableBalance;
        this.exchangeRate = exchangeRate;
        this.exchangeCurrency = exchangeCurrency;
    }

    public String getExchangeCurrency() {
        return exchangeCurrency;
    }

    public void setExchangeCurrency(String exchangeCurrency) {
        this.exchangeCurrency = exchangeCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSalableBalance() {
        return salableBalance;
    }

    public void setSalableBalance(String salableBalance) {
        this.salableBalance = salableBalance;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public String toString() {
        return "SellDataBean{" +
                "currency='" + currency + '\'' +
                ", salableBalance='" + salableBalance + '\'' +
                ", exchangeRate='" + exchangeRate + '\'' +
                ", exchangeCurrency='" + exchangeCurrency + '\'' +
                '}';
    }
}
