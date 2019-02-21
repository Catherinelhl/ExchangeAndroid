package io.bcaas.exchange.vo;

import io.bcaas.exchange.tools.StringTool;

import java.io.Serializable;

/**
 * 會員平台私鑰, 公鑰, 地址資訊
 *
 * @author Costa
 * @version 1.0.0
 * @since 2018/12/11
 */
public class MemberKeyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 會員平台資訊Uid
     */
    private Integer memberKeyUid;
    /**
     * 私鑰
     */
    private String privateKey;
    /**
     * 公鑰
     */
    private String publicKey;
    /**
     * 地址
     */
    private String address;
    /**
     * 總餘額
     */
    private String balanceTotal;
    /**
     * 凍結金額
     */
    private String balanceBlocked;
    /**
     * 可提現餘額
     */
    private String balanceAvailable;
    /**
     * 會員帳號 (Email format)
     */
    private MemberVO memberVO;
    /**
     * 幣種清單Uid
     */
    private CurrencyListVO currencyListVO;
    /**
     * 更新時間
     */
    private String updateTime;
    /**
     * 建立時間
     */
    private String createTime;

    public MemberKeyVO() {
        super();
    }

    public Integer getMemberKeyUid() {
        return memberKeyUid;
    }

    public void setMemberKeyUid(Integer memberKeyUid) {
        this.memberKeyUid = memberKeyUid;
    }

    public void setMemberKeyUid(int memberKeyUid) {
        this.memberKeyUid = memberKeyUid;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalanceTotal() {
        return balanceTotal;
    }

    public void setBalanceTotal(String balanceTotal) {
        this.balanceTotal = balanceTotal;
    }

    public String getBalanceBlocked() {
        return balanceBlocked;
    }

    public void setBalanceBlocked(String balanceBlocked) {
        this.balanceBlocked = balanceBlocked;
    }

    public String getBalanceAvailable() {
        return balanceAvailable;
    }

    public void setBalanceAvailable(String balanceAvailable) {
        this.balanceAvailable = balanceAvailable;
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
        return "MemberKeyVO{" +
                "memberKeyUid=" + memberKeyUid +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", address='" + address + '\'' +
                ", balanceTotal='" + balanceTotal + '\'' +
                ", balanceBlocked='" + balanceBlocked + '\'' +
                ", balanceAvailable='" + balanceAvailable + '\'' +
                ", memberVO=" + memberVO +
                ", currencyListVO=" + currencyListVO +
                ", updateTime='" + updateTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof MemberKeyVO) {
                MemberKeyVO memberKeyVO = (MemberKeyVO) obj;
                    CurrencyListVO currencyListVO = memberKeyVO.getCurrencyListVO();
                    if (currencyListVO != null) {
                        String uid = currencyListVO.getCurrencyUid();
                        if (StringTool.equals(getCurrencyListVO().getCurrencyUid(), uid)) {
                            return true;
                        }
                }
            }
        }
        return false;
    }
}
