package com.sht.shoesboot.mapper;

import com.sht.shoesboot.entity.Admin;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Aaron
 * @date 2021/1/6 20:43
 */
public interface AdminMapper extends Mapper<Admin> {

    @Select("select count(1) from admin LIMIT 1")
    Integer countAdmin();
}
