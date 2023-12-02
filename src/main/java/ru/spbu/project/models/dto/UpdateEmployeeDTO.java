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
  private String job;
  private String project;
  private String purpose;
  private Stage stage;
  private Long leader;
  private LocalDate start;
  private String reason;
  private boolean isActive;
}
