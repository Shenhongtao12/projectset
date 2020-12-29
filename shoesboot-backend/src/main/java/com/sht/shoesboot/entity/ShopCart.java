package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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

    public Integer goodsId;

    @Size(min = 1)
    @NotNull(message = "加购数量不能为空")
    public Integer amount;

    public Integer userId;

    public Date inDate;
}
