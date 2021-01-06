package com.sht.shoesboot.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.shoesboot.entity.Admin;
import com.sht.shoesboot.entity.PageResult;
import com.sht.shoesboot.mapper.AdminMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Aaron.H.Shen
 * @date 1/6/2021 10:10 AM
 */
@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public boolean checkRole(Integer userId) {
        Admin admin = adminMapper.selectByPrimaryKey(userId);
        if (admin == null) {
            return false;
        }
        return StringUtils.equals("admin", admin.getRole()) || StringUtils.equals("经理", admin.getRole());
    }

    public Boolean add(Admin admin) {
        try {
            adminMapper.insertSelective(admin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(Integer userId, String oldPassword, String password) {
        Admin admin = adminMapper.selectByPrimaryKey(userId);
        if (!admin.getPassword().equals(oldPassword)) {
            return false;
        }
        admin.setPassword(password);
        adminMapper.updateByPrimaryKeySelective(admin);
        return true;
    }

    public Admin login(Admin admin) {
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("admin_name", admin.getAdminName());
        criteria.andEqualTo("password", admin.getPassword());
        return adminMapper.selectOneByExample(example);
    }

    public PageResult<Admin> queryPage(String role, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(role)) {
            criteria.andEqualTo("role", role);
        }
        Page<Admin> adminPage = (Page<Admin>) adminMapper.selectByExample(example);
        return new PageResult<>(adminPage.getTotal(), adminPage.getPages(), adminPage.getResult());
    }

    public boolean update(Admin admin) {
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Integer id) {
        try {
            adminMapper.deleteByPrimaryKey(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
