package com.eu.classroom.controller;

import com.eu.classroom.common.RestResponse;
import com.eu.classroom.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 */
@RestController
@RequestMapping({"api/upload"})
@Api(tags = "图片服务")
public class UploadController extends BaseController{

    @Autowired
    private UploadService uploadService;

    @PostMapping({"image"})
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public ResponseEntity<RestResponse> uploadImage(@RequestParam("file") MultipartFile[] file, @RequestParam(name = "site", defaultValue = "/classroom/images") String site) {
        RestResponse url = this.uploadService.upload(file, site);
        return ResponseEntity.ok(url);
    }
}
