package ru.spbu.project.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spbu.project.models.Leader;
import ru.spbu.project.models.dto.LeaderDTO;
import ru.spbu.project.repositories.LeaderRepository;
import java.util.List;

@Tag(name="Leader Controller", description="Управление руководителями.")
@RestController
@RequestMapping("/api/leaders")
public class LeaderController {

  public final LeaderRepository leaderRepository;

  public LeaderController(LeaderRepository leaderRepository) {
    this.leaderRepository = leaderRepository;
  }

  @Operation(
          summary = "Поиск руководителя.",
          description = "Метод, позволяющий найти руководителя по имени.",
          parameters = {
                  @Parameter(name = "search", description = "Поиск по имени руководителя", required = false)
          }
  )
  @GetMapping("")
  public ResponseEntity<List<Leader>> getLeaders(@RequestParam(required = false) String search) {
    List<Leader> leaders;
    boolean searchNotEmpty = (search != null);

    if (searchNotEmpty) leaders = leaderRepository.searchByName(search);
    else leaders = leaderRepository.findAll();

    return new ResponseEntity<>(leaders, HttpStatus.OK);
  }

  @Operation(
          summary = "Создание руководителя.",
          description = "Метод, позволяющий создать руководителя.",
          parameters = {
                  @Parameter(name = "leaderDTO", description = "Данные для создания руководителя", required = true)
          }
  )
  @PostMapping("")
  public ResponseEntity<Long> createLeader(@RequestBody LeaderDTO leaderDTO) {
    Leader leader = new Leader(leaderDTO.getName(), leaderDTO.getSurname(), leaderDTO.getPatronymic(), leaderDTO.getJob());
    leaderRepository.save(leader);
    return new ResponseEntity<>(leader.getId(), HttpStatus.OK);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorMessage> illegalArgumentException(IllegalArgumentException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorMessage("There is no employee/leader with this id."));
  }
}