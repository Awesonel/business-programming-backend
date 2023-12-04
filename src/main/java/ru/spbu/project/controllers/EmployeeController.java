package ru.spbu.project.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.Leader;
import ru.spbu.project.models.dto.UpdateEmployeeDTO;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.repositories.EmployeeRepository;
import ru.spbu.project.repositories.LeaderRepository;
import ru.spbu.project.services.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeRepository employeeRepository;
  private final EmployeeService employeeService;
  private final LeaderRepository leaderRepository;

  public EmployeeController(EmployeeRepository employeeRepository, EmployeeService employeeService, LeaderRepository leaderRepository) {
    this.employeeRepository = employeeRepository;
    this.employeeService = employeeService;
    this.leaderRepository = leaderRepository;
  }

  @GetMapping("")
  public ResponseEntity<List<Employee>> getParticipants(
          @RequestParam(required = false) List<Stage> stages,
          @RequestParam(required = false) String search) {
    List<Employee> employeeList;

    boolean stagesNotEmpty = (stages != null && !stages.isEmpty());
    boolean searchNotEmpty = (search != null);

    if (stagesNotEmpty && searchNotEmpty) {
      employeeList = new ArrayList<>();
      for (Stage stage: stages) {
        employeeList.addAll(employeeRepository.findByStageAndName(search, stage));
      }
    } else if (searchNotEmpty) {
      employeeList = employeeRepository.searchByName(search);
    } else if (stagesNotEmpty) {
      employeeList = new ArrayList<>();
      for (Stage stage: stages) {
        employeeList.addAll(employeeRepository.findByStage(stage));
      }
    } else {
      employeeList = employeeRepository.findAll();
    }
    return new ResponseEntity<>(employeeList, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Employee> changeEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeDTO updateInfo) {
    Employee employee = employeeService.findEmployeeByID(id);
    Leader leader = leaderRepository.findById(updateInfo.getLeader()).orElseThrow(() -> new IllegalArgumentException("There is no leader with id: " + updateInfo.getLeader()));
    employee.setName(updateInfo.getName());
    employee.setSurname(updateInfo.getSurname());
    employee.setPatronymic(updateInfo.getPatronymic());
    employee.setJobTitle(updateInfo.getJob());
    employee.setProject(updateInfo.getProject());
    employee.setTrainingPurpose(updateInfo.getPurpose());
    employee.setStage(updateInfo.getStage());
    employee.setLeader(leader);
    employee.setStartTime(updateInfo.getStart());
    employee.setReasonForRefuseTraining(updateInfo.getReason());
    employee.setIsActive(updateInfo.isActive());
    employeeRepository.save(employee);
    return new ResponseEntity<>(employee, HttpStatus.valueOf(204));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> findEmployeeByID(@PathVariable Long id) {
    Employee employee = employeeService.findEmployeeByID(id);
    return new ResponseEntity<>(employee, HttpStatus.OK);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorMessage> illegalArgumentException(IllegalArgumentException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorMessage("There is no employee/leader with this id."));
  }
}

