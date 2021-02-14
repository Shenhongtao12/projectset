package com.sht.vehicle.controller;

import com.sht.vehicle.common.RestResponse;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aaron
 * @date 2021/01/12 20:00
 */
public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Integer userId;
    protected Claims claims;

    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response) {
        this.request = request;
        this.response = response;

        Object obj = request.getAttribute("user_claims");

        if(obj != null) {
            this.claims = (Claims) obj;
            this.userId = (Integer)claims.get("id");
        }
    }

    public RestResponse SUCCESS(String message){
        return new RestResponse(200, message);
    }

    public RestResponse SUCCESS(Object data) {
        return new RestResponse(200, data);
    }

    public RestResponse SUCCESS(Object data, String message) {
        return new RestResponse(200, data, message);
    }

    public RestResponse ERROR(String message) {
        return new RestResponse(400, message);
    }

    public RestResponse ERROR(Integer code, String message) {
        return new RestResponse(code, message);
    }
}
