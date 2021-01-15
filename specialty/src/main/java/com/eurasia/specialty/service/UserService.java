package com.eurasia.specialty.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eurasia.specialty.entity.Post;
import com.eurasia.specialty.repository.UserRepository;
import com.eurasia.specialty.entity.Carousel;
import com.eurasia.specialty.entity.Classify;
import com.eurasia.specialty.entity.User;
import com.eurasia.specialty.exception.AllException;
import com.eurasia.specialty.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hongtao Shen
 * @date 2020/5/16 - 13:48
 **/
@Service
public class UserService {

    @Value("${WX_DATA.appid}")
    protected String APPID;
    @Value("${WX_DATA.secret}")
    protected String SECRET;
    @Value("${WX_DATA.grant_type}")
    protected String GRANT_TYPE;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private MatterService matterService;
    @Autowired
    private PostService postService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    public JSONObject login(User user) {

        return null;
    }

    public Map<String, Object> init() {
        Map<String, Object> map = new HashMap<>(4);

        //轮播图数据
        List<Carousel> carouselList = carouselService.findAll();
        map.put("carouselList", carouselList);

        //分类数据
        List<Classify> classifyList = classifyService.findAll();
        map.put("classifyList", classifyList);
        //热门
        map.put("matterList", matterService.init());
        //帖子
        map.put("post", postService.init());
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
}
