package com.eu.classroom.service;

import cn.hutool.core.util.StrUtil;
import com.eu.classroom.common.PageResult;
import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Admin;
import com.eu.classroom.entity.Role;
import com.eu.classroom.entity.User;
import com.eu.classroom.repository.AdminRepository;
import com.eu.classroom.utils.JpaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Aaron.H.Shen
 * @date 1/6/2021 10:10 AM
 */
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleService roleService;

    public boolean checkRole(Integer userId) {
        return adminRepository.existsById(userId);
    }

    public Boolean add(Admin admin) {
        try {
            adminRepository.save(admin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(Integer userId, String oldPassword, String password) {
        Admin admin = adminRepository.findById(userId).get();
        if (!admin.getPassword().equals(oldPassword)) {
            return false;
        }
        admin.setPassword(password);
        adminRepository.save(admin);
        return true;
    }

    public Admin login(Admin admin) {
        return adminRepository.findAdminByAdminNameAndPassword(admin.getAdminName(), admin.getPassword());
    }

    public PageResult<Admin> queryPage(String adminName, Integer page, Integer rows) {
        Specification<Admin> specification = new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(StrUtil.isNotEmpty(adminName)){
                    list.add(criteriaBuilder.equal(root.get("adminName"), adminName));
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        Page<Admin> adminPage = adminRepository.findAll(specification, PageRequest.of(page, rows));
        return new PageResult<>(adminPage.getTotalElements(), adminPage.getTotalPages(), adminPage.getContent());
    }

    public boolean update(Admin admin) {
        try {
            Admin response = adminRepository.findById(admin.getId()).get();
            admin.setPassword(response.getPassword());
            JpaUtils.copyNotNullProperties(admin, response);
            /*if (!admin.getRoles().isEmpty()){
                Set<Role> roles = response.getRoles();
                roles.addAll(admin.getRoles());
                admin.setRoles(roles);
            }*/
            adminRepository.save(admin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Integer id) {
        try {
            adminRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public RestResponse deleteRole(Integer adminId, Integer roleId) {
        Admin admin = adminRepository.findById(adminId).get();
        admin.getRoles().remove(roleService.findById(roleId));

        try {
            adminRepository.save(admin);
            return new RestResponse(200, "成功");
        }catch (Exception e) {
            return new RestResponse(400, "删除失败，请求检查数据是否有误");
        }
    }
}
