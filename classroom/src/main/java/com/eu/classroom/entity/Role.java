package com.eu.classroom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Aaron
 * @date 2021/3/10 20:46
 */
@Data
@Entity
@ApiModel(description = "权限")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nodes;

    @ApiModelProperty(notes = "页面路由结点")
    private String node;

    @JsonBackReference(value = "role-user")
    @ManyToMany
    @JoinTable(name = "role_user", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;
}
