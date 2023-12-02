package ru.spbu.project.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spbu.project.models.enums.Stage;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeDTO {
  private String name;
  private String surname;
  private String patronymic;
  private String jobTitle;
  private String project;
  private String trainingPurpose;
  private Stage stage;
  private Long leaderId;
  private LocalDate startTime;
  private String reasonForRefuseTraining;
  private boolean isActive;
}
