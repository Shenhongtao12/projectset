package com.sht.shoesboot.controller;

import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.service.GoodsService;
import com.sht.shoesboot.service.RedisService;
import com.sht.shoesboot.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    private RedisService redisService;

    @PostMapping
    public ResponseEntity<RestResponse> jsoupSave(@Valid @RequestBody Goods goods) {
        goods.setId(null);
        //goods.setSalesVolume(0)
        int id = goodsService.save(goods);
        goods.setId(goods.getId());
        if (goods.getShelf()) {
            goodsService.saveGoodsToEs(goods);
            redisService.setData("shoes_goods_" + id, goods.getInventory().toString());
        }
        return ResponseEntity.ok(SUCCESS(id, "添加成功"));
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
            return ResponseEntity.ok().body(ERROR(404, "不存在"));
        }
        return ResponseEntity.ok().body(SUCCESS(byId));
    }

    @GetMapping("findById")
    public ResponseEntity<RestResponse> findById(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(SUCCESS(goodsService.findByID(id)));
    }

    @GetMapping("findGoodsPage")
    public ResponseEntity<RestResponse> findByPage(@RequestParam(name = "keyword", required = false) String keyword,
                                                   @RequestParam(name = "shelf", required = false) Boolean shelf,
                                                   @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(SUCCESS(goodsService.findGoodsPage(keyword, shelf, page, size)));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> soldOut(@RequestParam(name = "id") Integer id) {
        if (!goodsService.soldOut(id)) {
            redisService.deleteData("shoes_goods_" + id);
            return ResponseEntity.ok(ERROR("不存在id为'" + id + "'的商品"));
        }
        return ResponseEntity.ok().body(SUCCESS("success"));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@Valid @RequestBody Goods goods) {
        if (goodsService.existsWithPrimaryKey(goods.getId())) {
            int i = goodsService.update(goods);
            if (i != 0) {
                if (goods.getShelf()) {
                    goodsService.saveGoodsToEs(goods);
                    redisService.setData("shoes_goods_" + goods.getId(), goods.getInventory().toString());
                } else {
                    goodsService.soldOut(goods.getId());
                }
                return ResponseEntity.ok().body(SUCCESS("更新成功"));
            }
            return ResponseEntity.ok().body(ERROR(400, "更新失败"));
        }
        return ResponseEntity.ok().body(ERROR(400, "该商品不存在"));
    }

    @DeleteMapping("delete")
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if (goodsService.existsWithPrimaryKey(id)) {
            goodsService.soldOut(id);
            redisService.deleteData("shoes_goods_" + id);
            goodsService.delete(id);
            return ResponseEntity.ok().body(SUCCESS("删除成功"));
        } else {
            return ResponseEntity.ok().body(ERROR(400, "该商品不存在"));
        }
    }
}
