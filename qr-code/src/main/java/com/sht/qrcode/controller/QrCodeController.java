package com.sht.qrcode.controller;

import com.sht.qrcode.entity.QrCode;
import com.sht.qrcode.service.QrCodeService;
import com.sht.qrcode.service.UploadService;
import com.sht.qrcode.utils.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
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

    @PostMapping
    public ResponseEntity<JsonData> getQRCode(QrCode qrCode, HttpServletResponse response) {
        try {
            Boolean bool = qrCode.getFile() == null && (qrCode.getContent() == null || "".equals(qrCode.getContent()));
            if (bool) {
                return ResponseEntity.ok(JsonData.buildError("不能为空数据"));
            }
            if (qrCode.getFile() != null) {
                JsonData upload = uploadService.upload(qrCode.getFile(), "/qrcode/images");
                if (upload.getCode() == 200) {
                    qrCode.setContent(upload.getData().toString());
                    log.info("返回的图片链接：{}", qrCode.getContent());
                } else {
                    return ResponseEntity.ok(upload);
                }
            }
            qrCodeService.createQRCode2Stream(qrCode.getContent(), response);
            log.info("成功生成二维码！");
            return ResponseEntity.ok(JsonData.buildSuccess("成功"));
        } catch (Exception e) {
            log.error("发生错误， 错误信息是：{}！", e.getMessage());
            return ResponseEntity.ok(JsonData.buildError(e.getMessage(), "发送异常，失败"));
        }
    }
}
