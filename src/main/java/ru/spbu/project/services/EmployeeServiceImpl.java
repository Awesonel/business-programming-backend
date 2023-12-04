package ru.spbu.project.services;

import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.repositories.EmployeeRepository;

import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {
  
  final EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public Employee findEmployeeByID (Long employeeID) {
    return employeeRepository.findById(employeeID).orElseThrow(
            () -> new IllegalArgumentException("There is no employee with id: " + employeeID)
    );
  }

  public Boolean checkEmployeeExistenceByEmail(String email) {
    return employeeRepository.findByEmail(email).isPresent();
  }

 public Employee getEmployeeByEmail(String email) {
    return employeeRepository.findByEmail(email).orElseThrow(
            () -> new IllegalArgumentException("There is no employee with email: " + email)
    );
 }
}
