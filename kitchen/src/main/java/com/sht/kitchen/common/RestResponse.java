package com.sht.kitchen.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Aaron
 * @date 2021/2/12 11:48
 */
@Getter
@Setter
@NoArgsConstructor
public class RestResponse {
    private Integer code;
    private String message;
    private Object data;

    public RestResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestResponse(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public RestResponse(Integer code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
