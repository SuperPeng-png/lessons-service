package com.yourname.tutor.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Lessons Service API",
                version = "v1",
                description = "REST API for managing tutors and lessons.",
                contact = @Contact(name = "Lessons Service Team", email = "team@example.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development"),
                @Server(url = "https://staging.example.com", description = "Staging"),
                @Server(url = "https://api.example.com", description = "Production")
        }
)
public class OpenApiConfig {
}
