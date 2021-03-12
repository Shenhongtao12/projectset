package com.eu.classroom.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/3/12 21:42
 */
@Data
@Entity
@ApiModel(description = "预定实验室")
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("实验室id")
    private Integer laboratoryId;

    private Integer borrowNum;

    @ApiModelProperty(notes = "申请借实验室状态：1-待审批  2-审批通过  3-未通过")
    private Integer status;

    @ApiModelProperty(notes = "开始日期")
    private LocalDateTime startDateTime;

    @ApiModelProperty(notes = "结束日期")
    private LocalDateTime endDateTime;

    @ApiModelProperty(notes = "备注")
    private String notes;

    private LocalDateTime inDate;

    private Integer userId;

    @Transient
    private User user;

    @Transient
    private Laboratory laboratory;
}
