package io.bcaas.exchange.vo;

import io.bcaas.exchange.gson.ServerResponseJson;

import java.util.List;

/**
 * Server 回應使用
 * 
 * @since 2018/06/10
 * 
 * @author Costa Peng
 * 
 * @version 1.0.0
 * 
 */
public class ResponseJson extends ServerResponseJson {

	private static final long serialVersionUID = 1L;

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

	public ResponseJson() {
		super();
	}

	public ResponseJson(boolean success, int code, String message) {
		super(success, code, message);
	}

	public ResponseJson(boolean success, int code, String message, VersionVO versionVO) {
		super(success, code, message);
		this.versionVO = versionVO;
	}

	public ResponseJson(boolean success, int code, String message, VersionVO versionVO, List<VersionVO> versionList) {
		super(success, code, message);
		this.versionVO = versionVO;
		this.versionList = versionList;
	}

	public ResponseJson(boolean success, int code, String message, MemberVO memberVO) {
		super(success, code, message);
		this.memberVO = memberVO;
	}

	public ResponseJson(boolean success, int code, String message, MemberVO memberVO, List<MemberVO> memberVOList) {
		super(success, code, message);
		this.memberVO = memberVO;
		this.memberVOList = memberVOList;
	}

	public ResponseJson(boolean success, int code, String message, LoginInfoVO loginInfoVO) {
		super(success, code, message);
		this.loginInfoVO = loginInfoVO;
	}

	public ResponseJson(boolean success, int code, String message, LoginInfoVO loginInfoVO,
			List<LoginInfoVO> loginInfoVOList) {
		super(success, code, message);
		this.loginInfoVO = loginInfoVO;
		this.loginInfoVOList = loginInfoVOList;
	}

	public ResponseJson(boolean success, int code, String message, CurrencyListVO currencyListVO) {
		super(success, code, message);
		this.currencyListVO = currencyListVO;
	}

	public ResponseJson(boolean success, int code, String message, CurrencyListVO currencyListVO,
			List<CurrencyListVO> currencyListVOList) {
		super(success, code, message);
		this.currencyListVO = currencyListVO;
		this.currencyListVOList = currencyListVOList;
	}

	public ResponseJson(boolean success, int code, String message, CurrencyServiceChargeVO currencyServiceChargeVO) {
		super(success, code, message);
		this.currencyServiceChargeVO = currencyServiceChargeVO;
	}

	public ResponseJson(boolean success, int code, String message, CurrencyServiceChargeVO currencyServiceChargeVO,
			List<CurrencyServiceChargeVO> currencyServiceChargeVOList) {
		super(success, code, message);
		this.currencyServiceChargeVO = currencyServiceChargeVO;
		this.currencyServiceChargeVOList = currencyServiceChargeVOList;
	}

	public ResponseJson(boolean success, int code, String message, MemberKeyVO memberKeyVO) {
		super(success, code, message);
		this.memberKeyVO = memberKeyVO;
	}

	public ResponseJson(boolean success, int code, String message, MemberKeyVO memberKeyVO,
			List<MemberKeyVO> memberKeyVOList) {
		super(success, code, message);
		this.memberKeyVO = memberKeyVO;
		this.memberKeyVOList = memberKeyVOList;
	}

	public ResponseJson(boolean success, int code, String message, MemberOrderVO memberOrderVO) {
		super(success, code, message);
		this.memberOrderVO = memberOrderVO;
	}

	public ResponseJson(boolean success, int code, String message, MemberOrderVO memberOrderVO,
			List<MemberOrderVO> memberOrderVOList) {
		super(success, code, message);
		this.memberOrderVO = memberOrderVO;
		this.memberOrderVOList = memberOrderVOList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

}
