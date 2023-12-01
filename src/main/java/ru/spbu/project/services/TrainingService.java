package ru.spbu.project.services;

import java.time.LocalDate;

import ru.spbu.project.models.dto.TestDTO;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.exceptions.DifferentStageException;
import ru.spbu.project.models.exceptions.TestTypeException;
import ru.spbu.project.models.exceptions.TimeUpException;

public interface TrainingService {
  long applyForTraining(TrainingApplicationDTO applicationDTO);

  void confirmTraining(Long employeeId, LocalDate date) throws TimeUpException, DifferentStageException;

  void refuseTraining(Long employeeId, String reason, LocalDate date) throws DifferentStageException, TimeUpException;

  boolean entryTest(Long employeeId, TestDTO testDTO)
          throws TimeUpException, DifferentStageException, TestTypeException;

  void takeModuleTest(Long employeeId, TestDTO moduleTest)
          throws TimeUpException, DifferentStageException, TestTypeException;

  void takePracticeTask(Long employeeId, TestDTO practiceTask)
          throws TimeUpException, DifferentStageException, TestTypeException;
}
