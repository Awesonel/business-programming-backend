package ru.spbu.project.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "production-practice")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionPractice {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Schema(description = "Идентификатор производственной практики", example = "1")
  private Long id;

  @ManyToOne
  @Schema(description = "Руководитель практики")
  private Leader leader;

  @OneToOne
  @Schema(description = "Сотрудник, участвующий в практике")
  private Employee employee;

  @Schema(description = "Проект, связанный с практикой", example = "Проект XYZ")
  private String project;

  @Schema(description = "Результат практики", example = "true")
  private Boolean result;

  public ProductionPractice(Leader leader, Employee employee, String project) {
    this.leader = leader;
    this.employee = employee;
    this.project = project;
    this.result = false;
  }
}