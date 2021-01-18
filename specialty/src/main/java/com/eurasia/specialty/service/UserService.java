package com.eurasia.specialty.service;

import com.alibaba.fastjson.JSONObject;
import com.eurasia.specialty.repository.UserRepository;
import com.eurasia.specialty.entity.Carousel;
import com.eurasia.specialty.entity.Classify;
import com.eurasia.specialty.entity.User;
import com.eurasia.specialty.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aaron
 * @date 2020/5/16 - 13:48
 **/
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    public JsonData login(User user) {
        User info = userRepository.findUserByNickName(user.getNickName());
        JsonData data = new JsonData();

        if (info == null || !StringUtils.equals(user.getPassword(), info.getPassword())) {
            data.setCode(400);
            data.setMsg("用户名或密码错误");
        } else {
            data.setCode(200);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", user);
            jsonObject.put("token", JwtUtils.geneJsonWebToken(user));
            data.setData(jsonObject);
        }
        return data;
    }

    public Map<String, Object> init() {
        Map<String, Object> map = new HashMap<>(4);

        //轮播图数据
        List<Carousel> carouselList = carouselService.findAll();
        map.put("carouselList", carouselList);

        //分类数据
        List<Classify> classifyList = classifyService.findAll();
        map.put("classifyList", classifyList);
        //土特产
        map.put("goods", goodsService.findByPage(null, null, "id", "1", null, 0, 20));
        return map;
    }

    public JsonData getMessage(Integer userId) {

        //获取回复数量
        String reply = redisTemplate.boundValueOps("eurasia_" + userId).get();
        int comNum = reply == null ? 0 : Integer.parseInt(reply);

        //获取关注的数量
        Long fans = redisTemplate.opsForSet().size("eu_fans-" + userId);
        int fansNum = fans == null ? 0 : fans.intValue();

        //获取点赞数量
        String praise = redisTemplate.boundValueOps("eu_praise_" + userId).get();
        int praiseNum = praise == null ? 0 : Integer.parseInt(praise);
        Map<String, Integer> map = new HashMap<>();
        map.put("comment", comNum);
        map.put("fans", fansNum);
        map.put("praise", praiseNum);
        return JsonData.buildSuccess(map, "");
    }

    public User findUserById(Integer id) {
        return userRepository.findById(id).get();
    }

    public JsonData save(User user) {
        JsonData data = new JsonData();
        try {
            if (user.getId() != null) {
                User one = userRepository.getOne(user.getId());
                JpaUtils.copyNotNullProperties(user, one);
            }else {
                user.setCreateTime(new Date());
            }
            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            data.setCode(502);
            data.setMsg("服务异常，请稍后重试");
            return data;
        }
        data.setCode(200);
        data.setMsg("成功");
        return data;
    }
}
