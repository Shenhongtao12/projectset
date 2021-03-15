package com.eu.classroom.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Aaron
 * @date 2021/3/13 21:46
 */
@Data
public class RoleDto {
    private Integer id;

    private String notes;

    private String node;
}
