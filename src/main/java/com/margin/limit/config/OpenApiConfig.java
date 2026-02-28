package com.margin.limit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Margin & Risk Limits API")
                        .version("1.0.0")
                        .description("High-performance microservice for pre-trade risk validation.")
                        .contact(new Contact()
                                .name("Venkata Siva Reddy Naga")
                                .url("https://github.com/venkatasivanaga")));
    }
}