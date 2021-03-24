package com.eurasia.specialty.service;

import com.alibaba.fastjson.JSONObject;
import com.eurasia.specialty.entity.Goods;
import com.eurasia.specialty.repository.UserRepository;
import com.eurasia.specialty.entity.Carousel;
import com.eurasia.specialty.entity.Classify;
import com.eurasia.specialty.entity.User;
import com.eurasia.specialty.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

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

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;

    public JsonData login(User user) {
        User info = userRepository.findUserByNickName(user.getNickName());
        JsonData data = new JsonData();

        if (info == null || !StringUtils.equals(user.getPassword(), info.getPassword())) {
            data.setCode(400);
            data.setMsg("用户名或密码错误");
        } else {
            data.setCode(200);
            JSONObject jsonObject = new JSONObject();
            info.setPassword("******");
            jsonObject.put("user", info);
            jsonObject.put("token", JwtUtils.geneJsonWebToken(info));
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
                user.setPassword(null);
                JpaUtils.copyNotNullProperties(user, one);
            }else {
                User info = userRepository.findUserByNickName(user.getNickName());
                if (info != null) {
                    data.setMsg("用户名已存在");
                    data.setCode(400);
                    return data;
                }
                user.setCreateTime(new Date());
            }
            User save = userRepository.save(user);
            Map<String, Object> jsonObject = new HashMap<>(4);
            jsonObject.put("user", save);
            jsonObject.put("token", JwtUtils.geneJsonWebToken(save));
            data.setCode(200);
            data.setMsg("成功");
            data.setData(jsonObject);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            data.setCode(500);
            data.setMsg("服务异常，请稍后重试");
            return data;
        }
    }

    public PageResult<User> findByPage(String name, Integer page, Integer rows) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (name != null) {
                    list.add(criteriaBuilder.like(root.get("goodsName"),"%" + name + "%"));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<User> userPage = userRepository.findAll(specification, PageRequest.of(page, rows, Sort.by(Sort.Direction.ASC, "id")));
        return new PageResult<>(userPage.getTotalElements(), userPage.getTotalPages(), userPage.getContent());
    }

    public JsonData delete(Integer id) {
        if (!userRepository.existsById(id)) {
            return JsonData.buildError("不存在该用户");
        }
        if (postService.existsByUser(id)) {
            return JsonData.buildError("该用户存在帖子数据，删除失败");
        }
        if (commentService.existsByUser(id)) {
            return JsonData.buildError("该用户存在留言数据，删除失败");
        }
        if (replyService.existsByUser(id)) {
            return JsonData.buildError("该用户存在回复数据，删除失败");
        }

        userRepository.deleteById(id);
        return JsonData.buildSuccess("删除成功");
    }
}
