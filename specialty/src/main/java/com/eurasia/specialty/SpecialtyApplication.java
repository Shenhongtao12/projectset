package com.eurasia.specialty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 *
 * @author sht
 */
@SpringBootApplication
@EnableScheduling
@EntityScan(value="com.eurasia.specialty.entity")
public class SpecialtyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpecialtyApplication.class, args);
    }

    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}
