package com.code.orientation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger文档配置类
 */
@Configuration
public class SwaggerConfig {
    private static final String API_TILE = "智慧迎新系统";

    /**
     * 配置Swagger2的Docket的Bean实例
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // apiInfo()：配置 API 的一些基本信息，比如：文档标题title，文档描述description，文档版本号version
                .apiInfo(apiInfo())
                // select()：生成 API 文档的选择器，用于指定要生成哪些 API 文档
                .select()
                // apis()：指定要生成哪个包下的 API 文档
                .apis(RequestHandlerSelectors.basePackage("com.code.orientation.controller"))
                // paths()：指定要生成哪个 URL 匹配模式下的 API 文档。这里使用 PathSelectors.any()，表示生成所有的 API 文档。
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档标题
                .title(API_TILE)
                // 文档描述信息
                .description("智慧迎新平台")
                // 文档版本号
                .version("1.0")
                .build();

    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TILE)
                        .version("1.0"));
    }
}
