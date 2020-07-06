package com.project.auto.testing.messagepublisher.build.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Auto Test Project - Message Publisher")
                .description("Project Test Automation for publishing messages")
                .version("1.0.0")
                .license("Apache License v2.0")
                .licenseUrl("https://github.com/rishuatgithub/ProjectAutoTesting/blob/master/LICENSE")
                .contact(new Contact("Rishu Shrivastava","","rishu.shrivastava@gmail.com"))
                .build();
    }
}
