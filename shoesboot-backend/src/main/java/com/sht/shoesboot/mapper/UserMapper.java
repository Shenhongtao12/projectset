package com.sht.shoesboot.mapper;

import com.sht.shoesboot.entity.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Aaron
 * @date 2020/11/25 20:40
 */
public interface UserMapper extends Mapper<User> {
    @Select("select count(1) from user LIMIT 1")
    Integer countUser();
}
