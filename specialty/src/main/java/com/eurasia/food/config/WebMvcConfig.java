package com.eurasia.food.config;

import com.eurasia.food.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

//@Configuration
//public class InterceptorConfig extends WebMvcConfigurationSupport {
//
//    @Autowired
//    private JwtInterceptor jwtInterceptor;
//
//    /**
//     * 添加拦截器的配置
//     */
//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        //1.添加自定义拦截器
//        registry.addInterceptor(jwtInterceptor).
//                addPathPatterns("/api/*/**").//2.指定拦截器的url地址
//                excludePathPatterns("/api/user/login","/api/user/init","/eurasia/**");//3.指定不拦截的url地址
//    }
//}

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 跨域拦截器需放在最上面
        registry.addInterceptor(corsInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/user/**",
                        "/api/admin/login",
                        "/api/goods/findByPage",
                        "api/post/findByClassifyOrMatter",
                        "api/goods/findById",
                        "api/matter/**",
                        "api/upload/**",
                        "api/classify/"
                );

        WebMvcConfigurer.super.addInterceptors(registry);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowCredentials(true).maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/food/**").addResourceLocations("file:/food/");
    }
}
