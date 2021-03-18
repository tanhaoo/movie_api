package com.th.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author TanHaooo
 * @date 2021/3/18 21:29
 */
@Configuration
public class CorsConfig {
    private CorsConfiguration buildConfig() {
        CorsConfiguration configuration = new CorsConfiguration();
        //设置属性
        //允许跨域请求的地址，*表示所有
        configuration.addAllowedOriginPattern("*");
        //配置跨域请求头
        configuration.addAllowedHeader("*");
        //配置跨域的请求方法
        configuration.addAllowedMethod("*");
        //表示跨域请求的时候是否使用的是同一个session
        configuration.setAllowCredentials(true);
        return configuration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

}
