package com.sht.vehicle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
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
@Entity
@ApiModel("申请车辆使用")
public class Scheduling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;

    @ApiModelProperty(name = "申请状态", notes = "1 : 申请，待审批\n2 : 审批通过\n3 : 审批未通过")
    @Column(columnDefinition = "CHAR(1) default '1' not null")
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "u_id")
    private User user;

    /**
     * 可选属性optional=false,表示author不能为空。删除文章，不影响用户
     * @Getter(AccessLevel.NONE)
     * @ManyToOne(targetEntity = Car.class, cascade={CascadeType.MERGE,CascadeType.REFRESH}, optional=false)
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "c_id")
    private Car car;

}
