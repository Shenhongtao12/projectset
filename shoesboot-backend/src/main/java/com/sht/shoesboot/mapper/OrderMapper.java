package com.sht.shoesboot.mapper;

import com.sht.shoesboot.DTO.OrderDTO;
import com.sht.shoesboot.entity.Order;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
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
     * @param status 状态
     * @param user_id user id
     * @return list
     */
    /*@Select("<script> " +
            "SELECT g.id AS goodsId,g.title AS title,g.images AS images,g.price AS price, o.id AS orderId,o.order_number AS orderNumber,o.status AS status,o.amount AS amount,o.money AS money,o.in_date AS inDate\n" +
            "FROM `order` o INNER JOIN goods g\n" +
            "ON o.goods_id = g.id\n" +
            " <where> " +
            " <if test=\"status != '0'\"> status = #{status} </if> \n" +
            " <if test=\"user_id != 0\"> AND o.user_id = #{user_id} </if>\n" +
            " </where> \n" +
            " </script> ")*/

    List<OrderDTO> queryPage(String status, Integer user_id);
}
