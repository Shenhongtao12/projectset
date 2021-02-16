package com.sht.vehicle.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Aaron
 * @date 2021/2/15 10:44
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 汽车品牌
     */
    @ApiModelProperty(name = "汽车品牌")
    private String brand;

    /**
     * 备注
     */
    @ApiModelProperty(name = "备注")
    private String note;

    /**
     * 限载人数
     */
    @ApiModelProperty(name = "限载人数")
    private Integer number;

    /**
     * 入库时间
     */
    @ApiModelProperty(name = "入库时间", notes = "由新增汽车时，后台自动录入")
    private LocalDate inDate;

    @OneToMany(targetEntity = Scheduling.class)
    @JoinColumn(name = "c_id", referencedColumnName = "id")
    private Set<Scheduling> schedulingSet = new HashSet<>();
}
