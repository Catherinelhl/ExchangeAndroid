package io.bcaas.exchange.vo;

import io.bcaas.exchange.bean.CoinMarketCapBean;
import io.bcaas.exchange.bean.ExchangeBean;
import io.bcaas.exchange.bean.MemberBean;
import io.bcaas.exchange.bean.VerificationBean;

import java.io.Serializable;
import java.util.List;

/**
 * Client 請求使用
 *
 * @author Costa Peng
 * @version 1.0.0
 * @since 2018/06/10
 */
public class RequestJson implements Serializable {

    private VersionVO versionVO;
    private List<VersionVO> versionList;

    private MemberVO memberVO;
    private List<MemberVO> memberVOList;

    private LoginInfoVO loginInfoVO;
    private List<LoginInfoVO> loginInfoVOList;

    private CurrencyListVO currencyListVO;
    private List<CurrencyListVO> currencyListVOList;

    private CurrencyServiceChargeVO currencyServiceChargeVO;
    private List<CurrencyServiceChargeVO> currencyServiceChargeVOList;

    private MemberKeyVO memberKeyVO;
    private List<MemberKeyVO> memberKeyVOList;

    private MemberOrderVO memberOrderVO;
    private List<MemberOrderVO> memberOrderVOList;

    private ExchangeBean exchangeBean;
    private VerificationBean verificationBean;
    private List<VerificationBean> verificationBeanList;


    private PaginationVO paginationVO;

    private CoinMarketCapBean coinMarketCapBean;

    private MemberBean memberBean;

    private MemberPayInfoVO memberPayInfoVO;

    public MemberPayInfoVO getMemberPayInfoVO() {
        return memberPayInfoVO;
    }

    public void setMemberPayInfoVO(MemberPayInfoVO memberPayInfoVO) {
        this.memberPayInfoVO = memberPayInfoVO;
    }

    public CoinMarketCapBean getCoinMarketCapBean() {
        return coinMarketCapBean;
    }

    public void setCoinMarketCapBean(CoinMarketCapBean coinMarketCapBean) {
        this.coinMarketCapBean = coinMarketCapBean;
    }

    public PaginationVO getPaginationVO() {
        return paginationVO;
    }

    public void setPaginationVO(PaginationVO paginationVO) {
        this.paginationVO = paginationVO;
    }

    public VerificationBean getVerificationBean() {
        return verificationBean;
    }

    public void setVerificationBean(VerificationBean verificationBean) {
        this.verificationBean = verificationBean;
    }

    public RequestJson() {
        super();
    }

    public RequestJson(VersionVO versionVO) {
        super();
        this.versionVO = versionVO;
    }

    public RequestJson(VersionVO versionVO, List<VersionVO> versionList) {
        super();
        this.versionVO = versionVO;
        this.versionList = versionList;
    }

    public RequestJson(MemberVO memberVO) {
        super();
        this.memberVO = memberVO;
    }

    public RequestJson(MemberVO memberVO, List<MemberVO> memberVOList) {
        super();
        this.memberVO = memberVO;
        this.memberVOList = memberVOList;
    }

    public RequestJson(LoginInfoVO loginInfoVO) {
        super();
        this.loginInfoVO = loginInfoVO;
    }

    public RequestJson(LoginInfoVO loginInfoVO, List<LoginInfoVO> loginInfoVOList) {
        super();
        this.loginInfoVO = loginInfoVO;
        this.loginInfoVOList = loginInfoVOList;
    }

    public RequestJson(CurrencyListVO currencyListVO) {
        super();
        this.currencyListVO = currencyListVO;
    }

    public RequestJson(CurrencyListVO currencyListVO, List<CurrencyListVO> currencyListVOList) {
        super();
        this.currencyListVO = currencyListVO;
        this.currencyListVOList = currencyListVOList;
    }

    public RequestJson(CurrencyServiceChargeVO currencyServiceChargeVO) {
        super();
        this.currencyServiceChargeVO = currencyServiceChargeVO;
    }

    public RequestJson(CurrencyServiceChargeVO currencyServiceChargeVO,
                       List<CurrencyServiceChargeVO> currencyServiceChargeVOList) {
        super();
        this.currencyServiceChargeVO = currencyServiceChargeVO;
        this.currencyServiceChargeVOList = currencyServiceChargeVOList;
    }

    public RequestJson(MemberKeyVO memberKeyVO) {
        super();
        this.memberKeyVO = memberKeyVO;
    }

    public RequestJson(MemberKeyVO memberKeyVO, List<MemberKeyVO> memberKeyVOList) {
        super();
        this.memberKeyVO = memberKeyVO;
        this.memberKeyVOList = memberKeyVOList;
    }

    public RequestJson(MemberOrderVO memberOrderVO) {
        super();
        this.memberOrderVO = memberOrderVO;
    }

    public RequestJson(MemberOrderVO memberOrderVO, List<MemberOrderVO> memberOrderVOList) {
        super();
        this.memberOrderVO = memberOrderVO;
        this.memberOrderVOList = memberOrderVOList;
    }

