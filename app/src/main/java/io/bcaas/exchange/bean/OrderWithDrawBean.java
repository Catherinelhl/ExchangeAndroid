package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 订单中的提现数据类
 */

@Deprecated
public class OrderWithDrawBean implements Serializable {

    private String orderStatus;
    private String orderType;
    private String orderTime;
    private String number;
    private String fee;
    private String address;
    private String currency;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "OrderWithDrawBean{" +
                "orderStatus='" + orderStatus + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", number='" + number + '\'' +
                ", fee='" + fee + '\'' +
                ", address='" + address + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
