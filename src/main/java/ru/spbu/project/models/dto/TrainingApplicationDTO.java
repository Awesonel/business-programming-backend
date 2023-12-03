package ru.spbu.project.models.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingApplicationDTO {
  Long leaderId;
  String project;
  String employeeName;
  String employeeSurname;
  String employeePatronymic;
  String employeeJobTitle;
  String trainingPurpose;
  LocalDate date;
}
