package com.sht.qrcode.mapper;

import com.sht.qrcode.entity.QrEntity;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Aaron
 * @date 2021/3/8 22:43
 */
public interface QrMapper extends Mapper<QrEntity> {

    /**
     * 查询IP地址
     * @return ip
     */
    @Select("select ip from config")
    String findIp();
}
