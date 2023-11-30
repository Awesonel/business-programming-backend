package ru.spbu.project.services;

import java.time.LocalDate;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.exceptions.StageDifferent;
import ru.spbu.project.models.exceptions.TimeUpException;

public interface TrainingService {
  long applyForTraining(TrainingApplicationDTO applicationDTO);
  void confirmTraining(Long employeeID, LocalDate date) throws TimeUpException;
  void refuseTraining(Long employeeID, String reason, LocalDate date) throws StageDifferent;
}
