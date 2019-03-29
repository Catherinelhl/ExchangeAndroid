package io.bcaas.exchange.vo;

import java.io.Serializable;

/**
 * 平台資訊 - 收付款帳戶
 *
 * @author Costa
 * @version 1.0.0
 * @since 2019/03/14
 */
public class CenterInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 平台資訊Uid
     */
    private Long centerInfoUid;
    /**
     * 收款銀行名字
     */
    private String bankPersonalName;
    /**
     * 銀行名稱
     */
    private String bankName;
    /**
     * 銀行支行名稱
     */
    private String bankBranchName;
    /**
     * 銀行帳戶
     */
    private String bankAccount;
    /**
     * 更新時間
     */
    private String updateTime;
    /**
     * 建立時間
     */
    private String createTime;

    public CenterInfoVO() {
        super();
    }

    public Long getCenterInfoUid() {
        return centerInfoUid;
    }

    public void setCenterInfoUid(Long centerInfoUid) {
        this.centerInfoUid = centerInfoUid;
    }

    public String getBankPersonalName() {
        return bankPersonalName;
    }

    public void setBankPersonalName(String bankPersonalName) {
        this.bankPersonalName = bankPersonalName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
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
        return "CenterInfoVO{" +
                "centerInfoUid=" + centerInfoUid +
                ", bankPersonalName='" + bankPersonalName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankBranchName='" + bankBranchName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
