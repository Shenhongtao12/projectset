package com.eurasia.food.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 留言
 * @author Administrator
 */
@Data
@Table(name = "comment")
@Entity
public class Comment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Integer commentId;
	private String content;
	private String createTime;
	private Integer number;
	/**
	 * 留言的发布人id
	 */
	private Integer userId;
	private Integer postId;

	private Integer matterId;
	private Boolean isShow;
	/**
	 *  null 用来区分留言和回复
	 */
	private Integer leaf;

	@Transient
	private String title;
	@Transient
	private User user;
	@Transient
	private List<Reply> replyList;

}
