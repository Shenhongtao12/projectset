package com.sht.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sht.vehicle.dto.SchedulingDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/15 11:19
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@ApiModel("申请车辆使用")
public class Scheduling implements Serializable{
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

    @Transient
    private User user;

    @Transient
    private Car car;

    public Scheduling(SchedulingDto schedulingDto) {
        this.id = schedulingDto.getId();
        this.notes = schedulingDto.getNotes();
        this.status = schedulingDto.getStatus();
        this.startDateTime = schedulingDto.getStartDateTime();
        this.endDateTime = schedulingDto.getEndDateTime();
        this.cId = schedulingDto.getCId();
        this.uId = schedulingDto.getUId();
    }
}
