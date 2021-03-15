package com.sht.shoesboot.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sht.shoesboot.entity.PageResult;
import com.sht.shoesboot.entity.User;
import com.sht.shoesboot.enums.UrlEnum;
import com.sht.shoesboot.mapper.UserMapper;
import com.sht.shoesboot.utils.JwtUtils;
import com.sht.shoesboot.utils.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aaron
 * @date 2020/11/25 20:39
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;

    public RestResponse sendEmailCode(String email, String newUser) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("email", email);
        User user = userMapper.selectOneByExample(example);
        if ("1".equals(newUser) && user == null){
            return new RestResponse(400, "请先注册");
        }else if ("0".equals(newUser) && user != null){
            return new RestResponse(400, "该邮箱已注册");
        }else {
            JSONObject body = new JSONObject();
            body.put("fromName", "鞋靴专卖旗舰店");
            body.put("email", email);
            body.put("subject", "空军一号");
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(UrlEnum.EMAIL.getUrl(), body, String.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK){
                redisService.setEmailCode("shoes-" + email, JSONObject.parseObject(responseEntity.getBody()).get("message").toString());
                return new RestResponse(200, "发送成功");
            }else {
                return new RestResponse(400, "发送失败，请重试");
            }
        }
    }

    public void save(User user) {
        user.setInDate(LocalDateTime.now());
        userMapper.insertSelective(user);
    }

    public JSONObject login(String username, String password) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.orEqualTo("username", username);
        criteria.orEqualTo("email", username);
        User user = userMapper.selectOneByExample(example);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }else {
            updateLoginDate(user.getId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", JwtUtils.geneJsonWebToken(user));
            jsonObject.put("user", user);
            return jsonObject;
        }

    }

    public void updateLoginDate(Integer id) {
        User user = new User();
        user.setId(id);
        user.setLastLoginDate(LocalDateTime.now());
        userMapper.updateByPrimaryKeySelective(user);
    }

    public User queryUserByEmail(String email) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.orEqualTo("email", email);
        return userMapper.selectOneByExample(example);
    }

    public Boolean update(User user) {
        return userMapper.updateByPrimaryKeySelective(user) == 1;
    }

    public Boolean updatePassword(Integer id, String oldPas, String password) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user != null && user.getPassword().equals(oldPas)) {
            user.setPassword(password);
            return update(user);
        }else {
            return false;
        }
    }

    public PageResult<User> userPage(String  name, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(name)) {
            criteria.andEqualTo("username", name);
        }
        Page<User> users = (Page<User>) userMapper.selectByExample(example);
        return new PageResult<>(users.getTotal(), users.getPages(), users.getResult());
    }

    public Integer count() {
        return userMapper.countUser();
    }
}
