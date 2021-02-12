package com.sht.kitchen.service;

import com.alibaba.fastjson.JSONObject;
import com.sht.kitchen.common.RestResponse;
import com.sht.kitchen.entity.User;
import com.sht.kitchen.repository.UserRepository;
import com.sht.kitchen.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/2/12 12:21
 */
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
    private RestTemplate restTemplate;

    public RestResponse login(User user) {
        JSONObject map = new JSONObject();
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + APPID +
                "&secret=" + SECRET +
                "&js_code=" + user.getJs_code() +
                "&grant_type=" + GRANT_TYPE;
        User userInfo = restTemplate.getForEntity(url, User.class).getBody();
        if (userInfo.getErrcode() != null) {
            return new RestResponse(400, "登录失败，错误码为: " + userInfo.getErrcode());
        }
        userInfo.setNickName(user.getNickName());
        userInfo.setAvatarUrl(user.getAvatarUrl());
        //查看该用户是否为老用户
        User byOpenid = userRepository.findUserByOpenid(userInfo.getOpenid());
        if (byOpenid != null) {
            //老用户就更新信息
            userInfo.setId(byOpenid.getId());
        } else {
            //新用户就设置首次登陆时间
            userInfo.setCreateTime(LocalDateTime.now());
        }
        userRepository.save(userInfo);
        user.setId(userInfo.getId());
        String token = JwtUtils.geneJsonWebToken(userInfo);
        map.put("token", token);
        map.put("user", user);
        return new RestResponse(200, map, "");
    }

    public String refreshToken(Integer userId) {

        User user = userRepository.findById(userId).get();
        return JwtUtils.geneJsonWebToken(user);
    }
}
