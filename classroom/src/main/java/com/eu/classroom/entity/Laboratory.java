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
 * @date 2021/3/10 20:48
 */
@Entity
@Data
@ApiModel(description = "实验室")
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(notes = "实验室name")
    private String name;

    @ApiModelProperty(notes = "实验室功能说明")
    private String instruction;

    @ApiModelProperty(notes = "座位数量")
    private Integer seating;

    private LocalDateTime inDate;
}
