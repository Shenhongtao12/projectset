package com.sht.vehicle.controller;

import com.alibaba.fastjson.JSONObject;
import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.Admin;
import com.sht.vehicle.entity.User;
import com.sht.vehicle.service.SchedulingService;
import com.sht.vehicle.utils.JwtUtils;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/2/20 16:59
 */
@RestController
@RequestMapping("api/admin")
public class AdminController extends BaseController{

    @Autowired
    private SchedulingService schedulingService;

    @PostMapping("login")
    public ResponseEntity<RestResponse> login(@RequestBody Admin admin) {
        if (!StringUtils.equals(admin.getUsername(), "admin") || !"admin".equals(admin.getPassword())) {
            return ResponseEntity.ok(ERROR("用户名或密码错误"));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("admin", admin);
        User user = new User();
        user.setId(999999);
        jsonObject.put("token", JwtUtils.geneJsonWebToken(user));
        return ResponseEntity.ok(SUCCESS(jsonObject));
    }

    @PutMapping
    public ResponseEntity<RestResponse> approval(@RequestParam(name = "id") Integer id,
                                                 @ApiParam(value = "1通过，2拒绝") @RequestParam(name = "operation") String operation) {
        return ResponseEntity.ok(schedulingService.approval(id, operation));
    }
}
