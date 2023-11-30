package ru.spbu.project.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectApplicationDTO {
  Long id;
  String reason;
  LocalDate date;
}
