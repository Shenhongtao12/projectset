package com.sht.vehicle.controller;

import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.dto.SchedulingDto;
import com.sht.vehicle.entity.Scheduling;
import com.sht.vehicle.service.SchedulingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/20 14:19
 */
@RestController
@RequestMapping("api/car/scheduling")
@Api(tags = "车辆申请/调度管理")
public class SchedulingController extends BaseController {

    @Autowired
    private SchedulingService schedulingService;

    @PostMapping("/save")
    public ResponseEntity<RestResponse> saveOrUpdate(@RequestBody SchedulingDto schedulingDto) {
        return ResponseEntity.ok(schedulingService.save(new Scheduling(schedulingDto)));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(schedulingService.delete(id));
    }

    @GetMapping
    public ResponseEntity<RestResponse> findByPage(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "carId", required = false) Integer carId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "rows", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(SUCCESS(schedulingService.findByPage(startDate, endDate, userId, carId, status, page, size)));
    }
}
