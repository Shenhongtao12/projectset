package com.eu.classroom.controller;

import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Laboratory;
import com.eu.classroom.service.LaboratoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/3/11 22:48
 */
@RestController
@RequestMapping("api/laboratory")
@Api(tags = "实验室")
public class LaboratoryController extends BaseController {

    @Autowired
    private LaboratoryService laboratoryService;

    @PostMapping
    public ResponseEntity<RestResponse> saveOrUpdate(@RequestBody Laboratory laboratory) {
        return ResponseEntity.ok(laboratoryService.saveOrUpdate(laboratory));
    }

    @GetMapping("findByPage")
    public ResponseEntity<RestResponse> findByPage(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(SUCCESS(laboratoryService.findByPage(name, page, size), ""));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(laboratoryService.delete(id));
    }
}
