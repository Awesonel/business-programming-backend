package ru.spbu.project.models.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spbu.project.models.Leader;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductionPracticeDTO {
  Long employeeId;
  Leader leader;
  String project;
  LocalDate date;
}
