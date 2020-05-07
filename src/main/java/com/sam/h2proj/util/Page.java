package com.sam.h2proj.util;

import java.util.List;

/**
 * 承載分頁資訊的物件 <br>
 * 包含以下參數 : <br>
 * 1.每頁筆數 pageSize <br>
 * 2.目前頁碼 currentPage <br>
 * 3.總頁數   totalPages <br>
 * 4.資料總筆數 total <br>
 * 5.Entity清單   records <br>
 * 6.是否為第一頁 isFirst <br>
 * 7.是否為最後一頁 isLast <br>
 * 8.是否還有下一頁 hasNext
 * 
 * @author SamKao
 *
 * @param <T> T為該DAO回傳的Entity類別
 */
public class Page<T> {

	/** 每頁筆數 **/
	private Integer pageSize;
	
	/** 目前頁數 **/
	private Integer currentPage;
	
	/** 總頁數 **/
	private Integer totalPages;
	
	/** 資料總筆數 **/
	private Integer total;
	
	/** 資料清單 **/
	private List<T> records;
	
	public Page(List<T> records,Integer pageNum,Integer pageSize,long totalCount) {
		this.records = records;
		
		this.pageSize = pageSize;
		this.currentPage = pageNum;
		this.totalPages = (int)(Math.ceil((double) totalCount) / pageSize);
		this.total = (int) totalCount;
	}
	
	public void setPageInfo(PageInfo pageInfo) {
		this.pageSize = pageInfo.getPageSize();
		this.currentPage = pageInfo.getPageNum();
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}
	
	public Integer getTotal() {
		return this.total;
	}
	
	public boolean getIsFirstPage() {
		return this.currentPage == 1;
	}
	
	public boolean getIsLastPage() {
		return this.currentPage == this.totalPages;
	}
	
	public boolean isHasNext() {
		return this.currentPage != this.totalPages;
	}
	
}
