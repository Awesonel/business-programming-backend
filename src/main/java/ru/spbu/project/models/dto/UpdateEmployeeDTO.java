package ru.spbu.project.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spbu.project.models.enums.Stage;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeDTO {
  @Schema(description = "Имя сотрудника", example = "Иван")
  private String name;

  @Schema(description = "Фамилия сотрудника", example = "Иванов")
  private String surname;

  @Schema(description = "Отчество сотрудника", example = "Иванович")
  private String patronymic;

  @Schema(description = "Должность сотрудника", example = "Разработчик")
  private String job;

  @Schema(description = "Проект, в котором участвует сотрудник", example = "Проект ABC")
  private String project;

  @Schema(description = "Цель обучения сотрудника", example = "Повышение квалификации")
  private String purpose;

  @Schema(description = "Этап обучения сотрудника", example = "WAITING_APPLICATION_TRAINING")
  private Stage stage;

  @Schema(description = "Идентификатор руководителя", example = "1")
  private Long leader;

  @Schema(description = "Дата начала обучения", example = "2023-01-01")
  private LocalDate start;

  @Schema(description = "Причина отказа от обучения", example = "Недостаток времени")
  private String reason;

  @Email
  @Schema(description = "Email сотрудника", example = "employee@example.com")
  private String email;

  @Schema(description = "Активность сотрудника", example = "true")
  private boolean isActive;
}