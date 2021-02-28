package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2020/12/27 19:41
 */
@Getter
@Setter
@NoArgsConstructor
public class Classify {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @NotBlank(message = "分类名不能为空")
    @NotNull(message = "分类名不能为空")
    @Length(min = 1, max = 30, message = "分类名长度应大于1，小于30")
    private String name;

    private LocalDateTime inDate;
}
