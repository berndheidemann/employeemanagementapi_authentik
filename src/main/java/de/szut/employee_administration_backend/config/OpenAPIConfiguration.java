package de.szut.employee_administration_backend.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    private final ServletContext context;

    public OpenAPIConfiguration(ServletContext context) {
        this.context = context;
    }


    @Bean
    public OpenAPI springShopOpenAPI(
            //  @Value("${info.app.version}") String appVersion,
    ) {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .addServersItem(new Server().url(this.context.getContextPath()))
                .info(new Info()
                        .title("Employees Management Micro-Service")
                        .description("\n## Overview\n" + "\nEmployees Management Service API manages the employees of HighTec Gmbh including their qualifications. " +
                                "It offers the possibility to create, read, update and delete employees and qualifications. Existing employees can be assigned new " +
                                "qualifications or have them withdrawn. \nThe API is organized around REST. It has predictable resource-oriented URLs, accepts JSON-encoded request bodies, " +
                                "returns JSON-encoded responses, uses standard HTTP response codes and authentication.\n"
                        )
                        .version("1.1.2"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }


}