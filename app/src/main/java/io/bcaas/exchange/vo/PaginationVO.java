package io.bcaas.exchange.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 分頁 Object
 * 
 * @since 2018/02/01
 * 
 * @author Costa Peng
 * 
 * @version 1.0.0
 * 
 */

public class PaginationVO implements Serializable {

	private static final long serialVersionUID = 2728515357810146958L;

	// Data list
	private List<Object> objectList;
	// Next page objectId
	private String nextObjectId;
	// 總頁數
	private Integer totalPageNumber;
	// 總筆數
	private Long totalObjectNumber;

	public PaginationVO() {
		super();
	}

	public PaginationVO(String nextObjectId) {
		super();
		this.nextObjectId = nextObjectId;
	}

	public PaginationVO(List<Object> objectList, String nextObjectId) {
		super();
		this.objectList = objectList;
		this.nextObjectId = nextObjectId;
	}

	public PaginationVO(List<Object> objectList, String nextObjectId, Integer totalPageNumber, Long totalObjectNumber) {
		super();
		this.objectList = objectList;
		this.nextObjectId = nextObjectId;
		this.totalPageNumber = totalPageNumber;
		this.totalObjectNumber = totalObjectNumber;
	}

	public List<Object> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<Object> objectList) {
		this.objectList = objectList;
	}

	public String getNextObjectId() {
		return nextObjectId;
	}

	public void setNextObjectId(String nextObjectId) {
		this.nextObjectId = nextObjectId;
	}

	public Integer getTotalPageNumber() {
		return totalPageNumber;
	}

	public void setTotalPageNumber(int totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}

	public Long getTotalObjectNumber() {
		return totalObjectNumber;
	}

	public void setTotalPageNumber(Integer totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}

	public void setTotalObjectNumber(Long totalObjectNumber) {
		this.totalObjectNumber = totalObjectNumber;
	}

	@Override
	public String toString() {
		return "PaginationVO{" +
				"objectList=" + objectList +
				", nextObjectId='" + nextObjectId + '\'' +
				", totalPageNumber=" + totalPageNumber +
				", totalObjectNumber=" + totalObjectNumber +
				'}';
	}
}