    public VersionVO getVersionVO() {
        return versionVO;
    }

    public void setVersionVO(VersionVO versionVO) {
        this.versionVO = versionVO;
    }

    public List<VersionVO> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<VersionVO> versionList) {
        this.versionList = versionList;
    }

    public MemberVO getMemberVO() {
        return memberVO;
    }

    public void setMemberVO(MemberVO memberVO) {
        this.memberVO = memberVO;
    }

    public MemberBean getMemberBean() {
        return memberBean;
    }

    public void setMemberBean(MemberBean memberBean) {
        this.memberBean = memberBean;
    }

    public List<MemberVO> getMemberVOList() {
        return memberVOList;
    }

    public void setMemberVOList(List<MemberVO> memberVOList) {
        this.memberVOList = memberVOList;
    }

    public LoginInfoVO getLoginInfoVO() {
        return loginInfoVO;
    }

    public void setLoginInfoVO(LoginInfoVO loginInfoVO) {
        this.loginInfoVO = loginInfoVO;
    }

    public List<LoginInfoVO> getLoginInfoVOList() {
        return loginInfoVOList;
    }

    public void setLoginInfoVOList(List<LoginInfoVO> loginInfoVOList) {
        this.loginInfoVOList = loginInfoVOList;
    }

    public CurrencyListVO getCurrencyListVO() {
        return currencyListVO;
    }

    public void setCurrencyListVO(CurrencyListVO currencyListVO) {
        this.currencyListVO = currencyListVO;
    }

    public List<CurrencyListVO> getCurrencyListVOList() {
        return currencyListVOList;
    }

    public void setCurrencyListVOList(List<CurrencyListVO> currencyListVOList) {
        this.currencyListVOList = currencyListVOList;
    }

    public CurrencyServiceChargeVO getCurrencyServiceChargeVO() {
        return currencyServiceChargeVO;
    }

    public void setCurrencyServiceChargeVO(CurrencyServiceChargeVO currencyServiceChargeVO) {
        this.currencyServiceChargeVO = currencyServiceChargeVO;
    }

    public List<CurrencyServiceChargeVO> getCurrencyServiceChargeVOList() {
        return currencyServiceChargeVOList;
    }

    public void setCurrencyServiceChargeVOList(List<CurrencyServiceChargeVO> currencyServiceChargeVOList) {
        this.currencyServiceChargeVOList = currencyServiceChargeVOList;
    }

    public MemberKeyVO getMemberKeyVO() {
        return memberKeyVO;
    }

    public void setMemberKeyVO(MemberKeyVO memberKeyVO) {
        this.memberKeyVO = memberKeyVO;
    }

    public List<MemberKeyVO> getMemberKeyVOList() {
        return memberKeyVOList;
    }

    public void setMemberKeyVOList(List<MemberKeyVO> memberKeyVOList) {
        this.memberKeyVOList = memberKeyVOList;
    }

    public MemberOrderVO getMemberOrderVO() {
        return memberOrderVO;
    }

    public void setMemberOrderVO(MemberOrderVO memberOrderVO) {
        this.memberOrderVO = memberOrderVO;
    }

    public List<MemberOrderVO> getMemberOrderVOList() {
        return memberOrderVOList;
    }

    public void setMemberOrderVOList(List<MemberOrderVO> memberOrderVOList) {
        this.memberOrderVOList = memberOrderVOList;
    }

    public List<VerificationBean> getVerificationBeanList() {
        return verificationBeanList;
    }

    public void setVerificationBeanList(List<VerificationBean> verificationBeanList) {
        this.verificationBeanList = verificationBeanList;
    }

    public ExchangeBean getExchangeBean() {
        return exchangeBean;
    }

    public void setExchangeBean(ExchangeBean exchangeBean) {
        this.exchangeBean = exchangeBean;
    }

    @Override
    public String toString() {
        return "RequestJson{" +
                "versionVO=" + versionVO +
                ", versionList=" + versionList +
                ", memberVO=" + memberVO +
                ", memberVOList=" + memberVOList +
                ", loginInfoVO=" + loginInfoVO +
                ", loginInfoVOList=" + loginInfoVOList +
                ", currencyListVO=" + currencyListVO +
                ", currencyListVOList=" + currencyListVOList +
                ", currencyServiceChargeVO=" + currencyServiceChargeVO +
                ", currencyServiceChargeVOList=" + currencyServiceChargeVOList +
                ", memberKeyVO=" + memberKeyVO +
                ", memberKeyVOList=" + memberKeyVOList +
                ", memberOrderVO=" + memberOrderVO +
                ", memberOrderVOList=" + memberOrderVOList +
                ", exchangeBean=" + exchangeBean +
                ", verificationBean=" + verificationBean +
                ", verificationBeanList=" + verificationBeanList +
                ", paginationVO=" + paginationVO +
                ", coinMarketCapBean=" + coinMarketCapBean +
                ", memberBean=" + memberBean +
                ", memberPayInfoVO=" + memberPayInfoVO +
                '}';
    }
}
