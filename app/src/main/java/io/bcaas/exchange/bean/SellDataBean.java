package io.bcaas.exchange.bean;

import io.bcaas.exchange.vo.CurrencyListVO;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/29
 * 「售出」界面的传递数据类
 */
public class SellDataBean extends CurrencyListVO implements Serializable {

    /**
     * 需要交换币种Uid
     */
    private String exchangeCurrencyUid;
    /**
     * 需要交换币种名称
     */
    private String exchangeCurrencyName;
    /**
     * 可提現餘額
     */
    private String balanceAvailable;

    /**
     * 總餘額
     */
    private String balanceTotal;

    /**
     * 出售金额
     */
    private String sellAmount;

    /**
     * 单价
     */
    private String unitPrice;

    /**
     * 扣除手续费的交易额
     * double
     */

    private String txAmountExceptFee;

    /**
     * 扣除手续费的交易额
     * 字符串
     */

    private String txAmountExceptFeeString;

    public SellDataBean(String balanceAvailable) {
        super();
        this.balanceAvailable = balanceAvailable;
    }

    public String getExchangeCurrencyUid() {
        return exchangeCurrencyUid;
    }

    public void setExchangeCurrencyUid(String exchangeCurrencyUid) {
        this.exchangeCurrencyUid = exchangeCurrencyUid;
    }

    public String getBalanceAvailable() {
        return balanceAvailable;
    }

    public void setBalanceAvailable(String balanceAvailable) {
        this.balanceAvailable = balanceAvailable;
    }

    public String getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(String balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    public String getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(String sellAmount) {
        this.sellAmount = sellAmount;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getExchangeCurrencyName() {
        return exchangeCurrencyName;
    }

    public void setExchangeCurrencyName(String exchangeCurrencyName) {
        this.exchangeCurrencyName = exchangeCurrencyName;
    }

    public String getTxAmountExceptFee() {
        return txAmountExceptFee;
    }

    public void setTxAmountExceptFee(String txAmountExceptFee) {
        this.txAmountExceptFee = txAmountExceptFee;
    }

    public String getTxAmountExceptFeeString() {
        return txAmountExceptFeeString;
    }

    public void setTxAmountExceptFeeString(String txAmountExceptFeeString) {
        this.txAmountExceptFeeString = txAmountExceptFeeString;
    }

    @Override
    public String toString() {
        return "SellDataBean{" +
                "exchangeCurrencyUid='" + exchangeCurrencyUid + '\'' +
                ", exchangeCurrencyName='" + exchangeCurrencyName + '\'' +
                ", balanceAvailable='" + balanceAvailable + '\'' +
                ", balanceTotal='" + balanceTotal + '\'' +
                ", sellAmount='" + sellAmount + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", txAmountExceptFee=" + txAmountExceptFee +
                ", txAmountExceptFeeString='" + txAmountExceptFeeString + '\'' +
                '}';
    }
}
