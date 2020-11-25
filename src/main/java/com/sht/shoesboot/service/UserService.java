package com.sht.shoesboot.service;

import com.sht.shoesboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aaron
 * @date 2020/11/25 20:39
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
}
