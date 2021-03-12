package com.eu.classroom.service;

import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Equipment;
import com.eu.classroom.entity.Role;
import com.eu.classroom.repository.RoleRepository;
import com.eu.classroom.utils.JpaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aaron
 * @date 2021/3/12 23:02
 */
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public RestResponse saveOrUpdate(Role role) {
        RestResponse response = new RestResponse();
        if (role.getId() != null && roleRepository.existsById(role.getId())) {
            Role one = roleRepository.getOne(role.getId());
            JpaUtils.copyNotNullProperties(role, one);
        }
        try {
            Role save = roleRepository.save(role);
            response.setCode(200);
            response.setMessage("成功");
            response.setData(save);
            return response;
        } catch (Exception e) {
            response.setCode(500);
            response.setData(e.getMessage());
            response.setMessage("服务异常，请稍后重试");
            return response;
        }
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
