package com.eu.classroom.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/3/11 20:17
 */
@Data
@Entity
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer equipmentId;

    private Integer borrowNum;

    @ApiModelProperty(notes = "申请借实验器材状态：1-待审批  2-审批通过  3-未通过")
    private Integer status;

    @ApiModelProperty(notes = "归还日期")
    private LocalDateTime giveBackDate;

    @ApiModelProperty(notes = "备注")
    private String notes;

    private LocalDateTime inDate;

    private Integer userId;

    @Transient
    private Equipment equipment;

    @Transient
    private User user;
}
