package com.th.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author TanHaooo
 * @date 2021/5/7 21:31
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 访问路径：http://localhost:93/facereport/static/captured/daping_report/captured_images/face/788001414206074880/2021/1/27/cj_142207194912440320.png
         * "/facereport/static/captured/**" 为前端URL访问路径
         * "file:" + uploadPath 是本地磁盘映射
         */
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + uploadPath);
    }
}
