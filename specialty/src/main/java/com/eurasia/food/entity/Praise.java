package com.eurasia.food.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Aaron
 * @date 2020/5/24 - 19:58
 **/
@Table(name = "praise")
@Entity
@Data
public class Praise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String type;
    private Integer typeId;
    private Integer typeUserId;
    private Integer userId;
    private String createTime;

    @Transient
    private User user;
}
