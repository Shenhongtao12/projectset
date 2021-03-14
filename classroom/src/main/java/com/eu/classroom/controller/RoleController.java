package com.eu.classroom.controller;

import com.eu.classroom.common.RestResponse;
import com.eu.classroom.dto.RoleDto;
import com.eu.classroom.entity.Role;
import com.eu.classroom.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/3/12 23:03
 */
@RestController
@RequestMapping("api/role")
@Api(tags = "权限")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    private ResponseEntity<RestResponse> saveOrUpdate(@RequestBody RoleDto role) {
        return ResponseEntity.ok(roleService.saveOrUpdate(new Role(role)));
    }

    @GetMapping
    public ResponseEntity<RestResponse> findAll() {
        return ResponseEntity.ok(SUCCESS(roleService.findAll()));
    }
}
