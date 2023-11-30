package ru.spbu.project.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingApplicationDTO {
  String leaderName;
  String leaderSurname;
  String leaderPatronymic;
  String leaderJobTitle;
  String project;
  String employeeName;
  String employeeSurname;
  String employeePatronymic;
  String employeeJobTitle;
  String trainingPurpose;
}
