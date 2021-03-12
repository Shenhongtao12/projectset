package com.eu.classroom.controller;

import com.eu.classroom.common.RestResponse;
import com.eu.classroom.dto.BorrowReq;
import com.eu.classroom.entity.Reserve;
import com.eu.classroom.service.ReserveService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/3/12 21:46
 */
@RestController
@RequestMapping("api/reserve")
@Api(tags = "预定实验室")
public class ReserveController extends BaseController{

    @Autowired
    private ReserveService reserveService;

    @PostMapping
    public ResponseEntity<RestResponse> saveOrUpdate(@RequestBody Reserve reserve) {
        return ResponseEntity.ok(reserveService.saveOrUpdate(reserve));
    }

    @GetMapping("findByPage")
    public ResponseEntity<RestResponse> findByPage(
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "laboratoryId", required = false) Integer laboratoryId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        return ResponseEntity.ok(SUCCESS(reserveService.findByPage(userId, laboratoryId, page, size), ""));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(reserveService.delete(id));
    }

    @PutMapping
    public ResponseEntity<RestResponse> approval(@RequestBody BorrowReq borrowReq) {
        return ResponseEntity.ok(reserveService.approval(borrowReq));
    }
}
