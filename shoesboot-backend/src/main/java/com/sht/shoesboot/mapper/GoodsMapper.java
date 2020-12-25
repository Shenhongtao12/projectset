package com.sht.shoesboot.mapper;

import com.sht.shoesboot.entity.Goods;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/19 22:48
 */
public interface GoodsMapper extends Mapper<Goods> {

    List<Goods> queryInId();
}
