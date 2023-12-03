package ru.spbu.project.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderDTO {

  private String name;
  private String surname;
  private String patronymic;
  private String job;
}
