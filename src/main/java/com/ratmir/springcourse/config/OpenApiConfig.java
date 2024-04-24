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
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Production ENV",
                        url = "http://localhost:8080/dev"
                ),
        }
)
@SecuritySchemes(
        value = {
                @SecurityScheme(
                    name = "bearerAuth",
                    description = "JWT auth description",
                    scheme = "bearer",
                    type = SecuritySchemeType.HTTP,
                    bearerFormat = "JWT",
                    in = SecuritySchemeIn.HEADER
                ),
                @SecurityScheme(
                        name = "jwtCookieAuth",
                        type = SecuritySchemeType.APIKEY,
                        in = SecuritySchemeIn.COOKIE,
                        paramName = "Authorization",
                        description = "JWT Bearer Token (access_token) or session cookie"
                )
        }
)
//@SecuritySchemes({
//        @SecurityScheme(
//                name = "bearerToken",
//                type = SecuritySchemeType.HTTP,
//                scheme = "bearer",
//                bearerFormat = "JWT"
//        ),
//        @SecurityScheme(
//                name = "cookie",
//                type = SecuritySchemeType.APIKEY,
//                in = SecuritySchemeIn.COOKIE,
//                paramName = "__Secure-Fgp"
//        )
//})
public class OpenApiConfig {
}
