package com.sht.shoesboot.controller;

import com.sht.shoesboot.service.OrderService;
import com.sht.shoesboot.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 * @date 2020/12/29 22:22
 */
@RestController
@RequestMapping("api/order")
public class OrderController extends BaseController{

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<RestResponse> createOrder() {
        return ResponseEntity.ok(SUCCESS(""));
    }
}
