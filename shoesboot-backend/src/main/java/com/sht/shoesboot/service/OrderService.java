package com.sht.shoesboot.service;

import com.sht.shoesboot.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aaron
 * @date 2020/12/29 22:53
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
}
