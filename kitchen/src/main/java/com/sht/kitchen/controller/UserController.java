package com.sht.kitchen.controller;

import com.sht.kitchen.common.RestResponse;
import com.sht.kitchen.entity.User;
import com.sht.kitchen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/2/12 12:37
 */
@RestController
@RequestMapping("api/user")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<RestResponse> login(@RequestBody User user) {
        return ResponseEntity.ok(userService.login(user));
    }

    @GetMapping("refresh-token")
    public ResponseEntity<RestResponse> refreshToken() {
        return ResponseEntity.ok(SUCCESS(userService.refreshToken(userId), ""));
    }
}
