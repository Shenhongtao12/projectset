package com.eu.classroom.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author Aaron
 * @date 2021/3/10 20:51
 */
@Data
@Entity
@ApiModel(description = "管理员")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*@ManyToMany(mappedBy = "users")
    private List<Role> roles;*/
}
