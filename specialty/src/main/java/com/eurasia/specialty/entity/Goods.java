package com.eurasia.specialty.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Administrator
 */
@Data
@Table(name = "sp_goods")
@Entity
@DynamicUpdate
public class Goods implements Serializable
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String intro;
	private Double price;
	private String images;

	@Column(name = "create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private Integer state;
	private Integer classifyId;

	/**
	 * 判断用户是否收藏该产品
	 */
	@Transient
	private boolean code;

}
