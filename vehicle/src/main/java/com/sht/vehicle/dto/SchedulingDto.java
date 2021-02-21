package com.sht.vehicle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sht.vehicle.entity.Car;
import com.sht.vehicle.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/15 11:19
 */
@Getter
@Setter
@NoArgsConstructor
public class SchedulingDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(notes = "申请说明")
    private String notes;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;

    @ApiModelProperty(name = "申请状态", notes = "1 : 申请，待审批\n2 : 审批通过\n3 : 审批未通过")
    @Column(columnDefinition = "CHAR(1) default '1' not null")
    private String status;

    @Column(name = "u_id")
    private Integer uId;

    @Column(name = "c_id")
    private Integer cId;

}
