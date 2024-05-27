package ru.spbu.project.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Automation of the learning process",
                version = "1.0.0",
                contact = @Contact(
                        name = "The winning team",
                        email = "tolstok.sav@mail.ru",
                        url = "https://github.com/Awesonel/business-programming-backend"
                )
        )
)

@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi controllerApi() {
    return GroupedOpenApi.builder()
            .group("controller-api")
            .packagesToScan("ru.spbu.project.controllers") // Specify the package to scan
            .build();
  }

}
