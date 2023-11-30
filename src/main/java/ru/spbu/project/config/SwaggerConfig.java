package ru.spbu.project.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
