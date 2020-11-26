package com.sht.shoesboot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

/**
 * @author Aaron
 * @date 2020/11/25 20:31
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private Integer id;

    private String username;
    private String password;
}
