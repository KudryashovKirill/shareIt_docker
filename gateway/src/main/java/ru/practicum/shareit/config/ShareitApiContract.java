package ru.practicum.shareit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Shareit API",
                version = "1.0.0",
                description = """
                        REST API для публикации объявлений о бронировании вещей.
                        """
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development")
        })
public class ShareitApiContract {
}
