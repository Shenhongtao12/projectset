package com.eu.classroom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inDate;
}
