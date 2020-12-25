package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author Aaron
 * @date 2020/12/24 23:17
 */
@Getter
@Setter
@NoArgsConstructor
public class Favorite {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private Integer goodsId;

    private Integer userId;

    private Date inDate;

}
