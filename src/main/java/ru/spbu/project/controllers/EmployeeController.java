package ru.spbu.project.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.repositories.EmployeeRepository;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

  private final EmployeeRepository employeeRepository;

  public EmployeeController(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @GetMapping("/all")
  public ResponseEntity<List<Employee>> getParticipants() {
    List<Employee> employeeList = employeeRepository.findAll();
    return new ResponseEntity<>(employeeList, HttpStatus.OK);
  }

  @GetMapping("/byStage")
  public ResponseEntity<List<Employee>> getAllByStage(@RequestParam Stage stage) {
    List<Employee> employeeList = employeeRepository.findByStage(stage);
    return new ResponseEntity<>(employeeList, HttpStatus.OK);
  }

  @PutMapping("/changeEmployee")
  public ResponseEntity<Employee> changeEmployee(@RequestBody Employee employee) {
    employeeRepository.save(employee);
    return new ResponseEntity<>(employee, HttpStatus.OK);
  }
}

