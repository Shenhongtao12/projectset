package com.sht.shoesboot.DTO;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author Aaron.H.Shen
 * @date 12/29/2020 3:54 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShopCartDTO {

    private Integer goodsId;
    private String title;
    private String image;
    private BigDecimal price;

    private Integer cartId;
    private Integer amount;
}