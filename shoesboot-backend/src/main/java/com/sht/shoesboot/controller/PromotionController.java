package com.sht.shoesboot.controller;

import com.sht.shoesboot.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 * @date 2021/1/5 20:07
 */
@RestController
@RequestMapping("api/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;
}
