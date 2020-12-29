package com.sht.shoesboot;

import com.sht.shoesboot.utils.OrderNumber;
import org.junit.jupiter.api.Test;

/**
 * @author Aaron
 * @date 2020/12/21 21:57
 */
public class StringTest {

    @Test
    void testTitle() {
        String title = "花花公子马丁靴男秋冬新品高帮沙漠工装靴男靴子户外短靴英伦复古百搭休闲鞋男增高保暖雪地靴棉鞋子男 PL220169沙色 41 花花公子马丁靴男秋冬新品高帮沙漠工装靴男靴子户外短靴英伦复古百搭休闲鞋男增高保暖雪地靴棉鞋子男 PL220169沙色 41";
        String a = title.substring(0, title.length() / 2).trim();
        String b = title.substring(title.length() / 2).trim();
        System.out.println(a);
        System.out.println(b);
        System.out.println(a.equals(b));
    }

    @Test
    public void testOrderNumber() {
        System.out.println(System.currentTimeMillis());
        System.out.println(OrderNumber.createOrderNumber());
    }
}
