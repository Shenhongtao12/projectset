package com.eurasia.food.controller;

import com.alibaba.fastjson.JSONObject;
import com.eurasia.food.entity.Admin;
import com.eurasia.food.entity.User;
import com.eurasia.food.service.AdminService;
import com.eurasia.food.utils.JsonData;
import com.eurasia.food.utils.JwtUtils;
import io.swagger.annotations.Api;
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
@Api(tags = "管理员模块")
public class AdminController extends BaseController {

    @Autowired
    private AdminService adminService;

    /**
     * 仅限超级管理员admin和role为经理的管理员有权限添加普通管理员
     */
    @PostMapping("add")
    public ResponseEntity<JsonData> add(@RequestBody Admin admin) {
        if (!adminService.checkRole(userId)) {
            return ResponseEntity.status(HttpStatus.OK).body(JsonData.buildError("您无权限添加管理员", 401));
        }
        if (!adminService.add(admin)) {
            return ResponseEntity.ok().body(JsonData.buildError("数据异常"));
        }
        return ResponseEntity.ok(JsonData.buildSuccess(""));
    }

    @PostMapping("login")
    public ResponseEntity<JsonData> login(@RequestBody Admin admin) {
        Admin adminInfo = adminService.login(admin);
        if (adminInfo == null) {
            return ResponseEntity.ok().body(JsonData.buildError("用户名或密码错误"));
        }
        JSONObject response = new JSONObject();
        response.put("admin", adminInfo);
        response.put("token", JwtUtils.geneJsonWebToken(new User(adminInfo)));
        return ResponseEntity.ok(JsonData.buildSuccess(response, ""));
    }

    @PutMapping("updatePas")
    public ResponseEntity<JsonData> updatePas(@RequestParam(name = "oldPassword") String oldPassword,
                                                  @RequestParam(name = "password") String password) {
        if (adminService.updatePassword(userId, oldPassword, password)) {
            return ResponseEntity.ok(JsonData.buildSuccess("修改成功，请重新登录"));
        }
        return ResponseEntity.ok(JsonData.buildError("密码错误，修改失败"));
    }

    @GetMapping
    public ResponseEntity<JsonData> queryPage(@RequestParam(name = "role", required = false) String role,
                                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(name = "rows", defaultValue = "10") Integer rows) {
        return ResponseEntity.ok(JsonData.buildSuccess(adminService.queryPage(role, page, rows), ""));
    }

    @PutMapping
    public ResponseEntity<JsonData> update(@RequestBody Admin admin) {
        if (adminService.update(admin)) {
            return ResponseEntity.ok(JsonData.buildSuccess("成功"));
        }
        return ResponseEntity.ok(JsonData.buildError(400,"更新失败"));
    }

    @DeleteMapping
    public ResponseEntity<JsonData> delete(@RequestParam(name = "id") Integer id) {
        if (adminService.delete(id)) {
            return ResponseEntity.ok(JsonData.buildSuccess("成功"));
        }
        return ResponseEntity.ok(JsonData.buildError(400,"删除失败"));
    }

}
