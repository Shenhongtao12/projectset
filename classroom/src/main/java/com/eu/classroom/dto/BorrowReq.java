package com.eu.classroom.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Aaron
 * @date 2021/3/11 21:52
 */
@Data
public class BorrowReq {

    @ApiModelProperty(notes = "借还记录或预定实验室的id：borrow id")
    private Integer id;

    @ApiModelProperty(notes = "审批：0-拒绝  1-同意")
    private String operation;
}
