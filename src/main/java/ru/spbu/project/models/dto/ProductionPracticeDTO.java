package ru.spbu.project.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spbu.project.models.Leader;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionPracticeDTO {
  @Schema(description = "Идентификатор сотрудника", example = "1")
  Long employeeId;

  @Schema(description = "Идентификатор руководителя", example = "2")
  Long leaderId;

  @Schema(description = "Проект, связанный с практикой", example = "Проект XYZ")
  String project;

  @Schema(description = "Дата начала практики", example = "2023-01-01")
  LocalDate date;
}