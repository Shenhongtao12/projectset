package com.sht.kitchen.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/12 11:59
 */
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *用户名
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密匙
     */
    @Transient
    private String session_key;

    /**
     * 新用户登录时间
     */
    private LocalDateTime createTime;

    /**
     * 登录code
     */
    @Transient
    private String js_code;

    /**
     * 登录错误时的状态码
     */
    @Transient
    private Integer errcode;

}
