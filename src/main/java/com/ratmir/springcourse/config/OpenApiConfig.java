package com.ratmir.springcourse.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Ratmir",
                        email = "ratmirvslvvslv@gmail.com"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification - SecurityDemo",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://some-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers =  {
                @Server(
                        description = "Local ENV",
                        url = "https://monkfish-app-n9pmx.ondigitalocean.app"
                ),
                @Server(
                        description = "Production ENV",
                        url = "https://monkfish-app-n9pmx.ondigitalocean.app"
                ),
        }
)
@SecuritySchemes(
        value = {
                @SecurityScheme(
                        name = "Bearer Authentication",
                        description = "JWT auth description",
                        type = SecuritySchemeType.HTTP,
                        bearerFormat = "JWT",
                        scheme = "bearer"
                )
        }
)
public class OpenApiConfig {
}
