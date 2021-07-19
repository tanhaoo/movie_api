package com.th.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author TanHaooo
 * @date 2021/5/20 18:36
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket getUserDocket(){
        ApiInfo apiInfo=new ApiInfoBuilder()
                .title("电影API文档")//api标题
//                .description("用户管理相关接口描述")//api描述
                .version("1.0.0")//版本号
                .build();
        return new Docket(DocumentationType.SWAGGER_2)//文档类型（swagger2）
                .apiInfo(apiInfo)//设置包含在json ResourceListing响应中的api元信息
                .select()//启动用于api选择的构建器
                .apis(RequestHandlerSelectors.basePackage("com.th.controller"))//扫描接口的包
                .paths(PathSelectors.any())//路径过滤器（扫描所有路径）
                .build();
    }
}
