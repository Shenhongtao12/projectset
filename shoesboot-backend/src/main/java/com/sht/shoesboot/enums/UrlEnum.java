package com.sht.shoesboot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aaron
 * @date 2020/12/15 22:47
 */
public enum UrlEnum {

    /**
     *
     */
    EMAIL("http://47.98.128.88:8080/api/email"),
    UPLOAD("http://47.98.128.88:8080/api/upload")
    ;

    private final String url;

    private UrlEnum(String url) {
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
}
