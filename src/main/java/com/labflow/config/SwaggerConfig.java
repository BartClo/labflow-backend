package com.labflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LabFlow Backend API")
                        .version("1.0.0")
                        .description("API REST para el sistema LabFlow - Gestión de clientes y laboratorios")
                        .contact(new Contact()
                                .name("Equipo LabFlow")
                                .email("support@labflow.com")
                                .url("https://github.com/BartClo/labflow-backend"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}