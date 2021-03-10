package com.eu.classroom.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.eu.classroom.controller.BaseController;
import com.eu.classroom.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Aaron
 * @date 2020/11/26 21:11
 *
 * 自定义拦截器
 *  继承HandlerInterceptorAdapter
 *
 * preHandle:进入到控制器方法之前执行的内容
 *   boolean：
 *      true：可以继续执行控制器方法
 *      false：拦截
 *   posthandler：执行控制器方法之后执行的内容
 *   afterCompletion：响应结束之前执行的内容
 *
 * 1.简化获取token数据的代码编写
 *      统一的用户权限校验（是否登录）
 * 2.判断用户是否具有当前访问接口的权限
 */
@Component
public class JwtInterceptor extends BaseController implements HandlerInterceptor{

    /**
     * 简化获取token数据的代码编写（判断是否登录）
     *  1.通过request获取请求token信息
     *  2.从token中解析获取claims
     *  3.将claims绑定到request域中
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.通过request获取请求token信息
        String authorization = request.getHeader("Auth-token");
        //判断请求头信息是否为空，或者是否已Bearer开头
        if(StrUtil.isNotEmpty(authorization) && authorization.startsWith("Bearer")) {
            //获取token数据
            String token = authorization.replace("Bearer ","");
            //解析token获取claims
            Claims claims = JwtUtils.checkJWT(token);
            if(claims != null) {
                //通过claims获取到当前用户的可访问API权限字符串
                Integer id = (Integer) claims.get("id");
                //通过handler
                HandlerMethod h = (HandlerMethod) handler;
                //获取接口上的reqeustmapping注解
                //RequestMapping annotation = h.getMethodAnnotation(RequestMapping.class);
                //获取当前请求接口中的name属性
                //String name = annotation.name();

                //判断当前用户是否具有响应的请求权限
                if(id != null) {
                    request.setAttribute("user_claims",claims);
                    return true;
                }else {
                    sendJsonMessage(response, ERROR("鉴权失败"));
                }
            }
        }
        sendJsonMessage(response, ERROR("请登录！"));
        return false;
    }

    /**
     * 响应数据给前端
     * @param response
     * @param obj
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSONUtil.parseObj(obj));
        writer.close();
        try {
            response.flushBuffer();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
