package com.onlinestore.sales_service.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sales Service API")
                        .version("1.0.0")
                        .description("REST API for managing sales and sale details in the online store system. " +
                                "Handles sale creation, retrieval, and detailed transaction information.")
                        .contact(new Contact()
                                .email("rmatias.dev@gmail.com")
                                .name("Matias Alejandro Rodriguez")
                                .url("https://www.linkedin.com/in/matias-rodriguez-alejandro/"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8086")
                                .description("Development server"),
                        new Server()
                                .url("http://localhost:8080/sales-service")
                                .description("API Gateway")
                ));
    }
}