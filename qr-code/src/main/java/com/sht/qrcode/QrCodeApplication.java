package com.sht.qrcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.sht.qrcode.mapper")
public class QrCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrCodeApplication.class, args);
    }

}
