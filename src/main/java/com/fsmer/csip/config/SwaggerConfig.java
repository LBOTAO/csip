package com.fsmer.csip.config;

import com.fsmer.csip.annotation.CurrentUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        List<Parameter> params = new ArrayList<>();
        params.add(new ParameterBuilder().name("Authorization")
                .description("token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .ignoredParameterTypes(CurrentUser.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fsmer.csip.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title("api文档")
                        .description("Security Swagger文档")
                        .version("0.1")
                        .contact(new Contact("liuhuan","","liuh@fsmer"))
                        .license("The Apache License")
                        .licenseUrl("http://www.baidu.com")
                        .build())
                .globalOperationParameters(params);
    }
}
