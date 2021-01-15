package com.eurasia.specialty.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 熱門事件
 * @author Hongtao Shen
 * @date 2020/5/23 - 13:29
 **/
@Table(name = "sp_matter")
@Data
@Entity
public class Matter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String matterUrl;
    private String createTime;

    @Transient
    private Integer MatterNum;

}
