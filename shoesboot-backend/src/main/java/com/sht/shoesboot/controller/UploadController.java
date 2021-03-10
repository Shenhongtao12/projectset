package com.sht.shoesboot.controller;

import com.sht.shoesboot.service.UploadService;
import com.sht.shoesboot.utils.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//上传图片
@RestController
@RequestMapping({"api/upload"})
@Api(tags = "图片服务")
public class UploadController extends BaseController{
    @Autowired
    private UploadService uploadService;

    @PostMapping({"image"})
    @ApiOperation(value = "上传图片", notes = "上传图片,可选图片路径为/shoes/classify、/shoes/carousel")
    public ResponseEntity<RestResponse> uploadImage(@RequestParam("file") MultipartFile[] file, @RequestParam(name = "site", defaultValue = "/eurasia/other") String site) {
        RestResponse url = this.uploadService.upload(file, site);
        if (url.getCode() != 200) {
            return ResponseEntity.badRequest().body(ERROR("上传失败"));
        }
        return ResponseEntity.ok(url);
    }
}
