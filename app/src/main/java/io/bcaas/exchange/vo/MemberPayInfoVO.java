package io.bcaas.exchange.vo;

import java.io.Serializable;

/**
 * 會員付費資訊
 *
 * @author Costa
 * @version 1.0.0
 * @since 2019/03/15
 */
public class MemberPayInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 會員付費資訊Uid
     */
    private Long memberPayInfoUid;
    /**
     * 會員帳號 (Email format)
     */
    private String memberId;
    /**
     * 付費方式<br>
     * (0:銀行卡, 1:)
     */
    private Integer payWayUid;
    /**
     * 收款銀行戶名
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

    public MemberPayInfoVO() {
        super();
    }

    public Long getMemberPayInfoUid() {
        return memberPayInfoUid;
    }

    public void setMemberPayInfoUid(Long memberPayInfoUid) {
        this.memberPayInfoUid = memberPayInfoUid;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Integer getPayWayUid() {
        return payWayUid;
    }

    public void setPayWayUid(Integer payWayUid) {
        this.payWayUid = payWayUid;
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
        return "MemberPayInfoVO{" +
                "memberPayInfoUid=" + memberPayInfoUid +
                ", memberId='" + memberId + '\'' +
                ", payWayUid=" + payWayUid +
                ", bankPersonalName='" + bankPersonalName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankBranchName='" + bankBranchName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
