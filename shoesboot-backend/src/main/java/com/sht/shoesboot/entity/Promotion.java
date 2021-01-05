package com.sht.shoesboot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Aaron.H.Shen
 * @date 1/4/2021 5:25 PM
 */
@Getter
@Setter
@NoArgsConstructor
public class Promotion {

    @Id
    private Integer id;

    private BigDecimal price;

    private Integer inventory;

    private Date startTime;

    private Date endTime;
}
