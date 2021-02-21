package com.sht.vehicle.controller;

import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.Car;
import com.sht.vehicle.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/2/16 11:50
 */
@RestController
@RequestMapping("api/car")
@Api(tags = "车辆管理")
public class CarController extends BaseController{

    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity<RestResponse> save(@RequestBody Car car) {
        return ResponseEntity.ok(carService.save(car));
    }

    @GetMapping("findByPage")
    public ResponseEntity<RestResponse> findByPage(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "rows", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(SUCCESS(carService.findByPage(page,size)));
    }

    /*@GetMapping("findById")
    public ResponseEntity<RestResponse> findById(@RequestParam(name = "id") Integer id,
                                                 @ApiParam(name = "类型，参数123，分别代表保养、加油、调度") @RequestParam(name = "type", required = false) String type) {
        return ResponseEntity.ok(carService.findById(id, type));
    }*/

    @DeleteMapping
    public ResponseEntity<RestResponse> deleteById(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(carService.deleteById(id));
    }
}
