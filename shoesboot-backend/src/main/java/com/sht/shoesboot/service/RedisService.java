package com.sht.shoesboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Aaron
 * @date 2020/12/15 21:51
 */
@Service
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setEmailCode(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES);
    }

    public void setData(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Boolean deleteData(String key) {
        return stringRedisTemplate.delete(key);
    }
}
