package com.sht.shoesboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.kafka.KafkaProducer;
import com.sht.shoesboot.kafka.KafkaSendResultHandler;
import com.sht.shoesboot.service.GoodsService;
import com.sht.shoesboot.utils.RestResponse;
import javassist.compiler.ast.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Map;

/**
 * @author Aaron
 * @date 2020/12/19 23:00
 */
@RestController
@RequestMapping("api/goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping
    public ResponseEntity<RestResponse> jsoupSave(@Valid @RequestBody Goods goods) {
        goods.setId(null);
        int id = goodsService.save(goods);
        goods.setId(id);
        if (goods.getShelf()) {
            kafkaProducer.send(goods);
        }
        return ResponseEntity.ok(SUCCESS(""));
    }

    @GetMapping
    public ResponseEntity<RestResponse> query(@RequestParam(name = "keyword", required = false) String keyword,
                                              @RequestParam(name = "page", defaultValue = "1") Integer page,
                                              @RequestParam(name = "size", defaultValue = "30") Integer size) {
        try {
            return ResponseEntity.ok(SUCCESS(goodsService.findByPage(page, size, keyword)));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ERROR("请求失败"));
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<RestResponse> queryById(@PathVariable(value = "id") Integer id) {
        Map<String, Object> byId = goodsService.findById(id);
        if (byId == null) {
            return ResponseEntity.badRequest().body(ERROR(404, "不存在"));
        }
        return ResponseEntity.ok().body(SUCCESS(byId));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> soldOut(@RequestParam(name = "id") Integer id) {
        if (!goodsService.soldOut(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(SUCCESS("success"));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@Valid @RequestBody Goods goods) {
        if (goodsService.existsWithPrimaryKey(goods.getId())) {
            int i = goodsService.update(goods);
            if (i != 0) {
                if (goods.getShelf()) {
                    kafkaProducer.send(goods);
                } else {
                    goodsService.soldOut(goods.getId());
                }
                return ResponseEntity.ok().body(SUCCESS("更新成功"));
            }
            return ResponseEntity.badRequest().body(ERROR(400, "更新失败"));
        }
        return ResponseEntity.badRequest().body(ERROR(400, "该商品不存在"));
    }

    @DeleteMapping("delete")
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if (goodsService.existsWithPrimaryKey(id)) {
            goodsService.soldOut(id);
            goodsService.delete(id);
            return ResponseEntity.ok().body(SUCCESS("删除成功"));
        } else {
            return ResponseEntity.badRequest().body(ERROR(400, "该商品不存在"));
        }
    }
}
