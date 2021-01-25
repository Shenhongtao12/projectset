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
    @Autowired
    private GoodsService goodsService;

    @PostMapping
    public ResponseEntity<List<RestResponse>> createOrder(@Valid @RequestBody List<Order> orderList) {
        List<RestResponse> responses = new ArrayList<>();
        for (Order order : orderList) {
            Map<String, Object> goods = goodsService.findById(order.getGoodsId());
            double sumPrice = Double.parseDouble(goods.get("price").toString()) * order.getAmount();
            /*if (order.getMoney().equals(new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_CEILING))){
                responses.add(ERROR("金额计算错误"));
                continue;
            }*/
            order.setMoney(new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_CEILING));
            order.setUserId(userId);
            RestResponse response = orderService.createOrder(order);
            responses.add(response);
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<RestResponse> queryPage(@RequestParam(name = "status", required = false, defaultValue = "0") String status,
                                                  @RequestParam(name = "userId", required = false, defaultValue = "0") Integer userId,
                                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(SUCCESS(orderService.queryPage(status, userId, page, size)));
    }
}
