package ru.spbu.project.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefuseApplicationDTO {
  @Schema(description = "Идентификатор заявления", example = "1")
  Long id;

  @Schema(description = "Причина отказа", example = "Недостаток времени")
  String reason;

  @Schema(description = "Дата отказа", example = "2023-01-01")
  LocalDate date;
}