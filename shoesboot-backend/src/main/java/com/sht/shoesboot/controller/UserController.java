package com.sht.shoesboot.controller;

import com.sht.shoesboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 * @date 2020/11/25 20:38
 */
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private UserService userService;
}
