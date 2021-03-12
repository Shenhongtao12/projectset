package com.eu.classroom.controller;

import com.eu.classroom.common.RestResponse;
import com.eu.classroom.dto.UserDto;
import com.eu.classroom.entity.User;
import com.eu.classroom.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aaron
 * @date 2021/3/10 20:57
 */
@RestController
@RequestMapping("api/user")
@Api(tags = "用户管理")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<RestResponse> login(@RequestBody UserDto user) {
        User user1 = new User();
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
        return ResponseEntity.ok(userService.login(user1));
    }

    @PostMapping
    public ResponseEntity<RestResponse> saveOrUpdate(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("refresh-token")
    public ResponseEntity<RestResponse> refreshToken() {
        String token = userService.refreshToken(userId);
        if (token == null) {
            return ResponseEntity.ok(ERROR("身份校验失败"));
        }
        return ResponseEntity.ok(SUCCESS(token, ""));
    }

    @GetMapping
    public ResponseEntity<RestResponse> findByPage(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(SUCCESS(userService.findByPage(username, page, size), ""));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> batchDelete(@RequestParam(name = "ids") List<Integer> ids) {
        return ResponseEntity.ok(userService.batchDelete(ids));
    }

}
