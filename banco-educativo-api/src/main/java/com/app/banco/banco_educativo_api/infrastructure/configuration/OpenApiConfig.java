package com.app.banco.banco_educativo_api.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI (Swagger).
 * 
 * Capa: INFRASTRUCTURE
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banco Educativo API")
                        .version("1.0.0")
                        .description("API REST para el sistema bancario educativo. " +
                                "Permite gestión de clientes, cuentas y operaciones financieras.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@bancoeducativo.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
