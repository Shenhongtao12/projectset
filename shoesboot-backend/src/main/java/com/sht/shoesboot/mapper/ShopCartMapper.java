package com.sht.shoesboot.mapper;

import com.sht.shoesboot.DTO.ShopCartDTO;
import com.sht.shoesboot.entity.ShopCart;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/27 23:39
 */
public interface ShopCartMapper extends Mapper<ShopCart> {

    /**
     * 连表查询
     * @param userId
     * @return
     */
    @Select("SELECT G.id AS goodsId, G.title, G.images AS image, G.price, C.id AS cartId, C.amount\n" +
            "FROM goods G\n" +
            "INNER JOIN shop_cart C\n" +
            "ON G.id = C.goods_id\n" +
            "WHERE C.user_id = #{userId}"
    )
    List<ShopCartDTO> queryPage(Integer userId);
}
