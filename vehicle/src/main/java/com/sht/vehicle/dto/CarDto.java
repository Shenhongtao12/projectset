package com.sht.vehicle.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

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

    /**
     * 入库时间
     */
    @ApiModelProperty(name = "入库时间", notes = "由新增汽车时，后台自动录入")
    private LocalDate inDate;
}
