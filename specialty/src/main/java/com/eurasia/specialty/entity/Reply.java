package com.eurasia.specialty.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 给留言的回复
 */
@Data
@Entity
@Table(name = "sp_reply")
@DynamicUpdate
@ApiModel("回复实体类")
public class Reply implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String createTime;
	private String content;
	private Integer number;
	/**
	 * 等于0时就是一个树的末尾
	 */
	private Integer leaf;
	/**
	 * 发布回复的人id
	 */
	private Integer userId;
	/**
	 * 帖子的id
	 */
	private Integer postId;
	private Integer commentId;
	/**
	 * 给谁回复的人id 父id
	 */
	private Integer nameId;
	/**
	 * reply的父id
	 */
	private Integer parentId;
	/**
	 * 父name
	 */
	private String parentName;

	/**
	 * 判断点赞
	 */
	@Transient
	private Object state;
	@Transient
	private User user;
	@Transient
	private List<Reply> replyList;


}
