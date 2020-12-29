package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @author Aaron
 * @date 2020/12/29 22:23
 */
@Getter
@Setter
@NoArgsConstructor
public class Order extends ShopCart{

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private BigDecimal money;

    private String orderNumber;
}
