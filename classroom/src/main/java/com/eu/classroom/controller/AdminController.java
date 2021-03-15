package com.eu.classroom.controller;

import com.alibaba.fastjson.JSONObject;
import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Admin;
import com.eu.classroom.service.AdminService;
import com.eu.classroom.utils.JwtUtils;
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

    @PostMapping("add")
    public ResponseEntity<RestResponse> add(@RequestBody Admin admin) {
        if (!adminService.checkRole(userId)) {
            return ResponseEntity.status(HttpStatus.OK).body(ERROR(400, "您无权限添加管理员"));
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
        JSONObject response = new JSONObject();
        response.put("admin", adminInfo);
        response.put("token", JwtUtils.geneJsonWebToken(adminInfo.getId(), "admin"));
        return ResponseEntity.ok(SUCCESS(response, ""));
    }

    @PutMapping("updatePas")
    public ResponseEntity<RestResponse> updatePas(@RequestParam(name = "oldPassword") String oldPassword,
                                              @RequestParam(name = "password") String password) {
        if (adminService.updatePassword(userId, oldPassword, password)) {
            return ResponseEntity.ok(SUCCESS("修改成功，请重新登录"));
        }
        return ResponseEntity.ok(ERROR("密码错误，修改失败"));
    }

    @GetMapping
    public ResponseEntity<RestResponse> queryPage(@RequestParam(name = "adminName", required = false) String adminName,
                                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                                              @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(SUCCESS(adminService.queryPage(adminName, page, size), ""));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@RequestBody Admin admin) {
        if (adminService.update(admin)) {
            return ResponseEntity.ok(SUCCESS("成功"));
        }
        return ResponseEntity.ok(ERROR(400,"更新失败"));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if (adminService.delete(id)) {
            return ResponseEntity.ok(SUCCESS("成功"));
        }
        return ResponseEntity.ok(ERROR(400,"删除失败"));
    }

    @DeleteMapping("deleteRole")
    public ResponseEntity<RestResponse> deleteRole(@RequestParam(name = "adminId") Integer adminId,
                                                   @RequestParam(name = "roleId") Integer roleId) {

        return ResponseEntity.ok(adminService.deleteRole(adminId, roleId));
    }

}
