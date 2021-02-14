package com.sht.vehicle.controller;

import com.sht.vehicle.common.RestResponse;
import com.sht.vehicle.entity.User;
import com.sht.vehicle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/2/12 12:37
 */
@RestController
@RequestMapping("api/user")
@Validated
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<RestResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(userService.login(user));
    }

    @PostMapping("register")
    public ResponseEntity<RestResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("refresh-token")
    public ResponseEntity<RestResponse> refreshToken() {
        return ResponseEntity.ok(SUCCESS(userService.refreshToken(userId), ""));
    }
}
