package com.sht.shoesboot;

import com.sht.shoesboot.utils.JwtUtils;
import com.sht.shoesboot.utils.OrderNumber;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

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
        System.out.println(Math.toIntExact(26L) / 10);
    }

    @Test
    public void testToken() {
        Claims claims = JwtUtils.checkJWT("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaHREZWFsIiwiaWQiOjEsImlhdCI6MTYxNDQ5NDAyNywiZXhwIjoxNjE1MDk4ODI3fQ.HritrE6ek02-4hFexcXcA0gsw_06iNTYe68SuxZmYDs");
        System.out.println(claims.get("id"));
    }

    @Test
    public void decode() {
        System.out.println(new String(Base64Utils.decode("MTIzNA==".getBytes())));
    }
}
