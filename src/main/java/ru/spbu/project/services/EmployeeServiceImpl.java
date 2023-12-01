package ru.spbu.project.services;

import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.Test;
import ru.spbu.project.models.enums.TestType;
import ru.spbu.project.repositories.EmployeeRepository;
import ru.spbu.project.repositories.TestRepository;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{
  
  final EmployeeRepository employeeRepository;
  final TestRepository testRepository;

  public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                             TestRepository testRepository) {
    this.employeeRepository = employeeRepository;
    this.testRepository = testRepository;
  }

  public Employee findEmployeeByID(Long employeeID) {
    return employeeRepository.findById(employeeID).orElseThrow(
            () -> new IllegalArgumentException("There is no employee with id: " + employeeID)
    );
  }
  public boolean checkProgress(Employee employee) {
    return (passedTest(employee, TestType.MODULE_1) &&
            passedTest(employee, TestType.MODULE_2) &&
            passedTest(employee, TestType.MODULE_3) &&
            passedTest(employee, TestType.MODULE_4) &&
            passedTest(employee, TestType.MODULE_5) &&
            passedTest(employee, TestType.MODULE_6) &&
            passedTest(employee, TestType.MODULE_7) &&
            passedTest(employee, TestType.MODULE_8) &&
            passedTest(employee, TestType.PRACTICE_TASK_1) &&
            passedTest(employee, TestType.PRACTICE_TASK_2));
  }

  private boolean passedTest(Employee employee, TestType testType) {
    List<Test> tests = testRepository.employeeFindTest(employee, testType);
    for (Test test : tests) {
      if (test.getScorePercent() >= 0.8) {
        return true;
      }
    }
    return false;
  }
}
