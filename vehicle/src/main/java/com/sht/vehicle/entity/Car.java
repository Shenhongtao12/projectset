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
    @ApiModelProperty(notes = "汽车品牌")
    private String brand;

    /**
     * 备注
     */
    @ApiModelProperty(notes = "备注")
    private String note;

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

    /**
     * //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
     * //拥有mappedBy注解的实体类为关系被维护端
     * //mappedBy="author"中的author是Article中的author属性
     */
    @OneToMany(mappedBy = "car", targetEntity = Scheduling.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Scheduling> schedulingSet = new HashSet<>();
}
