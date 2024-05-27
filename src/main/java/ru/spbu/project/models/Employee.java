package ru.spbu.project.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spbu.project.models.enums.Stage;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Schema(description = "Идентификатор сотрудника", example = "1")
  private Long id;

  @Schema(description = "Имя сотрудника", example = "Иван")
  private String name;

  @Schema(description = "Фамилия сотрудника", example = "Иванов")
  private String surname;

  @Schema(description = "Отчество сотрудника", example = "Иванович")
  private String patronymic;

  @Schema(description = "Должность сотрудника", example = "Разработчик")
  private String jobTitle;

  @Schema(description = "Проект, в котором участвует сотрудник", example = "Проект ABC")
  private String project;

  @Schema(description = "Цель обучения сотрудника", example = "Повышение квалификации")
  private String trainingPurpose;

  @Schema(description = "Этап обучения сотрудника", example = "WAITING_APPLICATION_TRAINING")
  private Stage stage;

  @ManyToOne
  @Schema(description = "Руководитель сотрудника")
  private Leader leader;

  @Schema(description = "Дата начала обучения", example = "2023-01-01")
  private LocalDate startTime;

  @Schema(description = "Причина отказа от обучения", example = "Недостаток времени")
  private String reasonForRefuseTraining;

  @Schema(description = "Активность сотрудника", example = "true")
  private Boolean isActive;

  @Email
  @Schema(description = "Email сотрудника", example = "employee@example.com")
  private String email;

  @Schema(description = "Результат экзамена", example = "true")
  private Boolean examResult;

  public Employee(String employeeName,
                  String employeeSurname,
                  String employeePatronymic,
                  String employeeJobTitle,
                  String project,
                  String trainingPurpose,
                  Leader leader) {
    this.name = employeeName;
    this.surname = employeeSurname;
    this.patronymic = employeePatronymic;
    this.jobTitle = employeeJobTitle;
    this.project = project;
    this.trainingPurpose = trainingPurpose;
    this.leader = leader;
    this.stage = Stage.WAITING_APPLICATION_TRAINING;
    this.isActive = true;
  }

  public Employee(String employeeName,
                  String employeeSurname,
                  String employeePatronymic,
                  String employeeJobTitle,
                  String project,
                  String trainingPurpose,
                  String email,
                  Leader leader) {
    this.name = employeeName;
    this.surname = employeeSurname;
    this.patronymic = employeePatronymic;
    this.jobTitle = employeeJobTitle;
    this.project = project;
    this.trainingPurpose = trainingPurpose;
    this.leader = leader;
    this.stage = Stage.WAITING_APPLICATION_TRAINING;
    this.isActive = true;
    this.email = email;
  }
}