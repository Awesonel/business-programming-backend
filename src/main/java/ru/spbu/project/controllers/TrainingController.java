package ru.spbu.project.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spbu.project.models.dto.*;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.exceptions.DifferentStageException;
import ru.spbu.project.models.exceptions.ExistingEmailException;
import ru.spbu.project.models.exceptions.TestTypeException;
import ru.spbu.project.models.exceptions.TimeUpException;
import ru.spbu.project.services.TrainingService;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

  private final TrainingService trainingService;

  public TrainingController(TrainingService tS) {
    this.trainingService = tS;
  }

  @PostMapping("/submit-application")
  public ResponseEntity<Long> submitApplication(@RequestBody TrainingApplicationDTO request)
          throws ExistingEmailException {
    long id = trainingService.applyForTraining(request);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }

  @PostMapping("/confirm-participation")
  public ResponseEntity<Boolean> confirmParticipation(
      @RequestBody ConfirmApplicationDTO confirmation)
      throws TimeUpException, DifferentStageException {
    trainingService.confirmTraining(confirmation.getEmployeeId(), confirmation.getDate());
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  @PostMapping("/refuse-participation")
  public ResponseEntity<Boolean> refuseParticipation(@RequestBody RefuseApplicationDTO rejection)
      throws DifferentStageException, TimeUpException {
    trainingService.refuseTraining(rejection.getId(), rejection.getReason(), rejection.getDate());
    return new ResponseEntity<>(true, HttpStatus.OK);
  }

  @PostMapping("/take-entrance-test/{employeeId}")
  public ResponseEntity<Boolean> takeEntryTest(@PathVariable Long employeeId,
      @RequestBody TestDTO testDTO)
      throws DifferentStageException, TestTypeException, TimeUpException {
    if (trainingService.takeEntryTest(employeeId, testDTO)) {
      return new ResponseEntity<>(true, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
  }

  @PostMapping("/take-module-test/{employeeId}")
  public ResponseEntity<Boolean> takeModuleTest(@PathVariable Long employeeId,
      @RequestBody TestDTO moduleTest)
      throws TimeUpException, DifferentStageException, TestTypeException {
    if (trainingService.takeModuleTest(employeeId, moduleTest)) {
      return new ResponseEntity<>(true, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
  }

  @PostMapping("/take-practice-task/{employeeId}")
  public ResponseEntity<Boolean> takePracticeTask(@PathVariable Long employeeId,
      @RequestBody TestDTO practiceTask)
      throws TimeUpException, DifferentStageException, TestTypeException {
    if (trainingService.takePracticeTask(employeeId, practiceTask)) {
      return new ResponseEntity<>(true, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
  }

  @PostMapping("/send-to-production-practice")
  public ResponseEntity<Boolean> passingProductionPractice(
      @RequestBody ProductionPracticeDTO productionPracticeDTO)
      throws TimeUpException, DifferentStageException {
    trainingService.passingProductionPractice(productionPracticeDTO);
    return new ResponseEntity<>(
        true,
        HttpStatus.OK);
  }

  @PostMapping("/take-exam/{employeeId}")
  public ResponseEntity<Boolean> takeExam(@PathVariable Long employeeId, @RequestBody passResultDTO result)
      throws TimeUpException, DifferentStageException {
    if (trainingService.takeExam(employeeId, result)) {
      return new ResponseEntity<>(true, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
  }

  @GetMapping("/get-from-period")
  public ResponseEntity<HashMap<Stage, Integer>> getFromPeriod(
      @RequestParam LocalDate startDateTime,
      @RequestParam LocalDate endDateTime) {
    HashMap<Stage, Integer> statistics = trainingService.getFromPeriod(startDateTime, endDateTime);
    return new ResponseEntity<>(statistics, HttpStatus.OK);
  }

  @PostMapping("/production-practice-result/{employeeId}")
  public ResponseEntity<Boolean> productionPracticeResult(@PathVariable Long employeeId,
      @RequestBody passResultDTO result)
      throws IllegalArgumentException, TimeUpException, DifferentStageException {
    if (trainingService.productionPracticeResult(employeeId, result)) {
      return new ResponseEntity<>(true, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(false, HttpStatus.OK);
    }
  }

  @DeleteMapping("/delete-employee/{id}")
  public ResponseEntity<Boolean> deleteEmployeeByID(@PathVariable Long id) {
    return new ResponseEntity<>(trainingService.deleteEmployeeById(id), HttpStatus.OK);
  }

  @PostMapping("/send-message")
  public ResponseEntity<Integer> sendMessages(@RequestParam  List<String> emails, @RequestParam String message) {
    return new ResponseEntity<>(trainingService.sendMessage(emails, message), HttpStatus.OK);
  }

  @ExceptionHandler(TimeUpException.class)
  public ResponseEntity<Boolean> timeUpExceptionHandler(TimeUpException exception) {
    return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DifferentStageException.class)
  public ResponseEntity<ErrorMessage> stageDifferentExceptionHandler(
      DifferentStageException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessage(exception.getMessage()));
  }

  @ExceptionHandler(TestTypeException.class)
  public ResponseEntity<ErrorMessage> testTypeExceptionHandler(TestTypeException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessage(exception.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorMessage> illegalArgumentException(IllegalArgumentException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessage("There is no employee/leader with this id."));
  }

  @ExceptionHandler(ExistingEmailException.class)
  public ResponseEntity<ErrorMessage> existingEmailException(ExistingEmailException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).
            body(new ErrorMessage(exception.getMessage()));
  }
}
