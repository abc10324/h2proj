package com.sam.h2proj.util;

import java.util.List;

public class Page<T> {

	private Integer number;
	
	private Integer size;
	
	private Integer totalPage;
	
	private List<T> content;
	
	public Page(List<T> content,Integer number,Integer size,Integer totalCount) {
		this.content = content;
		
		this.number = number;
		this.size = size;
		this.totalPage = (int)(Math.ceil((double) totalCount) / size);
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
	
	public boolean isFirst() {
		return number == 1;
	}
	
	public boolean isLast() {
		return number == totalPage;
	}
	
	public boolean hasNext() {
		return number != totalPage;
	}
	
	
	
}
