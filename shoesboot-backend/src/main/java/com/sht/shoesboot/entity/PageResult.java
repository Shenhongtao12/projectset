package com.sht.shoesboot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Aaron
 */
@Getter
@Setter
@AllArgsConstructor
public class PageResult<T> {
	private Long total;
	private Integer pages;
	private List<T> data;

	public PageResult() {
	}

	public PageResult(Long total, List<T> data){
		this.total = total;
		this.data = data;
	}

}
