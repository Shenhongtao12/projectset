package com.sht.shoesboot.service;

import com.sht.shoesboot.mapper.PromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aaron
 * @date 2021/1/5 20:07
 */
@Service
public class PromotionService {

    @Autowired
    private PromotionMapper promotionMapper;
}
