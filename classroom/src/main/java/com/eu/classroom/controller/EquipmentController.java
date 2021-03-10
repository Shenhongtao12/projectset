package com.eu.classroom.controller;

import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Equipment;
import com.eu.classroom.service.EquipmentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 * @date 2021/3/10 22:57
 */
@RequestMapping("api/equipment")
@RestController
@Api
public class EquipmentController extends BaseController {

    @Autowired
    private EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<RestResponse> saveOrUpdate(@RequestBody Equipment equipment) {
        return ResponseEntity.ok(equipmentService.saveOrUpdate(equipment));
    }
}
