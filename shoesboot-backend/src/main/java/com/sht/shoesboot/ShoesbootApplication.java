package com.sht.shoesboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.sht.shoesboot.Mapper")
public class ShoesbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoesbootApplication.class, args);
    }

}
