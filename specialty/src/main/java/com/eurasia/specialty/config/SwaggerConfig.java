package com.eurasia.specialty.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/6/6 - 17:13
 **/
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("默认分组")
                //是否启动swagger
                .enable(true)
                .select()
                //配置扫描的包  RequestHandlerSelectors.any()  扫描全部
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                //过滤的路径  .paths(PathSelectors.ant("path"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     *
     * @return
     */
    private ApiInfo apiInfo() {

        Contact contact = new Contact("沈鸿涛", "http://eurasia.plus/swagger-ui.html", "shenhongtao12@aliyun.com");

        return new ApiInfoBuilder()
                .title("鞋靴直销系统 APIs")
                //描述
                .description("api接口文档")
                .termsOfServiceUrl("http://eurasia.plus/swagger-ui.html")
                .contact(contact)
                .version("1.0")
                .build();
    }
}

