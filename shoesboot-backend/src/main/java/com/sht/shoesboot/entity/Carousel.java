package com.sht.shoesboot.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 轮播图
 * @author Aaron
 * @date 2020/5/16 - 14:14
 **/
@Data
@Entity
@Table(name = "carousel")
public class Carousel implements Serializable {
    /**
     * ID
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    /**
     * 轮播图的标题
     */
    private String title;

    /**
     * 每一张轮播图Url
     */
    private String url;

    private String link;
}
