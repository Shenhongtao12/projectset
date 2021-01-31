package com.eurasia.specialty.controller;

import com.eurasia.specialty.entity.Classify;
import com.eurasia.specialty.service.ClassifyService;
import com.eurasia.specialty.utils.JsonData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2020/5/16 - 17:30
 **/
@RestController
@RequestMapping("api/classify")
@Api(tags = "分类服务")
public class ClassifyController {

    @Autowired
    private ClassifyService classifyService;

    @PostMapping("save")
    public ResponseEntity<JsonData> save(@RequestBody Classify classify) {
        Classify save = classifyService.save(classify);
        JsonData jsonData = new JsonData();
        jsonData.setCode(200);
        if (save.getId() == null){
            jsonData.setCode(400);
        }
        return ResponseEntity.status(HttpStatus.OK).body(jsonData);
    }

    @PutMapping("update")
    public ResponseEntity<JsonData> update(@RequestBody Classify classify) {
        return ResponseEntity.status(HttpStatus.OK).body(classifyService.update(classify));
    }
    @GetMapping()
    public ResponseEntity<JsonData> findAll(){
        return ResponseEntity.ok(JsonData.buildSuccess(classifyService.findAll(), ""));
    }
}
