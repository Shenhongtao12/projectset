package com.sht.shoesboot.controller;

import com.sht.shoesboot.entity.Order;
import com.sht.shoesboot.service.GoodsService;
import com.sht.shoesboot.service.OrderService;
import com.sht.shoesboot.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<RestResponse> createOrder(@Valid @RequestBody Order order) {
        System.out.println("userID: " + userId);
        if (userId != null) {
            order.setUserId(userId);
        }
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping
    public ResponseEntity<RestResponse> queryPage(@RequestParam(name = "status", required = false) String status,
                                                  @RequestParam(name = "userId", required = false, defaultValue = "0") Integer userId,
                                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(SUCCESS(orderService.queryPage(status, userId, page, size)));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if (orderService.delete(id)) {
            return ResponseEntity.ok(SUCCESS("删除成功"));
        }else {
            return ResponseEntity.ok(ERROR("删除失败"));
        }
    }
}
