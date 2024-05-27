package ru.spbu.project.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import ru.spbu.project.models.enums.TestType;

@Entity
@Table(name = "tests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Schema(description = "Идентификатор теста", example = "1")
  private Long id;

  @ManyToOne
  @Schema(description = "Сотрудник, прошедший тест")
  private Employee employee;

  @Schema(description = "Тип теста", example = "MODULE")
  private TestType testType;

  @Schema(description = "Процент результата теста", example = "85.5")
  private Double scorePercent;

  @Schema(description = "Дата прохождения теста", example = "2023-01-01")
  private LocalDate date;

  public Test(Employee employee, TestType testType, Double scorePercent, LocalDate date) {
    this.employee = employee;
    this.testType = testType;
    this.scorePercent = scorePercent;
    this.date = date;
  }
}