package com.sht.shoesboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.sht.shoesboot.entity.Admin;
import com.sht.shoesboot.entity.User;
import com.sht.shoesboot.service.AdminService;
import com.sht.shoesboot.utils.JwtUtils;
import com.sht.shoesboot.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron.H.Shen
 * @date 1/6/2021 10:10 AM
 */
@RestController
@RequestMapping("api/admin")
public class AdminController extends BaseController {

    @Autowired
    private AdminService adminService;

    /**
     * 仅限超级管理员admin和role为经理的管理员有权限添加普通管理员
     */
    @PostMapping("add")
    public ResponseEntity<RestResponse> add(@RequestBody Admin admin) {

        if (!adminService.checkRole(userId)) {
            return ResponseEntity.status(HttpStatus.OK).body(ERROR("您无权限添加管理员"));
        }
        if (!adminService.add(admin)) {
            return ResponseEntity.ok().body(ERROR("数据异常"));
        }
        return ResponseEntity.ok(SUCCESS(""));
    }

    @PostMapping("login")
    public ResponseEntity<RestResponse> login(@RequestBody Admin admin) {
        Admin adminInfo = adminService.login(admin);
        if (adminInfo == null) {
            return ResponseEntity.ok().body(ERROR("用户名或密码错误"));
        }
        //更新上次登录时间
        adminService.updateLastDate(adminInfo.getId());
        JSONObject response = new JSONObject();
        response.put("admin", adminInfo);
        response.put("token", JwtUtils.geneJsonWebToken(new User(adminInfo)));
        return ResponseEntity.ok(SUCCESS(response));
    }

    @PutMapping("updatePas")
    public ResponseEntity<RestResponse> updatePas(@RequestParam(name = "oldPassword") String oldPassword,
                                                  @RequestParam(name = "password") String password) {
        if (adminService.updatePassword(userId, oldPassword, password)) {
            return ResponseEntity.ok(SUCCESS("修改成功，请重新登录"));
        }
        return ResponseEntity.ok().body(ERROR("密码错误，修改失败"));
    }

    @GetMapping
    public ResponseEntity<RestResponse> queryPage(@RequestParam(name = "role", required = false) String role,
                                                  @RequestParam(name = "adminName", required = false) String adminName,
                                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(name = "rows", defaultValue = "10") Integer rows) {
        return ResponseEntity.ok(SUCCESS(adminService.queryPage(role, adminName, page, rows)));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@RequestBody Admin admin) {
        //admin.setPassword(null);
        if (adminService.update(admin)) {
            return ResponseEntity.ok(SUCCESS("成功"));
        }
        return ResponseEntity.ok().body(ERROR(400,"更新失败"));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if (adminService.delete(id)) {
            return ResponseEntity.ok(SUCCESS("成功"));
        }
        return ResponseEntity.ok().body(ERROR(400,"删除失败"));
    }

}
