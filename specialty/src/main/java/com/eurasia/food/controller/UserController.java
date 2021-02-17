package com.eurasia.food.controller;

import com.eurasia.food.entity.User;
import com.eurasia.food.service.UserService;
import com.eurasia.food.utils.JsonData;
import com.eurasia.food.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aaron
 * @date 2020/5/16 - 13:48
 **/
@RestController
@RequestMapping("api/user")
@Api(tags = "用户服务")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    @PostMapping("login")
    @ApiOperation(value = "登陆", notes = "该接口不需要身份令牌")
    public ResponseEntity<JsonData> login(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(user));
    }

    @PostMapping
    @ApiOperation(value = "注册或更新", notes = "注册或者更新都是同一个接口，区别: 更新需要传递id")
    public ResponseEntity<JsonData> save(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("init")
    @ApiOperation(value = "初始化首页面", notes = "该接口不需要身份令牌")
    public ResponseEntity<JsonData> init(){
        return ResponseEntity.ok(JsonData.buildSuccess(userService.init(), "初始化数据"));
    }

    @GetMapping()
    public ResponseEntity<JsonData> getMessage(){
        return ResponseEntity.ok(userService.getMessage(userId));
    }

    @GetMapping("{id}")
    public ResponseEntity<JsonData> findById(@PathVariable Integer id){
        Map<String, Object> fans = new HashMap<>();
        fans.put("user", userService.findUserById(id));
        return ResponseEntity.ok(JsonData.buildSuccess(fans, ""));
    }

    @GetMapping("getToken")
    @ApiOperation(value = "获取一个token，用于测试", notes = "该接口不需要身份令牌")
    public ResponseEntity<String> getToken(@ApiParam("用户id") @RequestParam(name = "id") Integer id){
        User user = new User();
        user.setId(id);
        user.setNickName("Binary");
        String token ="Bearer " + JwtUtils.geneJsonWebToken(user);
        return ResponseEntity.ok(token);
    }

    @GetMapping("queryPage")
    public ResponseEntity<JsonData> findByPage(
                                                        @RequestParam(value = "name", required = false) String name,
                                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                        @RequestParam(value = "rows", defaultValue = "20") Integer rows) {
        return ResponseEntity.ok(JsonData.buildSuccess(this.userService.findByPage( name, page, rows),""));
    }
}
