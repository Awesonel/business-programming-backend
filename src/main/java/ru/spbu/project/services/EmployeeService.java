package ru.spbu.project.services;

import ru.spbu.project.models.Employee;

public interface EmployeeService {
  
  Employee findEmployeeByID(Long employeeID);

}
