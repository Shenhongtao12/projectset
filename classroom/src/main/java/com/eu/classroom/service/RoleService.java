package com.eu.classroom.service;

import com.alibaba.druid.sql.visitor.functions.If;
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
            if (roleRepository.checkNode(role.getNode(), role.getId()) != null) {
                response.setCode(400);
                response.setMessage("已存在该node结点: " + role.getNode());
                return response;
            }
            Role one = roleRepository.getOne(role.getId());
            JpaUtils.copyNotNullProperties(role, one);
        } else {
            if (roleRepository.existsRoleByNode(role.getNode())) {
                response.setCode(400);
                response.setMessage("已存在该node结点: " + role.getNode());
                return response;
            }
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

    public Role findById(Integer id) {
        return roleRepository.findById(id).get();
    }

    public Boolean exists(Integer id) {
        return roleRepository.existsById(id);
    }

    public RestResponse delete(Integer id) {
        if (exists(id)){
            roleRepository.deleteById(id);
            return new RestResponse(200, "删除成功");
        }
        return new RestResponse(400, "不存在该id");
    }
}
