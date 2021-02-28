package com.sht.shoesboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.sht.shoesboot.entity.PageResult;
import com.sht.shoesboot.entity.Register;
import com.sht.shoesboot.entity.User;
import com.sht.shoesboot.service.RedisService;
import com.sht.shoesboot.service.UserService;
import com.sht.shoesboot.utils.JwtUtils;
import com.sht.shoesboot.utils.RestResponse;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Aaron
 * @date 2020/11/25 20:38
 */
@RestController
@RequestMapping("api/user")
@Validated
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @PostMapping("save")
    public ResponseEntity<RestResponse> saveUser(@Valid @RequestBody Register register) {
        String code = redisService.getData("shoes-" + register.getEmail());
        if (code != null) {
            if (code.equals(register.getEmailCode())) {
                userService.save(new User(register));
                redisService.deleteData("shoes-" + register.getEmail());
                return ResponseEntity.ok(SUCCESS("注册成功"));
            } else {
                return ResponseEntity.ok().body(ERROR(400,"邮箱验证码错误"));
            }
        } else {
            return ResponseEntity.ok().body(ERROR(400,"请发送邮箱验证码"));
        }
    }

    /**
     * newUser：1：老用户登录 0：新用户注册
     */
    @GetMapping("emailCode")
    @Valid
    public ResponseEntity<RestResponse> sendEmailCode(@Email(message = "请检查邮箱格式") @RequestParam(name = "email") String email,
                                                      @NotBlank(message = "是否新用户") @RequestParam(name = "newUser") String newUser) {
        if ("1".equals(newUser) || "0".equals(newUser)) {
            return ResponseEntity.ok(userService.sendEmailCode(email, newUser));
        }
        return ResponseEntity.ok().body(ERROR("请求错误"));
    }

    @GetMapping("email-login")
    public ResponseEntity<RestResponse> emailLogin(@Validated @Email @NotBlank(message = "邮箱不能为空") @RequestParam(name = "email") String email,
                                              @RequestParam(name = "code") String code) {
        String redisCode = redisService.getData("shoes-" + email);
        if (!StringUtils.equals(code, redisCode)) {
            return ResponseEntity.ok().body(ERROR(400, "验证码错误"));
        }
        User user = userService.queryUserByEmail(email);
        userService.updateLoginDate(user.getId());
        JSONObject response = new JSONObject();
        response.put("user", user);
        response.put("token", JwtUtils.geneJsonWebToken(user));

        return ResponseEntity.ok(SUCCESS(response, "success"));
    }

    @PostMapping
    public ResponseEntity<RestResponse> login(@Validated @NotBlank(message = "用户名或邮箱不能为空") @RequestParam(name = "username") String username,
                                              @Validated @NotBlank(message = "密码不能为空") @RequestParam(name = "password") String password) {
        JSONObject login = userService.login(username, password);
        if (login == null) {
            return ResponseEntity.ok().body(ERROR(400, "用户名或密码错误"));
        }
        return ResponseEntity.ok(SUCCESS(login, "success"));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@Validated @RequestBody User user) {
        if (user.getId() == null) {
            user.setId(userId);
        }
        user.setPassword(null);
        if (userService.update(user)) {
            return ResponseEntity.ok(SUCCESS("成功"));
        }
        return ResponseEntity.ok().body(ERROR("更新失败"));
    }

    @PutMapping("updatePas")
    public ResponseEntity<RestResponse> updatePas(@RequestParam(name = "oldPassword") String oldPassword,
                                                  @RequestParam(name = "password") String password) {
        if (userService.updatePassword(userId, oldPassword, password)) {
            return ResponseEntity.ok(SUCCESS("修改成功，请重新登录"));
        }
        return ResponseEntity.ok().body(ERROR("修改失败，请重试"));
    }

    @GetMapping("userPage")
    public ResponseEntity<RestResponse> userPage(@RequestParam(name = "name",required = false) String name,
                                                 @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(name = "rows", defaultValue = "10") Integer rows) {
        PageResult<User> userPageResult = userService.userPage(name, page, rows);
        return ResponseEntity.ok(SUCCESS(userPageResult));
    }
}
