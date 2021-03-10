package com.eu.classroom.controller;

import com.eu.classroom.common.RestResponse;
import com.eu.classroom.entity.Announcement;
import com.eu.classroom.service.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron
 * @date 2021/3/10 20:52
 */
@RequestMapping("api/announcement")
@RestController
@Api
public class AnnouncementController extends BaseController {

    @Autowired
    private AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<RestResponse> saveOrUpdate(@RequestBody Announcement announcement) {
        return ResponseEntity.ok(announcementService.saveOrUpdate(announcement));
    }

    @GetMapping
    public ResponseEntity<RestResponse> findByPage(@ApiParam(value = "1为true,0为false")@RequestParam(name = "status", required = false) String status,
                                                   @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(SUCCESS(announcementService.findByPage(status, page, size)));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(announcementService.delete(id));
    }
}
