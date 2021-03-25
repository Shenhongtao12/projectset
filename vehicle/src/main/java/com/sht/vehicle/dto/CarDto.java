package com.sht.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/24 23:19
 */
@Data
public class CarDto {

    private Integer id;

    /**
     * 汽车品牌
     */
    @ApiModelProperty(notes = "汽车品牌")
    private String brand;

    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注")
    private String notes;

    /**
     * 限载人数
     */
    @ApiModelProperty(notes = "限载人数")
    private Integer number;

    @ApiModelProperty(notes = "车牌号")
    private String licensePlateNumber;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "购车时间")
    private LocalDateTime buyDateTime;

    /**
     * 入库时间
     */
    @ApiModelProperty(name = "入库时间", notes = "由新增汽车时，后台自动录入")
    private LocalDate inDate;
}
