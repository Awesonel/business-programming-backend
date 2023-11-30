package ru.spbu.project.models;

import jakarta.persistence.*;
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
  private Long id;
  private String name;
  private String surname;
  private String patronymic;
  private String jobTitle;
  private String project;
  private String trainingPurpose;
  private Stage stage;
  @ManyToOne
  private Leader leader;
  private LocalDate startTime;
  private String reasonForRefuseTraining;
  private boolean isActive;

}
