package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Aaron
 * @date 2021/1/26 21:35
 */
@Table(name = "order_goods")
@Getter
@Setter
@NoArgsConstructor
public class OrderGoods {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private Integer orderId;
    private Integer goodsId;
    private Integer cartId;
    private String title;
    private BigDecimal price;
    private Integer amount;
    private String image;
}
