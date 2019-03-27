package io.bcaas.exchange.vo;

import java.io.Serializable;

/**
 * 會員訂單 type : (0:转入, 1:提現, 2:買, 3:賣)
 *
 * @author Costa
 * @version 1.0.0
 * @since 2018/12/11
 */
public class MemberOrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 訂單Uid
     */
    private Long memberOrderUid;
    /**
     * 會員帳號 (Email format)
     */
    private MemberVO memberVO;
    /**
     * 幣種清單Uid
     */
    private CurrencyListVO currencyListVO;
    /**
     * 會員平台地址資訊Uid
     */
    private MemberKeyVO memberKeyVO;
    /**
     * 賣方訂單編號
     */
    private Long sellerOrderUid;
    /**
     * 支付的幣種
     */
    private CurrencyListVO paymentCurrencyUid;
    /**
     * 訂單種類<br>
     * (0:转入, 1:提現, 2:買, 3:賣)
     */
    private Integer type;
    /**
     * 訂單種類對應的狀態<br>
     * 0:转入 ---> (0:失敗, 1:已完成)<br>
     * 1:提現 ---> (0:失敗, 1:已完成, 2:待驗證)<br>
     * 2:買 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
     * 3:賣 ---> (0:已撤銷, 1:已完成, 2:待出售)<br>
     */
    private Integer status;
    /**
     * 是否轉帳給平台集中戶<br>
     * (0:否, 1:是)
     */
    private Integer isTransferPlatform;
    /**
     * 是否轉帳給用户<br>
     * (0:否, 1:是)
     */
    private Integer isTransferMember;
    /**
     * 數量
     */
    private String amount;
    /**
     * 單價
     */
    private String unitPrice;
    /**
     * 手續費
     */
    private String handlingFee;
    /**
     * 總價
     */
    private String price;
    /**
     * 支出
     */
    private String expenditure;
    /**
     * 收入
     */
    private String income;
    /**
     * 交易Hash
     */
    private String txHash;
    /**
     * 備註(提現)
     */
    private String mark;
    /**
     * 出帳, 提币地址
     */
    private String address;
    /**
     * 礦工費
     */
    private String gasFeeCharge;
    /**
     * 更新時間
     */
    private String updateTime;
    /**
     * 建立時間
     */
    private String createTime;
    /**
     * 银行账户名字
     */
    private String bankPersonalName;
    /**
     * 银行名字
     */
    private String bankName;
    /**
     * 银行支行名字
     */
    private String bankBranchName;
    /**
     * 银行账号
     */
    private String bankAccount;

    public MemberOrderVO() {
        super();
    }

    public Long getMemberOrderUid() {
        return memberOrderUid;
    }

    public void setMemberOrderUid(Long memberOrderUid) {
        this.memberOrderUid = memberOrderUid;
    }

    public MemberVO getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(MemberVO memberVO) {
        this.memberVO = memberVO;
    }

    public CurrencyListVO getCurrencyListVO() {
        return currencyListVO;
    }

    public void setCurrencyListVO(CurrencyListVO currencyListVO) {
        this.currencyListVO = currencyListVO;
    }

    public MemberKeyVO getMemberKeyVO() {
        return memberKeyVO;
    }

    public void setMemberKeyVO(MemberKeyVO memberKeyVO) {
        this.memberKeyVO = memberKeyVO;
    }

    public Long getSellerOrderUid() {
        return sellerOrderUid;
    }

    public void setSellerOrderUid(Long sellerOrderUid) {
        this.sellerOrderUid = sellerOrderUid;
    }

    public CurrencyListVO getPaymentCurrencyUid() {
        return paymentCurrencyUid;
    }

    public void setPaymentCurrencyUid(CurrencyListVO paymentCurrencyUid) {
        this.paymentCurrencyUid = paymentCurrencyUid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsTransferPlatform() {
        return isTransferPlatform;
    }

    public void setIsTransferPlatform(Integer isTransferPlatform) {
        this.isTransferPlatform = isTransferPlatform;
    }

    public Integer getIsTransferMember() {
        return isTransferMember;
    }

    public void setIsTransferMember(Integer isTransferMember) {
        this.isTransferMember = isTransferMember;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getHandlingFee() {
        return handlingFee;
    }

    public void setHandlingFee(String handlingFee) {
        this.handlingFee = handlingFee;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGasFeeCharge() {
        return gasFeeCharge;
    }

    public void setGasFeeCharge(String gasFeeCharge) {
        this.gasFeeCharge = gasFeeCharge;
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

    @Override
    public String toString() {
        return "MemberOrderVO{" +
                "memberOrderUid=" + memberOrderUid +
                ", memberVO=" + memberVO +
                ", currencyListVO=" + currencyListVO +
                ", memberKeyVO=" + memberKeyVO +
                ", sellerOrderUid=" + sellerOrderUid +
                ", paymentCurrencyUid=" + paymentCurrencyUid +
                ", type=" + type +
                ", status=" + status +
                ", isTransferPlatform=" + isTransferPlatform +
                ", isTransferMember=" + isTransferMember +
                ", amount='" + amount + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", handlingFee='" + handlingFee + '\'' +
                ", price='" + price + '\'' +
                ", expenditure='" + expenditure + '\'' +
                ", income='" + income + '\'' +
                ", txHash='" + txHash + '\'' +
                ", mark='" + mark + '\'' +
                ", address='" + address + '\'' +
                ", gasFeeCharge='" + gasFeeCharge + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                ", bankPersonalName='" + bankPersonalName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankBranchName='" + bankBranchName + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                '}';
    }
}
