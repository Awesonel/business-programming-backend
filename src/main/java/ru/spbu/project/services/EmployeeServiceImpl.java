package ru.spbu.project.services;

import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.repositories.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{
  final EmployeeRepository employeeRepository;

  public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public Employee findEmployeeByID(Long employeeID) {
    return employeeRepository.findById(employeeID).orElseThrow(
        () -> new IllegalArgumentException("There is no employee with id: " + employeeID)
    );
  }

}
