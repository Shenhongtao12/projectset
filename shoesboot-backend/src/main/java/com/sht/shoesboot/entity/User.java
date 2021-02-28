package com.sht.shoesboot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @NotNull(message = "用户名不能为空")
    @NotBlank(message = "邮箱不能为空")
    @Length(min = 4, max = 20, message = "用户名长度应在4-20字符")
    private String username;

    @NotNull(message = "密码不能为空")
    @NotBlank(message = "邮箱不能为空")
    @Length(min = 6, max = 20, message = "密码长度应在6-20字符")
    private String password;

    @NotNull(message = "邮箱不能为空")
    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;

    @NotBlank(message = "手机号不能为空")
    @NotNull(message = "手机号不能为空")
    @Length(min = 11, max = 11)
    private String phone;

    private Boolean vip;

    private LocalDateTime inDate;

    private LocalDateTime lastLoginDate;

    public User(Register register) {
        this.username = register.getUsername();
        this.password = register.getPassword();
        this.email = register.getEmail();
        this.vip = register.getVip();
    }

    public User(Admin admin) {
        this.id = admin.getId();
        this.username = admin.getAdminName();
    }
}
