package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Aaron
 * @date 2020/12/27 23:19
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "shop_cart")
public class ShopCart {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private Integer goodsId;

    private Integer amount;

    private Integer userId;

    private Date inDate;
}
