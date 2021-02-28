package com.sht.vehicle.controller;

import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.Refuel;
import com.sht.vehicle.service.RefuelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/17 16:48
 */
@RestController
@RequestMapping("api/car/refuel")
@Api(tags = "加油管理")
public class RefuelController extends BaseController{

    @Autowired
    private RefuelService refuelService;

    @PostMapping
    public ResponseEntity<RestResponse> saveOrUpdate(@RequestBody Refuel refuel) {
        return ResponseEntity.ok(refuelService.save(refuel));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(refuelService.delete(id));
    }

    @GetMapping
    public ResponseEntity<RestResponse> findByPage(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
            @RequestParam(name = "carId", required = false) Integer carId,
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "rows", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(SUCCESS(refuelService.findByPage(startDate, endDate, userId, carId, page, size)));
    }

    //@GetMapping("findById")
    public ResponseEntity<RestResponse> findById(@RequestParam(name = "id") Integer id,
                                                 @ApiParam(value = "car or user", required = true) @RequestParam(name = "type") String type) {

        return ResponseEntity.ok(refuelService.findById(id, type));
    }
}
