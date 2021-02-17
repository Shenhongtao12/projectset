package com.eurasia.food.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Aaron
 * @date 2021/1/15 22:41
 */
@Data
@Entity
@Table(name = "admin")
@DynamicUpdate
public class Admin implements Serializable {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String adminName;

    private String password;

    private String role;

    private String image;

    private String description;
}
