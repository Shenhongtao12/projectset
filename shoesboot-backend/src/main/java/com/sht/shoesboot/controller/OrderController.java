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
    @Autowired
    private GoodsService goodsService;

    @PostMapping
    public ResponseEntity<RestResponse> createOrder(@Valid @RequestBody Order order) {
        Map<String, Object> goods = goodsService.findById(order.getId());
        double sumPrice = Double.parseDouble(goods.get("price").toString()) * order.getAmount();
        if (!order.getMoney().equals(new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_CEILING))){
            return ResponseEntity.badRequest().body(ERROR("金额计算错误"));
        }
        RestResponse response = orderService.createOrder(order);
        if (response.getCode() != 200) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<RestResponse> queryPage(@RequestParam(name = "status", required = false) String status,
                                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(SUCCESS(orderService.queryPage(status, userId, page, size)));
    }
}
