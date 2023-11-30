package ru.spbu.project.services;

import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.exceptions.TimeUpException;
import java.time.LocalDate;

public interface TrainingService {
  long applyForTraining(TrainingApplicationDTO applicationDTO);
  void confirmTraining(Long employeeID, LocalDate date) throws TimeUpException;
  void refuseTraining(Long employeeID, LocalDate date) throws TimeUpException;
}
