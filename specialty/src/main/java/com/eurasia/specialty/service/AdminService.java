package com.eurasia.specialty.service;

import com.eurasia.specialty.entity.Admin;
import com.eurasia.specialty.repository.AdminRepository;
import com.eurasia.specialty.utils.JpaUtils;
import com.eurasia.specialty.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
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

/**
 * @author Aaron.H.Shen
 * @date 1/6/2021 10:10 AM
 */
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public boolean checkRole(Integer userId) {
        Admin admin = adminRepository.findById(userId).get();
        if (admin == null) {
            return false;
        }
        return StringUtils.equals("admin", admin.getRole()) || StringUtils.equals("经理", admin.getRole());
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

    public PageResult<Admin> queryPage(String role, Integer page, Integer rows) {
        Specification<Admin> specification = new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if(StringUtils.isNotEmpty(role)){
                    list.add(criteriaBuilder.equal(root.get("role"), role));
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
            JpaUtils.copyNotNullProperties(admin, response);
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
}
