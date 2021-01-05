package com.sht.shoesboot.mapper;

import com.sht.shoesboot.DTO.OrderDTO;
import com.sht.shoesboot.entity.Order;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/29 22:54
 */
public interface OrderMapper extends Mapper<Order> {

    /**
     * 更新库存
     * @param goodsId
     * @param amount
     */
    @Update("UPDATE goods SET inventory = (inventory - #{amount}) WHERE id = #{goodsId}")
    void updateInventory(Integer goodsId, Integer amount);

    /**
     * 动态查询订单
     * @param status
     * @param userId
     * @return
     */
    @Select("<script> " +
            "SELECT g.id AS goodsId,g.title,g.images,g.price, o.id AS orderId,o.orderNumber,o.status,o.amount,o.money,o.in_date AS inDate\n" +
            "FROM order as o INNER JOIN goods g\n" +
            "ON o.goods_id = g.id\n" +
            " <where> " +
            " <if status=\"status != null\"> status=#{status} </if> " +
            " <if status=\"userId != null\"> AND o.user_id = #{userId} </if>" +
            " </where> " +
            " </script> ")
    List<OrderDTO> queryPage(String status, Integer userId);
}
