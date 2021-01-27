package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/29 22:23
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order{

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private BigDecimal money;

    private String orderNumber;

    /**
     * 创建成功: C
     */
    private String status;

    private Integer userId;

    private Date inDate;

    @Transient
    private List<OrderGoods> orderGoodsList;
}
