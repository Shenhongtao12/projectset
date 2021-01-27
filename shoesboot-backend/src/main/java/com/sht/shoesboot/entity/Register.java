package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Aaron
 * @date 2020/12/16 23:13
 */
@Getter
@Setter
@NoArgsConstructor
public class Register {

    @NotNull(message = "用户名不能为空")
    @NotBlank
    @Length(min = 4, max = 20, message = "用户名长度应在4-20字符")
    private String username;

    @NotNull(message = "密码不能为空")
    @NotBlank
    @Length(min = 6, max = 20, message = "密码长度应在6-20字符")
    private String password;

    @NotNull(message = "邮箱不能为空")
    @NotBlank
    @Email
    private String email;

    @NotNull(message = "验证码不能为空")
    @NotBlank
    private String emailCode;

    private Boolean vip;
}
