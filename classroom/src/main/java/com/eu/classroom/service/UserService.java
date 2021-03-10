package com.eu.classroom.service;

import cn.hutool.json.JSONObject;
import com.alibaba.druid.util.StringUtils;
import com.eu.classroom.common.PageResult;
import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.User;
import com.eu.classroom.repository.UserRepository;
import com.eu.classroom.utils.JpaUtils;
import com.eu.classroom.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron
 * @date 2021/2/12 12:21
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String refreshToken(Integer userId) {
        if (userRepository.existsById(userId)) {
            return JwtUtils.geneJsonWebToken(userId, "user");
        }
        return null;
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
            user.setInDate(LocalDateTime.now());
        }
        try {
            User save = userRepository.save(user);
            response.setCode(200);
            response.setMessage("成功");
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("user", save);
            jsonObject.set("token", JwtUtils.geneJsonWebToken(save.getId(), "user"));
            response.setData(jsonObject);
            return response;
        } catch (Exception e) {
            response.setCode(500);
            response.setData(e.getMessage());
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
                jsonObject.set("user", userByUsername);
                jsonObject.set("token", JwtUtils.geneJsonWebToken(userByUsername.getId(), "user"));
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

    public PageResult<User> findByPage(String username, Integer page, Integer size) {
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                List<Predicate> list = new ArrayList<>();
                if (username != null) {
                    list.add(criteriaBuilder.equal(root.get("username"), username));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        Page<User> userPage = userRepository.findAll(spec, PageRequest.of(page, size));
        return new PageResult<>(userPage.getTotalElements(), userPage.getTotalPages(), userPage.getContent());
    }

    @Transactional(rollbackFor = Exception.class)
    public RestResponse batchDelete(List<Integer> ids) {
        //Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint()
        try {
            userRepository.deleteUserByIdIn(ids);
            return new RestResponse(200,null, "删除成功");
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint)
            return new RestResponse(400, e.getMessage(),"删除失败，用户关联有其他数据");
        }
    }

    public User findById(Integer id) {
        return userRepository.findById(id).get();
    }
}
