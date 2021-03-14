package com.eu.classroom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Aaron
 * @date 2021/3/10 20:21
 */
@Entity
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class User {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 密码
     */
    private String password;

    private String email;

    /**
     * 新用户首次注册时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inDate;

    /**
     * 手机号
     */
    private String phone;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}
