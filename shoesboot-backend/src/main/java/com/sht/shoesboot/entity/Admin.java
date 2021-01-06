package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;

/**
 * @author Aaron
 * @date 2021/1/6 20:40
 */
@Getter
@Setter
@NoArgsConstructor
public class Admin {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String adminName;

    private String password;

    private String role;

    private String image;

    private String description;
}
