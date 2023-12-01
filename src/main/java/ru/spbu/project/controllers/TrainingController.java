package ru.spbu.project.controllers;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spbu.project.models.dto.ConfirmApplicationDTO;
import ru.spbu.project.models.dto.RefuseApplicationDTO;
import ru.spbu.project.models.dto.TestDTO;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.exceptions.DifferentStageException;
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
  public ResponseEntity<Long> submitApplication(@RequestBody TrainingApplicationDTO request) {
    long id = trainingService.applyForTraining(request);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }

  @PostMapping("/confirm-participation")
  public ResponseEntity<String> confirmParticipation(
      @RequestBody ConfirmApplicationDTO confirmation)
      throws TimeUpException, DifferentStageException {
    trainingService.confirmTraining(confirmation.getEmployeeId(), confirmation.getDate());
    return new ResponseEntity<>(String.valueOf(Stage.PASSES_ENTRANCE_TEST),
        HttpStatus.OK);
  }

  @PostMapping("/refuse-participation")
  public ResponseEntity<String> refuseParticipation(@RequestBody RefuseApplicationDTO rejection)
      throws DifferentStageException, TimeUpException {
    trainingService.refuseTraining(rejection.getId(), rejection.getReason(), rejection.getDate());
    return new ResponseEntity<>(rejection.getReason(), HttpStatus.OK);
  }

  @PostMapping("/take-entrance-test/{employeeId}")
  public ResponseEntity<String> takeEntryTest(@PathVariable Long employeeId,
      @RequestBody TestDTO testDTO)
      throws DifferentStageException, TestTypeException, TimeUpException {
    if (trainingService.takeEntryTest(employeeId, testDTO)) {
      return new ResponseEntity<>(String.valueOf(Stage.STUDYING), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(String.valueOf(Stage.FAILED_ENTRANCE_TEST), HttpStatus.OK);
    }
  }

  @PostMapping("/take-module-test/{employeeId}")
  public ResponseEntity<String> takeModuleTest(@PathVariable Long employeeId,
      @RequestBody TestDTO moduleTest)
      throws TimeUpException, DifferentStageException, TestTypeException {
    if (trainingService.takeModuleTest(employeeId, moduleTest)) {
      return new ResponseEntity<>("Module test passed.", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Module test failed.", HttpStatus.OK);
    }
  }

  @PostMapping("/take-practice-task/{employeeId}")
  public ResponseEntity<String> takePracticeTask(@PathVariable Long employeeId,
      @RequestBody TestDTO practiceTask)
      throws TimeUpException, DifferentStageException, TestTypeException {
    if (trainingService.takePracticeTask(employeeId, practiceTask)) {
      return new ResponseEntity<>("Practice task passed.", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Practice task failed.", HttpStatus.OK);
    }
  }

  @ExceptionHandler(TimeUpException.class)
  public ResponseEntity<ErrorMessage> timeUpExceptionHandler(TimeUpException exception) {
    return ResponseEntity.status(HttpStatus.OK).body(new ErrorMessage(exception.getMessage()));
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
}
