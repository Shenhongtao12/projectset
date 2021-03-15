package com.eu.classroom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
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

    @ApiModelProperty(notes = "是否停用")
    private Boolean status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inDate;
}
