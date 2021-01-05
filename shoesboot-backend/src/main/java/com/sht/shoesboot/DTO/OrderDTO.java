package com.sht.shoesboot.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Aaron
 * @date 2021/1/4 20:38
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {

    private Integer goodsId;
    private String title;
    private String images;
    private BigDecimal price;

    private Integer orderId;
    private String orderNumber;
    private Integer amount;
    private BigDecimal money;
    private Date inDate;
}
