package com.sht.shoesboot.controller;

import com.sht.shoesboot.entity.Goods;
import com.sht.shoesboot.service.GoodsService;
import com.sht.shoesboot.utils.RestResponse;
import javassist.compiler.ast.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<RestResponse> jsoupSave(@RequestBody Goods goods) {
        goods.setId(null);
        goodsService.save(goods);
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
            return ResponseEntity.badRequest().body(ERROR("不存在"));
        }
        return ResponseEntity.ok().body(SUCCESS(byId));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if(goodsService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(SUCCESS("success"));
    }
}
