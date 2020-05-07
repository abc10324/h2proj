package com.sam.h2proj.util;

import java.util.Map;

/**
 * 承載分頁設定的物件 <br>
 * 包含以下參數: <br>
 * 1.每頁筆數 pageSize <br>
 * 2.目前頁碼 pageNum
 * 
 * @author SamKao
 *
 */
public class PageInfo {
	
	/** 每頁筆數 **/
	private Integer pageSize;
	
	/** 目前頁碼 **/
	private Integer pageNum;

	public PageInfo(Integer pageSize, Integer pageNum) {
		// 空值檢查, 初步避免NullPointerException
		// 若為空值則採用Const中定義的系統預設值
		this.pageSize = (pageSize != null) ? pageSize : Const.DEFAULT_PAGE_SIZE;
		this.pageNum = (pageNum != null) ? pageNum : Const.FIRST_PAGE_NUM;
	}
	
	public PageInfo(Map<String,Object> paramMap) {
		String pageSize = String.valueOf(paramMap.get(Const.PAGE_SIZE));
		String pageNum = String.valueOf(paramMap.get(Const.PAGE_NUM));
	
		// 空值檢查, 初步避免NullPointerException
		// 若為空值則採用Const中定義的系統預設值
		this.pageSize = (pageSize != null) ? Integer.valueOf(pageSize) : Const.DEFAULT_PAGE_SIZE;
		this.pageNum = (pageNum != null) ? Integer.valueOf(pageNum) : Const.FIRST_PAGE_NUM;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	
}
