package com.sht.kitchen.config;

import com.sht.kitchen.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Aaron
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private CorsInterceptor corsInterceptor;
    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 跨域拦截器需放在最上面
        registry.addInterceptor(corsInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**")
                //配置不拦截的api
                .excludePathPatterns(
                        "/api/user/**",
                        "/api/admin/login",
                        "/api/goods/findByPage",
                        "api/post/findByClassifyOrMatter",
                        "api/goods/findById",
                        "api/matter/**",
                        "api/upload/**",
                        "api/classify/"
                )
        ;

        WebMvcConfigurer.super.addInterceptors(registry);
    }

    /**
     * 跨域处理
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowCredentials(true).maxAge(3600);
    }

}
