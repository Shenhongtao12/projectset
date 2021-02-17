package com.eurasia.food.controller;

import com.eurasia.food.service.UploadService;
import com.eurasia.food.utils.JsonData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//上传图片
@RestController
@RequestMapping({"api/upload"})
@Api(tags = "图片服务")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping({"image"})
    @ApiOperation(value = "上传图片", notes = "上传图片,可选图片路径为/food/classify、/food/carousel")
    public ResponseEntity<JsonData> uploadImage(@RequestParam("file") MultipartFile[] file, @RequestParam(name = "site", defaultValue = "/food/other") String site) {
        JsonData url = this.uploadService.upload(file, site);
        if (StringUtils.isEmpty(url)) {
            return ResponseEntity.badRequest().body(JsonData.buildError("上传失败"));
        }

        return ResponseEntity.ok(url);
    }

    @DeleteMapping({"deleteImage"})
    @ApiOperation(value = "删除单张图片", notes = "删除图片,传入图片的完整url")
    public ResponseEntity<JsonData> delFile(@RequestParam(name = "url") String url) {
        String msg = uploadService.deleteImage(url);
        if ("删除成功".equals(msg)) {
            return ResponseEntity.status(HttpStatus.OK).body(JsonData.buildSuccess(msg));
        }
        return ResponseEntity.status(HttpStatus.OK).body(JsonData.buildError(msg));
    }
}
