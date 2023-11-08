package com.cjg.traveling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration{
	
	/*
    Docket: Swagger 설정의 핵심이 되는 Bean
    apiInfo:Swagger UI 로 노출할 정보
    apis: api 스펙이 작성되어 있는 패키지 (Controller) 를 지정
    paths: apis 에 있는 API 중 특정 path 를 선택
    */

    private static final String SERVICE_NAME = "traveling";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "traveling API";
    private static final String API_URL = "http://localhost:8080/";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cjg.traveling")) // RequestMapping의 모든 URL LIST 
                .paths(PathSelectors.any()) // .any() -> ant(/api/**") /api/**인 URL만 표시
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(SERVICE_NAME) // 서비스명
                .version(API_VERSION)                   // API 버전
                .description(API_DESCRIPTION)           // API 설명
                .termsOfServiceUrl(API_URL)             // 서비스 url
                .build();
    }
    
	
}
