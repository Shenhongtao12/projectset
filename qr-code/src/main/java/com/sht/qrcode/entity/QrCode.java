package com.sht.qrcode.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Aaron
 * @date 2021/3/7 12:10
 */
@Data
public class QrCode {
    private String content;

    private MultipartFile file;
}
