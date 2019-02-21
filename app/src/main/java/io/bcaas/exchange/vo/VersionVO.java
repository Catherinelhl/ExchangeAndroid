package io.bcaas.exchange.vo;

import java.io.Serializable;

/**
 * 
 * 平台版本資訊
 *
 * @since 2019/01/04
 * 
 * @author Costa Peng
 * 
 * @version 1.0.0
 *
 * 
 */

public class VersionVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Version uid, 0:iOS, 1:Android
	 */
	private String versionUid;
	/**
	 * Authorize key
	 */
	private String authKey;
	/**
	 * 平台版本
	 */
	private String version;
	/**
	 * 是否強制更新, 0:不強制更新, 1:強制更新
	 */
	private Integer forceUpgrade;
	/**
	 * 平台更新網址, 應用安裝檔下載連接
	 */
	private String updateUrl;
	/**
	 * 平台更新網址, 應用內部資源下載連接, 更新軟件用
	 */
	private String updateSourceUrl;
	/**
	 * 應用商店下載連接
	 */
	private String appStoreUrl;
	/**
	 * 平台種類
	 */
	private String type;
	/**
	 * 更新時間
	 */
	private String updateTime;
	/**
	 * 建立時間
	 */
	private String createTime;

	public VersionVO() {
		super();
	}

	public String getVersionUid() {
		return versionUid;
	}

	public void setVersionUid(String versionUid) {
		this.versionUid = versionUid;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getForceUpgrade() {
		return forceUpgrade;
	}

	public void setForceUpgrade(Integer forceUpgrade) {
		this.forceUpgrade = forceUpgrade;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public String getUpdateSourceUrl() {
		return updateSourceUrl;
	}

	public void setUpdateSourceUrl(String updateSourceUrl) {
		this.updateSourceUrl = updateSourceUrl;
	}

	public String getAppStoreUrl() {
		return appStoreUrl;
	}

	public void setAppStoreUrl(String appStoreUrl) {
		this.appStoreUrl = appStoreUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

}