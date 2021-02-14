package com.sht.vehicle.service;

import com.alibaba.fastjson.JSONObject;
import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.User;
import com.sht.vehicle.repository.UserRepository;
import com.sht.vehicle.utils.JpaUtils;
import com.sht.vehicle.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Aaron
 * @date 2021/2/12 12:21
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String refreshToken(Integer userId) {
        User user = userRepository.findById(userId).get();
        return JwtUtils.geneJsonWebToken(user);
    }

    public RestResponse save(User user) {
        RestResponse response = new RestResponse();
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            User one = userRepository.getOne(user.getId());
            JpaUtils.copyNotNullProperties(user, one);
        } else {
            if (userRepository.existsUserByUsername(user.getUsername())) {
                response.setMessage("用户名已存在");
                response.setCode(400);
                return response;
            }
            user.setCreateTime(LocalDateTime.now());
        }
        try {
            User save = userRepository.save(user);
            response.setCode(200);
            response.setMessage("成功");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", save);
            jsonObject.put("token", JwtUtils.geneJsonWebToken(save));
            response.setData(jsonObject);
            return response;
        } catch (Exception e) {
            response.setCode(500);
            response.setMessage("服务异常，请稍后重试");
            return response;
        }
    }

    public RestResponse login(User user) {
        RestResponse response = new RestResponse();
        if (userRepository.existsUserByUsername(user.getUsername())){
            User userByUsername = userRepository.findUserByUsername(user.getUsername());
            if (StringUtils.equals(user.getPassword(), userByUsername.getPassword())) {
                response.setCode(200);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user", userByUsername);
                jsonObject.put("token", JwtUtils.geneJsonWebToken(userByUsername));
                response.setData(jsonObject);
                return response;
            }else {
                response.setMessage("密码错误");
                response.setCode(400);
                return response;
            }
        }
        response.setMessage("用户名不存在");
        response.setCode(400);
        return response;
    }
}
