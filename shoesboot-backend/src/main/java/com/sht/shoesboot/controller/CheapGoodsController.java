package com.sht.shoesboot.controller;

import com.sht.shoesboot.entity.CheapGoods;
import com.sht.shoesboot.service.CheapGoodsService;
import com.sht.shoesboot.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/1/29 22:01
 */
@RestController
@RequestMapping("api/goods")
public class CheapGoodsController extends BaseController {

    @Autowired
    private CheapGoodsService cheapGoodsService;

    @PostMapping("cheap-goods")
    public ResponseEntity<RestResponse> setup(@RequestBody CheapGoods cheapGoods) {
        if (userId != null) {
            cheapGoods.setSetupAdmin(userId);
        }
        return ResponseEntity.ok(cheapGoodsService.setup(cheapGoods));
    }

    @GetMapping("cheap-goods")
    public ResponseEntity<RestResponse> queryPage(@RequestParam(name = "status", defaultValue = "true") Boolean status,
                                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(name = "rows", defaultValue = "20") Integer rows) {

        return ResponseEntity.ok(SUCCESS(cheapGoodsService.queryPage(status, page, rows), ""));
    }

    @DeleteMapping("cheap-goods")
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        cheapGoodsService.delete(id);
        return ResponseEntity.ok(SUCCESS("删除成功"));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@RequestBody CheapGoods cheapGoods) {
        return ResponseEntity.ok(cheapGoodsService.update(cheapGoods));
    }

    @GetMapping("cheap-goods/findById")
    public ResponseEntity<RestResponse> findById(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(SUCCESS(cheapGoodsService.findById(id)));
    }
}