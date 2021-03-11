package com.eu.classroom.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aaron
 * @date 2021/3/10 20:23
 */
@Entity
@Data
@ApiModel(description = "实验器材仪器")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 器材名称
     */
    private String name;

    @ApiModelProperty(notes = "器材使用说明")
    private String instruction;

    @ApiModelProperty(notes = "现有库存")
    private Integer inventory;

    @ApiModelProperty(notes = "已借出数量")
    private Integer borrowNum;

    private LocalDateTime inDate;

    /*@ManyToMany
    @JoinTable(name = "equipment_user", joinColumns = @JoinColumn(name = "equipment_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;*/
}
