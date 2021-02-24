package com.sht.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.beans.binding.When;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Aaron
 * @date 2021/2/12 11:59
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *用户名
     */
    @NotNull(message = "用户名不能为空")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 驾驶证等级
     */

    private String level;

    /**
     * 是否为驾驶员
     */
    private Boolean driver;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 出生年月日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    /**
     * 新用户登录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonBackReference
    @OneToMany(targetEntity = Refuel.class, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Refuel> refuelList;

    @JsonBackReference
    @OneToMany(targetEntity = Upkeep.class, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Upkeep> upkeepList;
}
