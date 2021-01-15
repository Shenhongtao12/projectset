package com.eurasia.specialty.service;

import com.eurasia.specialty.entity.Admin;
import com.eurasia.specialty.repository.AdminRepository;
import com.eurasia.specialty.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("admin_name", admin.getAdminName());
        criteria.andEqualTo("password", admin.getPassword());
        return adminRepository.selectOneByExample(example);
    }

    public PageResult<Admin> queryPage(String role, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(role)) {
            criteria.andEqualTo("role", role);
        }
        Page<Admin> adminPage = (Page<Admin>) adminRepository.selectByExample(example);
        return new PageResult<>(adminPage.getTotal(), adminPage.getPages(), adminPage.getResult());
    }

    public boolean update(Admin admin) {
        try {
            adminRepository.updateByPrimaryKeySelective(admin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Integer id) {
        try {
            adminRepository.deleteByPrimaryKey(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
