package ru.spbu.project.services;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;

import ru.spbu.project.models.dto.passResultDTO;
import ru.spbu.project.models.dto.ProductionPracticeDTO;
import ru.spbu.project.models.dto.TestDTO;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.exceptions.*;

public interface TrainingService {

  long applyForTraining(TrainingApplicationDTO applicationDTO) throws ExistingEmailException;

  void confirmTraining(Long employeeId, LocalDate date)
          throws TimeUpException, DifferentStageException, ActionInPastException;

  void refuseTraining(Long employeeId, String reason, LocalDate date)
          throws DifferentStageException, TimeUpException, ActionInPastException;

  boolean takeEntryTest(Long employeeId, TestDTO testDTO)
          throws TimeUpException, DifferentStageException, TestTypeException, ActionInPastException;

  boolean takeModuleTest(Long employeeId, TestDTO moduleTest)
          throws TimeUpException, DifferentStageException, TestTypeException, ActionInPastException;

  boolean takePracticeTask(Long employeeId, TestDTO practiceTask)
          throws TimeUpException, DifferentStageException, TestTypeException, ActionInPastException;

  void passingProductionPractice(ProductionPracticeDTO productionPracticeDTO)
      throws ActionInPastException, DifferentStageException;

  boolean takeExam(Long employeeId, passResultDTO result) throws ActionInPastException, DifferentStageException;

  HashMap<Stage, Integer> getFromPeriod(LocalDate startTime, LocalDate endTime);

  boolean productionPracticeResult(Long employeeId, passResultDTO result)
      throws IllegalArgumentException, ActionInPastException, DifferentStageException;

  Boolean deleteEmployeeById(Long employeeID);

  Integer sendMessage(List<String> emails, String message);
}
