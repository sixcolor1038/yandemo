package com.yan.demo.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    private final SwaggerProperties swaggerProperties;

    public SwaggerConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public OpenAPI springDocOpenAPI() {

        ApiResponse api401Response = new ApiResponse();
        api401Response.setDescription("未授权");
        ApiResponse api500Response = new ApiResponse();
        api500Response.setDescription("服务器内部错误");
        api500Response.setContent(new Content()
                .addMediaType("application/json", new MediaType().schema(new Schema().$ref("ResponseResult"))));
        ApiResponse api200Response = new ApiResponse();
        api200Response.setDescription("操作成功");
        api200Response.setContent(new Content()
                .addMediaType("application/json", new MediaType().schema(new Schema().$ref("ResponseResult"))));
        //配置认证、请求头参数
        Components components = new Components();
        components.addResponses("401", api401Response)
                .addResponses("500", api500Response)
                .addResponses("200", api200Response)
                .addSecuritySchemes("jwt-token", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"));
        //.addSecuritySchemes("basicScheme", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"))
        //.addParameters("myHeader1", new Parameter().in("header").schema(new StringSchema()).name("myHeader1"))
        // 接口调试路径
        Server tryServer = new Server();
        tryServer.setUrl(swaggerProperties.getTryHost());
        return new OpenAPI()
                .components(components)
                .servers(Collections.singletonList(tryServer))
                .info(new Info()
                        .title(swaggerProperties.getApplicationName() + " Api Doc")
                        .description(swaggerProperties.getApplicationDescription())
                        .version("Application Version: " + swaggerProperties.getApplicationVersion() + "\n Spring Boot Version: " + SpringBootVersion.getVersion())
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("SpringDoc Full Documentation")
                        .url("https://springdoc.org/")
                );
    }

    @Bean
    public GroupedOpenApi departmentApi() {
        return GroupedOpenApi.builder()
                .group("department-api")
                .pathsToMatch("/hr/department/**")
                .build();
    }

    @Bean
    public GroupedOpenApi employeeApi() {
        return GroupedOpenApi.builder()
                .group("employee-api")
                .pathsToMatch("/hr/employees/**")
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all-api")
                .pathsToMatch("/**/**")
                .build();
    }

}
