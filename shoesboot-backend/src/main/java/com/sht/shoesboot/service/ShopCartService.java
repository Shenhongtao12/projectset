package com.sht.shoesboot.service;

import com.sht.shoesboot.entity.ShopCart;
import com.sht.shoesboot.mapper.ShopCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aaron
 * @date 2020/12/27 23:40
 */
@Service
public class ShopCartService {

    @Autowired
    private ShopCartMapper shopCartMapper;

    public void update(ShopCart shopCart) {
        shopCartMapper.updateByPrimaryKeySelective(shopCart);
    }
}
