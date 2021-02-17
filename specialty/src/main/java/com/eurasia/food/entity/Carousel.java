package com.eurasia.food.entity;

import lombok.Data;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 轮播图的标题
     */
    private String title;

    /**
     * 每一张轮播图Url
     */
    private String url;
}
