package com.sam.h2proj.util;

import java.awt.color.CMMException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.springframework.util.StringUtils;

import com.sam.h2proj.util.PageInfo.Sort.Direction;

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
	
	/** 排序 **/
	private Sort sort;

	public PageInfo(Integer pageSize, Integer pageNum, Sort sort) {
		// 空值檢查, 初步避免NullPointerException
		// 若為空值則採用Const中定義的系統預設值
		this.pageSize = (pageSize != null) ? pageSize : Const.DEFAULT_PAGE_SIZE;
		this.pageNum = (pageNum != null) ? pageNum : Const.FIRST_PAGE_NUM;
		
		this.sort = sort;
	}
	
	public PageInfo(Map<String,Object> paramMap) {
		String pageSize = String.valueOf(paramMap.get(Const.PAGE_SIZE));
		String pageNum = String.valueOf(paramMap.get(Const.PAGE_NUM));
	
		// 空值檢查, 初步避免NullPointerException
		// 若為空值則採用Const中定義的系統預設值
		this.pageSize = (pageSize != null) ? Integer.valueOf(pageSize) : Const.DEFAULT_PAGE_SIZE;
		this.pageNum = (pageNum != null) ? Integer.valueOf(pageNum) : Const.FIRST_PAGE_NUM;
		
		if(hasSortInfo(paramMap)) {
			String[] columnNames = String.valueOf(paramMap.get(Const.ORDER_COLUMNS)).split(",");
			String direction = String.valueOf(paramMap.get(Const.ORDER_DIRECTION));
			
			if(Direction.ASC.toString().equalsIgnoreCase(direction)) {
				this.sort = new Sort(Direction.ASC, columnNames);
			} else if(Direction.DESC.toString().equalsIgnoreCase(direction)) {
				this.sort = new Sort(Direction.DESC, columnNames);
			}
		}
	}
	
	/**
	 * *內部檢查使用
	 * 檢查是否含有排序所需參數 columnNames與direction
	 * @param paramMap
	 * @return 是否含有排序所需參數
	 */
	private boolean hasSortInfo(Map<String,Object> paramMap) {
		
		// 檢查columnNames與direction參數是否存在
		if(paramMap.get(Const.ORDER_COLUMNS) != null && 
		   paramMap.get(Const.ORDER_DIRECTION) != null) {
			
			// 檢查columnNames與direction參數是否有值(空白不能算有值)
			if(StringUtils.hasText(String.valueOf(paramMap.get(Const.ORDER_COLUMNS))) &&
			   StringUtils.hasText(String.valueOf(paramMap.get(Const.ORDER_DIRECTION)))) {
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * *外部呼叫使用
	 * 檢查是否含有排序的資訊
	 * @return 是否含有排序的資訊
	 */
	public boolean hasSortInfo() {
		return this.sort != null;
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
	
	/**
	 * 取得排序物件 含排序方向及欄位清單
	 * @return 排序物件
	 */
	public Sort getSort() {
		return sort;
	}

	public void setSort(Sort sort) {
		this.sort = sort;
	}





	/**
	 * 內部類別 -排序用物件 <br>
	 * 內部包含需排序的欄位清單及排序的方向
	 * 
	 * @author SamKao
	 *
	 */
	public static class Sort {
		
		/** 排序方向 **/
		private Direction direction;
		
		/** 欄位名稱陣列 **/
		private String[] columeNames;
		
		public Sort(Direction direction,String[] columnNames) {
			this.direction = direction;
			this.columeNames = columnNames;
		}
		
		public Direction getDirection() {
			return direction;
		}

		public String[] getColumeNames() {
			return columeNames;
		}

		/** 排序方向 定義用常數 Enum類別 **/
		public static enum Direction {
			ASC,DESC
		}
	}
}
