package com.eu.classroom.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @author Aaron
 * @date 2021/3/10 20:51
 */
@Data
@Entity
@ApiModel(description = "管理员")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String adminName;

    private String password;

    private String avatarUrl;

    private String phone;

    @ManyToMany
    @JoinTable(name = "admin_role", joinColumns = @JoinColumn(name = "admin_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
