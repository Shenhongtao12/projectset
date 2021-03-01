package com.sht.shoesboot.mapper;

import com.sht.shoesboot.entity.CheapGoods;
import com.sht.shoesboot.entity.Goods;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Aaron
 * @date 2021/1/29 22:02
 */
public interface CheapGoodsMapper  extends Mapper<CheapGoods> {
    /**
     * 特价商品
     * @param status 状态
     * @return 结果
     */
    @Select("SELECT A.id, A.title, A.price AS originalPrice, A.inventory, A.sales_volume AS salesVolume, A.images, A.brand, A.shelf, B.price \n" +
            "FROM goods A\n" +
            "INNER JOIN cheap_goods B\n" +
            "ON A.id = B.goods_id\n" +
            "WHERE B.status = #{status}"
    )
    List<Goods> queryPage(Boolean status);
}
