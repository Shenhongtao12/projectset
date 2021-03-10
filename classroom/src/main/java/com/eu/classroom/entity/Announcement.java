package com.eu.classroom.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/3/10 20:37
 */
@Entity
@Data
@ApiModel(description = "实验公告")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(notes = "公告内容")
    private String content;

    @ApiModelProperty(notes = "公告状态：true | false")
    private Boolean status;

    private LocalDateTime inDate;
}
