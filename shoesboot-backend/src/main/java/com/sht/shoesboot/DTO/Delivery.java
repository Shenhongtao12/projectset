package com.sht.shoesboot.DTO;

import lombok.Data;

/**
 * @author Aaron
 * @date 2021/3/1 21:10
 */
@Data
public class Delivery {

    private Integer id;

    /**
     * 快递公司
     */
    private String express;

    private String expressNum;

    private String status;
}
