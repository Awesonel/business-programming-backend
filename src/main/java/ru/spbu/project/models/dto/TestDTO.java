package ru.spbu.project.models.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.spbu.project.models.enums.TestType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDTO {
  Double score;
  TestType testType;
  LocalDate date;
}
