package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Aaron.H.Shen
 * @date 1/28/2021 11:19 AM
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cheap_goods")
public class CheapGoods {

    @Id
    private Integer goodsId;

    private BigDecimal price;

    private Boolean status;

    private Integer setupAdmin;
}
