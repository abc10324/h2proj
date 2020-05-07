package com.sam.h2proj.util;

public class Const {

	/** 每頁筆數 預設值 **/
	public static final Integer DEFAULT_PAGE_SIZE = 10;
	
	/** 第一頁 頁碼 **/
	public static final Integer FIRST_PAGE_NUM = 1;
	
	/** 每頁筆數 參數名稱 **/
	public static final String PAGE_SIZE = "pageSize";
	
	/** 目前頁碼 參數名稱 **/
	public static final String PAGE_NUM = "pageNum";

	/** 資料庫連線字串 **/
	public static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
	
	/** 資料庫帳號 **/
	public static final String DB_USERNAME = "sa";
	
	/** 資料庫密碼 **/
	public static final String DB_PASSWORD = "";
	
	/** 資料庫 JDBC Driver 完整類別名稱 **/
	public static final String DB_DRIVER_CLASS_NAME = "org.h2.Driver";
	
}
