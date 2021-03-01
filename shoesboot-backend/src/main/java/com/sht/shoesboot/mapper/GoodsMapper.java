package com.sht.shoesboot.mapper;

import com.sht.shoesboot.entity.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/19 22:48
 */
public interface GoodsMapper extends Mapper<Goods> {

    /**
     * 根据用户收藏查询下架或删除的商品
     * @param userId
     * @return
     */
    List<Goods> queryShelfGoods(@Param("userId")Integer userId);

    /**
     * 根据用户收藏查询下架或删除的商品
     * @return
     */
    List<Goods> queryShelfGoodsTest();

    /**
     * create table
     */
    void createTable();

    /**
     * insert tmp table
     */
    void insertTmp(String ids);

    /**
     * 更新库存
     * @param goodsId
     * @param amount
     */
    @Update("UPDATE goods SET inventory = (inventory - #{amount}), sales_volume = (sales_volume + #{amount}) WHERE id = #{goodsId}")
    void updateInventory(Integer goodsId, Integer amount);
}
