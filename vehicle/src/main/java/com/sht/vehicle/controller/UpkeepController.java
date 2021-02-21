package com.sht.vehicle.controller;

import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.Upkeep;
import com.sht.vehicle.service.UpkeepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/17 16:48
 */
@RestController
@RequestMapping("api/car/upkeep")
@Api(tags = "保养记录管理")
public class UpkeepController extends BaseController {

    @Autowired
    private UpkeepService upkeepService;

    @PostMapping
    public ResponseEntity<RestResponse> saveOrUpdate(@RequestBody Upkeep upkeep) {
        return ResponseEntity.ok(upkeepService.save(upkeep));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(upkeepService.delete(id));
    }

    @GetMapping
    public ResponseEntity<RestResponse> findByPage(
            @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false) LocalDateTime endDate,


            @RequestParam(name = "carId", required = false) Integer carId,
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "rows", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(SUCCESS(upkeepService.findByPage(startDate, endDate, carId, userId, page, size)));
    }

    //@GetMapping("findById")
    public ResponseEntity<RestResponse> findById(@RequestParam(name = "id") Integer id,
                                                 @ApiParam(value = "car or user", required = true) @RequestParam(name = "type") String type) {

        return ResponseEntity.ok(upkeepService.findById(id, type));
    }
}
