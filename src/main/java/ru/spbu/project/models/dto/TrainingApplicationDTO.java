package ru.spbu.project.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingApplicationDTO {
  @Schema(description = "Идентификатор руководителя", example = "1")
  Long leaderId;

  @Schema(description = "Проект, связанный с заявлением", example = "Проект XYZ")
  String project;

  @Schema(description = "Имя сотрудника", example = "Иван")
  String employeeName;

  @Schema(description = "Фамилия сотрудника", example = "Иванов")
  String employeeSurname;

  @Schema(description = "Отчество сотрудника", example = "Иванович")
  String employeePatronymic;

  @Schema(description = "Должность сотрудника", example = "Разработчик")
  String employeeJobTitle;

  @Schema(description = "Цель обучения сотрудника", example = "Повышение квалификации")
  String trainingPurpose;

  @Schema(description = "Email сотрудника", example = "employee@example.com")
  String employeeMail;

  @Schema(description = "Дата заявления", example = "2023-01-01")
  LocalDate date;
}