package com.sht.shoesboot.controller;

import com.sht.shoesboot.entity.User;
import com.sht.shoesboot.service.UserService;
import com.sht.shoesboot.utils.RestResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 * @date 2020/11/25 20:38
 */
@RestController
@RequestMapping("api/user")
@Api
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping("save")
    public ResponseEntity<RestResponse> saveUser(@RequestBody User user) {
        return ResponseEntity.ok(SUCCESS("success"));
    }
}
