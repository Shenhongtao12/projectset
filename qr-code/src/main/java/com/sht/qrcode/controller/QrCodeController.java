package com.sht.qrcode.controller;

import com.sht.qrcode.config.AllException;
import com.sht.qrcode.entity.QrCode;
import com.sht.qrcode.entity.QrEntity;
import com.sht.qrcode.mapper.QrMapper;
import com.sht.qrcode.service.QrCodeService;
import com.sht.qrcode.service.UploadService;
import com.sht.qrcode.utils.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Aaron
 * @date 2021/3/6 19:25
 */
@Slf4j
@RestController
@RequestMapping("api/qr-code")
public class QrCodeController {

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private QrMapper qrMapper;

    @PostMapping
    public void getQRCode(QrCode qrCode, HttpServletResponse response) {
        Boolean bool = qrCode.getFile() == null && (qrCode.getContent() == null || "".equals(qrCode.getContent()));
        if (bool) {
            response.setStatus(400);
            new AllException(400, "失败");
        }
        QrEntity qrEntity = new QrEntity();
        qrEntity.setContent(qrCode.getContent());
        if (qrCode.getFile() != null) {
            JsonData upload = uploadService.upload(qrCode.getFile(), "/qrcode/images");
            if (upload.getCode() == 200) {
                log.info("返回的图片链接：{}", qrCode.getContent());
                qrEntity.setImage(upload.getData().toString());
            } else {
                new AllException(400, "失败");
            }
        }

        int i = qrMapper.insertSelective(qrEntity);
        int id = qrEntity.getId();
        String url = qrMapper.findIp() + id;
        log.info(url);
        qrCodeService.createQRCode2Stream(url, response);
        log.info("成功生成二维码！");
    }

    @GetMapping
    public ResponseEntity<JsonData> findById(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(JsonData.buildSuccess(qrMapper.selectByPrimaryKey(id), "成功"));
    }
}
