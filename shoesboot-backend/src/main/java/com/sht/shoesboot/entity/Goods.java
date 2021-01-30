package com.sht.shoesboot.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Aaron
 * @date 2020/12/19 22:41
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goods {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @NotBlank(message = "标题不能为空")
    @NotNull(message = "标题不能为空")
    @Length(min = 1, max = 200, message = "分类名长度应大于1，小于200")
    public String title;

    public BigDecimal price;

    private Integer inventory;

    public String images;

    @NotBlank(message = "品牌不能为空")
    @NotNull(message = "品牌不能为空")
    @Length(min = 1, max = 100, message = "品牌长度应大于1，小于200")
    public String brand;

    public Boolean shelf;

    @Transient
    public BigDecimal originalPrice;

    public Goods (GoodsHistory history) {
        this.id = history.getId();
        this.title = history.getTitle();
        this.price = history.getPrice();
        this.inventory = history.getInventory();
        this.images = history.getImages();
        this.brand = history.getBrand();
        this.shelf = false;
    }
}
