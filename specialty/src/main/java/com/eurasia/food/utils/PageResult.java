package com.eurasia.food.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResult<T> {
	/**
	 * 总条数
	 */
	private Long total;
	/**
	 * 总页数
	 */
	private Integer pages;
	/**
	 * 当前页数据
	 */
	private List<T> data;

	public PageResult() {
	}

	public PageResult(Long total, List<T> data){
		this.total = total;
		this.data = data;
	}

	public PageResult(Long total, Integer pages, List<T> data) {
		this.total = total;
		this.pages = pages;
		this.data = data;
	}
}
