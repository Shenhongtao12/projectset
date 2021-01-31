package com.sht.shoesboot;

import com.sht.shoesboot.service.GoodsService;
import com.sht.shoesboot.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Aaron
 * @date 2020/12/27 13:00
 */
@SpringBootTest
public class GoodsTest {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Test
    public void test1() {

        //System.out.println(goodsService.queryShelfGoods(1));
        for (int i = 453; i < 243 + 453; i++) {
             redisService.setData("shoes_goods_" + (453 + i), "100");
        }
    }
}
