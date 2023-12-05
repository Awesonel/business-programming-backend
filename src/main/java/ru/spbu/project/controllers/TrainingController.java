package ru.spbu.project.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spbu.project.models.dto.*;
import ru.spbu.project.models.enums.ReturnType;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.exceptions.*;
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
  public ResponseEntity<ReturnType> confirmParticipation(
      @RequestBody ConfirmApplicationDTO confirmation)
          throws TimeUpException, DifferentStageException, ActionInPastException {
    trainingService.confirmTraining(confirmation.getEmployeeId(), confirmation.getDate());
    return new ResponseEntity<>(ReturnType.SUCCESS, HttpStatus.OK);
  }

  @PostMapping("/refuse-participation")
  public ResponseEntity<ReturnType> refuseParticipation(@RequestBody RefuseApplicationDTO rejection)
          throws DifferentStageException, TimeUpException, ActionInPastException {
    trainingService.refuseTraining(rejection.getId(), rejection.getReason(), rejection.getDate());
    return new ResponseEntity<>(ReturnType.SUCCESS, HttpStatus.OK);
  }

  @PostMapping("/take-entrance-test/{employeeId}")
  public ResponseEntity<ReturnType> takeEntryTest(@PathVariable Long employeeId,
      @RequestBody TestDTO testDTO)
          throws DifferentStageException, TestTypeException, TimeUpException, ActionInPastException {
    if (trainingService.takeEntryTest(employeeId, testDTO)) {
      return new ResponseEntity<>(ReturnType.SUCCESS, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(ReturnType.FAIL, HttpStatus.OK);
    }
  }

  @PostMapping("/take-module-test/{employeeId}")
  public ResponseEntity<ReturnType> takeModuleTest(@PathVariable Long employeeId,
      @RequestBody TestDTO moduleTest)
          throws TimeUpException, DifferentStageException, TestTypeException, ActionInPastException {
    if (trainingService.takeModuleTest(employeeId, moduleTest)) {
      return new ResponseEntity<>(ReturnType.SUCCESS, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(ReturnType.FAIL, HttpStatus.OK);
    }
  }

  @PostMapping("/take-practice-task/{employeeId}")
  public ResponseEntity<ReturnType> takePracticeTask(@PathVariable Long employeeId,
      @RequestBody TestDTO practiceTask)
          throws TimeUpException, DifferentStageException, TestTypeException, ActionInPastException {
    if (trainingService.takePracticeTask(employeeId, practiceTask)) {
      return new ResponseEntity<>(ReturnType.SUCCESS, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(ReturnType.FAIL, HttpStatus.OK);
    }
  }

  @PostMapping("/send-to-production-practice")
  public ResponseEntity<ReturnType> passingProductionPractice(
      @RequestBody ProductionPracticeDTO productionPracticeDTO)
      throws ActionInPastException, DifferentStageException {
    trainingService.passingProductionPractice(productionPracticeDTO);
    return new ResponseEntity<>(
        ReturnType.SUCCESS,
        HttpStatus.OK);
  }

  @PostMapping("/take-exam/{employeeId}")
  public ResponseEntity<ReturnType> takeExam(@PathVariable Long employeeId, @RequestBody passResultDTO result)
      throws ActionInPastException, DifferentStageException {
    if (trainingService.takeExam(employeeId, result)) {
      return new ResponseEntity<>(ReturnType.SUCCESS, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(ReturnType.FAIL, HttpStatus.OK);
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
  public ResponseEntity<ReturnType> productionPracticeResult(@PathVariable Long employeeId,
      @RequestBody passResultDTO result)
      throws IllegalArgumentException, ActionInPastException, DifferentStageException {
    if (trainingService.productionPracticeResult(employeeId, result)) {
      return new ResponseEntity<>(ReturnType.SUCCESS, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(ReturnType.FAIL, HttpStatus.OK);
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
  public ResponseEntity<ReturnType> timeUpExceptionHandler(TimeUpException exception) {
    return new ResponseEntity<>(ReturnType.TIME_UP, HttpStatus.OK);
  }

  @ExceptionHandler(ActionInPastException.class)
  public ResponseEntity<ReturnType> actionInPastHandler(ActionInPastException exception) {
    return new ResponseEntity<>(ReturnType.PAST_ACTION, HttpStatus.OK);
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
