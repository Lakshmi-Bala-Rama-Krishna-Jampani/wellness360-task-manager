package com.wellness360.taskmanager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * Ensures Swagger schemas use the same snake_case JSON names as the API
     * (aligned with spring.jackson.property-naming-strategy: SNAKE_CASE).
     */
    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    public OpenAPI taskManagerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .description("Production-grade RESTful API for task management with Basic Authentication")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Wellness360 Engineering")
                                .email("engineering@wellness360.example"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .description("HTTP Basic Authentication")));
    }
}
