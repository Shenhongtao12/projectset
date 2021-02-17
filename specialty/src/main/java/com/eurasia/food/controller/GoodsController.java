package com.eurasia.food.controller;

import com.eurasia.food.entity.Goods;
import com.eurasia.food.service.GoodsService;
import com.eurasia.food.utils.JsonData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping({"api/goods"})
@Api(tags = "美食")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @PostMapping({"add"})
    public ResponseEntity add(@RequestBody Goods goods) throws Exception {
        return ResponseEntity.ok(this.goodsService.add(goods));
    }


    @GetMapping({"findById"})
    public ResponseEntity<JsonData> findById(@RequestParam("id") Integer id) throws Exception {
        return ResponseEntity.ok(JsonData.buildSuccess(this.goodsService.findById(id), ""));
    }


    @PutMapping({"update"})
    public ResponseEntity update(@RequestBody Goods goods) throws Exception {
        return ResponseEntity.ok(this.goodsService.update(goods));
    }


    @GetMapping({"findByPage"})
    public ResponseEntity<JsonData> findByPage(@RequestParam(value = "id", required = false) Integer id, //二级分类id
                                               @RequestParam(value = "classify", required = false) Integer classify,
                                               @RequestParam(value = "orderBy", defaultValue = "id", required = false) String orderBy,
                                               @RequestParam(value = "status", required = false) String status,
                                               @RequestParam(value = "goodsName", required = false) String goodsName,
                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "rows", defaultValue = "20") Integer rows) {
        return ResponseEntity.ok(JsonData.buildSuccess(this.goodsService.findByPage(id, classify, orderBy, status, goodsName, page, rows), ""));
    }


    @DeleteMapping({"deleteGoods"})
    public ResponseEntity deleteGoods(Integer id) {
        return ResponseEntity.ok(this.goodsService.deleteGoods(id));
    }


}
