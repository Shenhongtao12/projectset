package com.sht.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/15 11:10
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Refuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal money;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inDate;

    @ManyToOne(targetEntity = User.class, optional = false)
    @JoinColumn(name = "u_id")
    private User user;

    @ManyToOne(targetEntity = Car.class, optional = false)
    @JoinColumn(name = "c_id")
    private Car car;
}
