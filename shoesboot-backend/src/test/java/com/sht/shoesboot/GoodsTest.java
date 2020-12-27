package com.sht.shoesboot;

import com.sht.shoesboot.service.GoodsService;
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

    @Test
    public void test1() {

        System.out.println(goodsService.queryShelfGoods(1));
    }
}
