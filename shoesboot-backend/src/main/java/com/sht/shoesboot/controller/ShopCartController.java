package com.sht.shoesboot.controller;

import com.sht.shoesboot.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 * @date 2020/12/27 23:39
 */
@RestController
@RequestMapping("api/shopCart")
public class ShopCartController {

    @Autowired
    private ShopCartService shopCartService;
}
