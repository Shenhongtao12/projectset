package com.eu.classroom.dto;

import lombok.Data;

/**
 * @author Aaron
 * @date 2021/2/24 23:13
 */
@Data
public class UserDto {

    private Integer id;

    /**
     *用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
