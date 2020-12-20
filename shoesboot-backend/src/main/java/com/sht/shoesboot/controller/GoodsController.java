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
        goodsService.save(goods);
        return ResponseEntity.ok(SUCCESS(""));
    }

    @GetMapping("{keyword}")
    public ResponseEntity<RestResponse> query(@PathVariable(value = "keyword", required = false) String keyword,
                                              @RequestParam(name = "page", defaultValue = "1") Integer page,
                                              @RequestParam(name = "size", defaultValue = "30") Integer size) {
        try {
            return ResponseEntity.ok(SUCCESS(goodsService.findByPage(page, size, keyword)));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ERROR("请求失败"));
        }
    }
}
