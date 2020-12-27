package com.sht.shoesboot.mapper;

import com.sht.shoesboot.entity.Favorite;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/24 23:19
 */
public interface FavoriteMapper extends Mapper<Favorite> {
    /**
     * 批量删除
     * @param ids
     */
    void batchDelete(List<Integer> ids);
}
