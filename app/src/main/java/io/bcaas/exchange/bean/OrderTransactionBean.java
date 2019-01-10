package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/21
 * 订单中的交易
 */
@Deprecated
public class OrderTransactionBean implements Serializable {
    private String orderStatus;
    private String orderType;
    private String orderTime;
    private String outCome;
    private String inCome;
    private String fee;

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

    public String getOutCome() {
        return outCome;
    }

    public void setOutCome(String outCome) {
        this.outCome = outCome;
    }

    public String getInCome() {
        return inCome;
    }

    public void setInCome(String inCome) {
        this.inCome = inCome;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "OrderTransactionBean{" +
                "orderStatus='" + orderStatus + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", outCome='" + outCome + '\'' +
                ", inCome='" + inCome + '\'' +
                ", fee='" + fee + '\'' +
                '}';
    }
}
