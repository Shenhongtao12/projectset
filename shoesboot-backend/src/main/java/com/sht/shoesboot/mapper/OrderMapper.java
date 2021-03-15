package com.sht.shoesboot.mapper;

import com.sht.shoesboot.DTO.OrderDTO;
import com.sht.shoesboot.entity.Order;
import com.sht.shoesboot.entity.OrderGoods;
import org.apache.ibatis.annotations.*;
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
    List<Order> queryPage(String status, Integer user_id, Integer id);

    /**
     * 批量插入
     * @param orderGoodsList
     */
    void batchInsert(@Param("orderGoodsList") List<OrderGoods> orderGoodsList);

    @Select("select count(1) from orders LIMIT 1")
    Integer countOrder();
}
