package ru.spbu.project.services;

import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.exceptions.TimeUpException;

public interface TrainingService {
  long applyForTraining(TrainingApplicationDTO applicationDTO);
  void confirmTraining(Long employeeID) throws TimeUpException;
  void refuseTraining(Long employeeID) throws TimeUpException;
}
