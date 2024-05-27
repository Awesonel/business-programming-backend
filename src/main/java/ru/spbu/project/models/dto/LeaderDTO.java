package ru.spbu.project.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderDTO {

  @Schema(description = "Имя руководителя", example = "Иван")
  private String name;

  @Schema(description = "Фамилия руководителя", example = "Иванов")
  private String surname;

  @Schema(description = "Отчество руководителя", example = "Иванович")
  private String patronymic;

  @Schema(description = "Должность руководителя", example = "Менеджер")
  private String job;
}