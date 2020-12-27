package com.sht.shoesboot.controller;

import com.sht.shoesboot.entity.Classify;
import com.sht.shoesboot.service.ClassifyService;
import com.sht.shoesboot.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Aaron
 * @date 2020/12/27 19:44
 */
@RestController
@RequestMapping("api/classify")
public class ClassifyController extends BaseController{

    @Autowired
    private ClassifyService classifyService;

    @PostMapping
    public ResponseEntity<RestResponse> save(@Valid @RequestBody Classify classify) {
        if (classifyService.save(classify)) {
            return ResponseEntity.ok(SUCCESS("成功"));
        }
        return ResponseEntity.badRequest().body(ERROR(400, "该分类已存在"));
    }

    @GetMapping
    public ResponseEntity<RestResponse> queryPage(@RequestParam(name = "page") Integer page,
                                                 @RequestParam(name = "size") Integer size) {
        return ResponseEntity.ok(SUCCESS(classifyService.queryPage(page, size)));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@Valid @RequestBody Classify classify) {
        if (classifyService.update(classify)) {
            return ResponseEntity.ok(SUCCESS("成功"));
        }
        return ResponseEntity.badRequest().body(ERROR(400, "该分类不存在"));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(Integer id) {
        classifyService.delete(id);
        return ResponseEntity.ok(SUCCESS("成功"));
    }
}
