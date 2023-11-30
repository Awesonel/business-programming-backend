package ru.spbu.project.models;

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
  private Long id;

  @ManyToOne
  private Employee employee;

  private TestType testType;
  private Double scorePercent;
  private LocalDate date;

  public Test(Employee employee, TestType testType, Double scorePercent, LocalDate date) {
    this.employee = employee;
    this.testType = testType;
    this.scorePercent = scorePercent;
    this.date = date;
  }
}
