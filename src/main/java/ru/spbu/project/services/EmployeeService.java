package ru.spbu.project.services;

import ru.spbu.project.models.Employee;

public interface EmployeeService {
  
  Employee findEmployeeByID(Long employeeID);

  Boolean checkEmployeeExistenceByEmail(String email);

  Employee getEmployeeByEmail(String email);
}
