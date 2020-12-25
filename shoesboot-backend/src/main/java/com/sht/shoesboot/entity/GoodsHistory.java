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
 * @date 2020/12/24 22:04
 */
@Getter
@Setter
@Table(name = "goods_history")
public class GoodsHistory extends Goods{

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private int goodsId;

    private Date inDate;

    public GoodsHistory(Goods goods) {
        this.goodsId = goods.getId();
        this.title = goods.getTitle();
        this.images = goods.getImages();
        this.brand = goods.getBrand();
        this.price = goods.getPrice();
        this.inDate = new Date();
    }
}
