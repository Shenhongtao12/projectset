package com.sht.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Aaron
 * @date 2021/2/15 11:10
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Refuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate inDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "u_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "c_id")
    private Car car;
}
