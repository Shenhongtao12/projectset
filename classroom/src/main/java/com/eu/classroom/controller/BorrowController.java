package com.eu.classroom.controller;

import com.eu.classroom.common.RestResponse;
import com.eu.classroom.dto.BorrowReq;
import com.eu.classroom.entity.Borrow;
import com.eu.classroom.entity.Borrow;
import com.eu.classroom.service.BorrowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/3/11 20:26
 */
@RestController
@RequestMapping("api/borrow")
@Api(tags = "器材借还")
public class BorrowController extends BaseController{

    @Autowired
    private BorrowService borrowService;

    @PostMapping
    public ResponseEntity<RestResponse> saveOrUpdate(@RequestBody Borrow borrow) {
        return ResponseEntity.ok(borrowService.saveOrUpdate(borrow));
    }

    @GetMapping("findByPage")
    public ResponseEntity<RestResponse> findByPage(
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "equipmentId", required = false) Integer equipmentId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(SUCCESS(borrowService.findByPage(userId, equipmentId, page, size), ""));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(borrowService.delete(id));
    }

    @PutMapping
    public ResponseEntity<RestResponse> approval(@RequestBody BorrowReq borrowReq) {
        return ResponseEntity.ok(borrowService.approval(borrowReq));
    }

    @PutMapping("refund")
    public ResponseEntity<RestResponse> refund(@RequestParam(name = "id")Integer id) {
        return ResponseEntity.ok(borrowService.refund(id));
    }
}
