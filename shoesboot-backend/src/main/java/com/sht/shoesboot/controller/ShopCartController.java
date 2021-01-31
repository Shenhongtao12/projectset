package com.sht.shoesboot.controller;

import com.alibaba.fastjson.JSONObject;
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
    public ResponseEntity<RestResponse> shoppingCart (@RequestBody ShopCart shopCart) {
        String inventory = redisService.getData("shoes_goods_" + shopCart.getGoodsId());
        if (StringUtils.isNoneEmpty(inventory)) {
            if (Integer.parseInt(inventory) >= shopCart.getAmount()) {
                JSONObject response = new JSONObject();
                response.put("inventory", inventory);
                if (shopCart.getId() != null) {
                    shopCartService.update(shopCart);
                    response.put("cartId", shopCart.getId());
                }else {
                    response.put("cartId", shopCartService.save(shopCart));
                }
                return ResponseEntity.ok(SUCCESS(response, "成功"));
            }
            else {
                return ResponseEntity.ok().body(ERROR(400, "库存不足"));
            }
        }
        return ResponseEntity.ok().body(ERROR(400, "该商品已下架"));
    }

    @GetMapping
    public ResponseEntity<RestResponse> queryPage(@RequestParam(name = "page") Integer page,
                                                  @RequestParam(name = "size") Integer size) {
        return ResponseEntity.ok(SUCCESS(shopCartService.queryPage(userId, page, size)));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if (shopCartService.delete(id)) {
            return ResponseEntity.ok(SUCCESS("删除成功"));
        }
        return ResponseEntity.ok().body(ERROR("删除失败,不存在id: "+ id +"的数据"));
    }
}
