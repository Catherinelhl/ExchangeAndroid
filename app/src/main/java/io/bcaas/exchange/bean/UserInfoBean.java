package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2019/1/2
 * <p>
 * 「用户信息」，用于存储当前用户在此平台的所有币种信息
 */
public class UserInfoBean implements Serializable {
    private String currency;
    private String tips;
    private String address;

    public UserInfoBean(String currency, String tips, String address) {
        super();
        this.currency = currency;
        this.tips = tips;
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "currency='" + currency + '\'' +
                ", tips='" + tips + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
