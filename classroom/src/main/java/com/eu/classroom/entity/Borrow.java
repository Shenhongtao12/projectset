package com.eu.classroom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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

    @ApiModelProperty(notes = "申请借实验器材状态：1-待审批  2-审批通过  3-未通过  4-退还")
    private Integer status;

    @ApiModelProperty(notes = "归还日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime giveBackDate;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private Integer userId;

    @Transient
    private Equipment equipment;

    @Transient
    private User user;
}
