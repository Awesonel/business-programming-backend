package ru.spbu.project.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spbu.project.models.enums.TestType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {
  @Schema(description = "Процент результата теста", example = "85.5")
  Double score;

  @Schema(description = "Тип теста", example = "MODULE")
  TestType testType;

  @Schema(description = "Дата прохождения теста", example = "2023-01-01")
  LocalDate date;
}