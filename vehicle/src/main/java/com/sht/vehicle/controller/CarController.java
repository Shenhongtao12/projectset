package com.sht.vehicle.controller;

import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.Car;
import com.sht.vehicle.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/2/16 11:50
 */
@RestController
@RequestMapping("api/car")
public class CarController extends BaseController{

    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity<RestResponse> save(@RequestBody Car car) {
        return ResponseEntity.ok(carService.save(car));
    }

    @GetMapping
    public ResponseEntity<RestResponse> findByPage(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "rows", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(SUCCESS(carService.findByPage(page,size)));
    }
}
