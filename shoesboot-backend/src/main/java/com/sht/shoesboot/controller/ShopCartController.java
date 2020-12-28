package com.sht.shoesboot.controller;

import com.sht.shoesboot.entity.ShopCart;
import com.sht.shoesboot.service.RedisService;
import com.sht.shoesboot.service.ShopCartService;
import com.sht.shoesboot.utils.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2020/12/27 23:39
 */
@RestController
@RequestMapping("api/shopCart")
public class ShopCartController extends BaseController{

    @Autowired
    private ShopCartService shopCartService;

    @Autowired
    private RedisService redisService;

    @PutMapping
    public ResponseEntity<RestResponse> queryInventory (@RequestBody ShopCart shopCart) {
        String inventory = redisService.getData("shoes_goods_" + shopCart.getGoodsId());
        if (StringUtils.isNoneEmpty(inventory)) {
            if (Integer.parseInt(inventory) >= shopCart.getAmount()) {
                shopCartService.update(shopCart);
                return ResponseEntity.ok(SUCCESS(Integer.valueOf(inventory), "成功"));
            }
            else {
                return ResponseEntity.badRequest().body(ERROR(400, "库存不足"));
            }
        }
        return ResponseEntity.badRequest().body(ERROR(400, "该商品已下架"));
    }

    @PostMapping
    public ResponseEntity<RestResponse> saveShopCart(@RequestBody ShopCart shopCart){
        return ResponseEntity.ok(SUCCESS(""));
    }
}
