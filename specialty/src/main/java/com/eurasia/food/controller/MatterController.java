package com.eurasia.food.controller;

import com.eurasia.food.entity.Matter;
import com.eurasia.food.service.MatterService;
import com.eurasia.food.utils.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2020/5/23 - 13:33
 **/
@RestController
@RequestMapping("api/matter")
@Api(tags = "热点资讯")
public class MatterController {

    @Autowired
    private MatterService matterService;

    @PostMapping
    @ApiOperation(value = "添加或更新资讯", notes = "不传id为添加，传id为更新")
    public ResponseEntity<JsonData> save(@RequestBody Matter matter){
        return ResponseEntity.status(HttpStatus.OK).body(matterService.save(matter));
    }

    @DeleteMapping
    public ResponseEntity<JsonData> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(matterService.delete(id));
    }

    @GetMapping
    public ResponseEntity<JsonData> findByPage(@RequestParam(name = "id", required = false) Integer id,
                                               @RequestParam(name = "title", required = false) String title,
                                               @RequestParam(name = "page", defaultValue = "0") Integer page,
                                               @RequestParam(name = "rows", defaultValue = "20") Integer rows) {
        return ResponseEntity.ok(JsonData.buildSuccess(matterService.findByPage(id, title, page, rows), ""));
    }

    @GetMapping("findById")
    public ResponseEntity<JsonData> findById(Integer id) {
        return ResponseEntity.ok(matterService.findById(id));
    }

}
