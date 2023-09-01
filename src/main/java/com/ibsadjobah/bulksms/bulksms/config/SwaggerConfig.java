package com.ibsadjobah.bulksms.bulksms.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public Docket api()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(
                        new ApiInfoBuilder()
                                .title("Bulk-SMS")
                                .description("API Rest d'envoi d'SMS de masse")
                                .build()
                )
                .groupName("")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ibsadjobah.bulksms.bulksms"))
                .paths(PathSelectors.ant("api/v1/**"))
                .build();
    }
}
