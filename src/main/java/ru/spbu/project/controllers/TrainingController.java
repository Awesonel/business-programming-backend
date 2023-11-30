package ru.spbu.project.controllers;

import java.time.LocalDate;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.exceptions.StageDifferent;
import ru.spbu.project.models.exceptions.TimeUpException;
import ru.spbu.project.services.TrainingService;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

  private final TrainingService trainingService;

  public TrainingController(TrainingService tS) {
    this.trainingService = tS;
  }

  @PostMapping("/submitApplication")
  public ResponseEntity<Long> submitApplication(@RequestBody TrainingApplicationDTO request) {
    long id = trainingService.applyForTraining(request);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }

  @PostMapping("/confirmParticipation/{employeeId}")
  public ResponseEntity<String> confirmParticipation(@PathVariable Long employeeId, @RequestBody
  LocalDate localDate)
      throws TimeUpException {
    trainingService.confirmTraining(employeeId, localDate);
    return new ResponseEntity<>(String.valueOf(Stage.PASSES_ENTRANCE_TEST),
        HttpStatus.OK);
  }

  @PostMapping("/refuseParticipation/{employeeId}")
  public ResponseEntity<String> refuseParticipation(@PathVariable Long employeeId,
      @RequestBody String reason, @RequestBody LocalDate localDate)
      throws StageDifferent {
    trainingService.refuseTraining(employeeId, reason, localDate);
    return new ResponseEntity<>(reason, HttpStatus.OK);
  }

  @ExceptionHandler(TimeUpException.class)
  public ResponseEntity<ErrorMessage> timeUp(TimeUpException exception) {
    return ResponseEntity.status(HttpStatus.OK).body(new ErrorMessage(exception.getMessage()));
  }

  @ExceptionHandler(StageDifferent.class)
  public ResponseEntity<ErrorMessage> stageDifferent(StageDifferent exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMessage(exception.getMessage()));
  }
}
