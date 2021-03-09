package com.sht.qrcode.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Aaron
 * @date 2021/3/8 22:38
 */
@Data
@Table(name = "qr_master")
public class QrEntity {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String content;

    private String image;
}
