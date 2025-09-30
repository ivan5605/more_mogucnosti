package hr.moremogucnosti.more_mogucnosti_backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("More Mogućnosti API")
                        .version("v1")
                        .description("Public i admin rute")
                )
                .tags(List.of(
                        new Tag().name("Rezervacije").description("Upravljanje rezervacijama"),
                        new Tag().name("Korisnici").description("Upravljanje korisnicima"),
                        new Tag().name("Recenzije").description("Upravljanje recenzijama"),
                        new Tag().name("Sobe").description("Upravljanje sobama"),
                        new Tag().name("Slike - hotel").description("Upravljanje slikama hotela"),
                        new Tag().name("Slike - soba").description("Upravljanje slikama soba"),
                        new Tag().name("Hoteli").description("Upravljanje hotelima"),
                        new Tag().name("Authentication").description("Upravljanje autentikacijom"),
                        new Tag().name("Uloge").description("Upravljanje ulogama")
                ))

                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Dev"),
                        new Server().url("https://moremogucnosti.hr").description("Prod")
                ))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
