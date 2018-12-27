package io.bcaas.exchange.bean;

import java.io.Serializable;

/**
 * @author catherine.brainwilliam
 * @since 2018/12/27
 * 「我的资产」 数据
 */
public class MyFundBean implements Serializable {
    //资金类型
    private String fundType;
    //可用金额
    private String available;
    //冻结金额
    private String freeze;

    public MyFundBean(String fundType, String available, String freeze) {
        super();
        this.fundType = fundType;
        this.available = available;
        this.freeze = freeze;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    @Override
    public String toString() {
        return "MyFundBean{" +
                "fundType='" + fundType + '\'' +
                ", available='" + available + '\'' +
                ", freeze='" + freeze + '\'' +
                '}';
    }
}
