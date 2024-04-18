package com.yan.demo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 20:04
 * @Description:
 */
@Configuration
public class SwaggerConfig {
    // 控制是否允许swagger
    private static final boolean enableSwagger = true;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30) //展示的swagger文档格
                .enable(enableSwagger) //是否启动swagger配置
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }


}
