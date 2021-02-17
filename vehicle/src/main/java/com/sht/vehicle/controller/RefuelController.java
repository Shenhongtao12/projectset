package com.sht.vehicle.controller;

import com.sht.vehicle.service.RefuelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 * @date 2021/2/17 16:48
 */
@RestController
@RequestMapping("api/car/refuel")
public class RefuelController {

    @Autowired
    private RefuelService refuelService;
}
