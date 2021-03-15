package com.eu.classroom.entity;

import com.eu.classroom.dto.RoleDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Aaron
 * @date 2021/3/10 20:46
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@ApiModel(description = "权限")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String notes;

    @ApiModelProperty(notes = "页面路由结点")
    private String node;

    @JsonBackReference(value = "role-user")
    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @JsonBackReference(value = "admin-role")
    @ManyToMany(mappedBy = "roles")
    private List<Admin> admins;

    public Role(RoleDto roleDto) {
        this.id = roleDto.getId();
        this.notes = roleDto.getNotes();
        this.node = roleDto.getNotes();
    }
}
