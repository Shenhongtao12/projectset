package com.sht.vehicle.dto;

import com.sht.vehicle.entity.Car;
import com.sht.vehicle.entity.User;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/20 12:06
 */
@Data
public class RefuelDto {
    private Integer id;

    private BigDecimal money;

    private String notes;

    private LocalDateTime inDate;

    private User user;

    private Car car;
}
