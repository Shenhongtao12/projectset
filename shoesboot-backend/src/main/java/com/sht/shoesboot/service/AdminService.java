package com.sht.shoesboot.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.shoesboot.entity.Admin;
import com.sht.shoesboot.entity.PageResult;
import com.sht.shoesboot.mapper.AdminMapper;
import com.sht.shoesboot.utils.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;

/**
 * @author Aaron.H.Shen
 * @date 1/6/2021 10:10 AM
 */
@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

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

    public void updateLastDate(Integer id) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setLastLoginDate(LocalDateTime.now());
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    public Admin login(Admin admin) {
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("adminName", admin.getAdminName());
        criteria.andEqualTo("password", admin.getPassword());
        return adminMapper.selectOneByExample(example);
    }

    public PageResult<Admin> queryPage(String role, String adminName, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(role)) {
            criteria.andEqualTo("role", role);
        }
        if (StringUtils.isNotEmpty(adminName)) {
            criteria.andEqualTo("adminName", adminName);
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


    public RestResponse info() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", userService.count());
        jsonObject.put("admin", adminMapper.countAdmin());
        jsonObject.put("order", orderService.countOrder());
        jsonObject.put("product", goodsService.countGoods());
        return new RestResponse(200, jsonObject, "");
    }
}
