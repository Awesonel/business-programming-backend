package ru.spbu.project.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmApplicationDTO {
  @Schema(description = "Идентификатор сотрудника", example = "1")
  Long employeeId;

  @Schema(description = "Дата подтверждения заявления", example = "2023-01-01")
  LocalDate date;
}