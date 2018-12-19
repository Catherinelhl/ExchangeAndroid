package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/19
 * <p>
 * 购买数据类
 */
public class BuyDataBean implements Serializable {
    /*人名*/
    private String personName;
    /*支付方式*/
    private String buyMethod;
    /*总额*/
    private String totalAccount;
    /*数量*/
    private String number;
    /*单价*/
    private String price;
    /*手续费*/
    private String fee;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getBuyMethod() {
        return buyMethod;
    }

    public void setBuyMethod(String buyMethod) {
        this.buyMethod = buyMethod;
    }

    public String getTotalAccount() {
        return totalAccount;
    }

    public void setTotalAccount(String totalAccount) {
        this.totalAccount = totalAccount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "BuyDataBean{" +
                "personName='" + personName + '\'' +
                ", buyMethod='" + buyMethod + '\'' +
                ", totalAccount='" + totalAccount + '\'' +
                ", number='" + number + '\'' +
                ", price='" + price + '\'' +
                ", fee='" + fee + '\'' +
                '}';
    }
}
