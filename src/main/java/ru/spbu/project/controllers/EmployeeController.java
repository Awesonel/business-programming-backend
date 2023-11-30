package ru.spbu.project.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.spbu.project.models.Employee;
import ru.spbu.project.repositories.EmployeeRepository;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

  private final EmployeeRepository employeeRepository;

  public EmployeeController(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  @GetMapping("/employees")
  public ResponseEntity<List<Employee>> getParticipants() {
    List<Employee> employeeList = employeeRepository.findAll();
    return new ResponseEntity<>(employeeList, HttpStatus.OK);
  }
}

