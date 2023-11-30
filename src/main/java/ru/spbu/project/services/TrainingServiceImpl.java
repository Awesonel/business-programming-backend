package ru.spbu.project.services;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.exceptions.StageDifferent;
import ru.spbu.project.models.exceptions.TimeUpException;
import ru.spbu.project.repositories.EmployeeRepository;

@Service
public class TrainingServiceImpl implements  TrainingService{
  private final EmployeeService employeeService;
  private final EmployeeRepository employeeRepository;

  public TrainingServiceImpl(EmployeeService employeeService, EmployeeRepository employeeRepository) {
    this.employeeService = employeeService;
    this.employeeRepository = employeeRepository;
  }


  @Override
  public long applyForTraining(TrainingApplicationDTO applicationDTO) {
    return 0;
  }

  @Override
  public void confirmTraining(Long employeeID, LocalDate date) throws TimeUpException {

  }

  @Override
  public void refuseTraining(Long employeeID, String reason, LocalDate date) throws StageDifferent {
    // TODO: 30.11.2023 Проверка времени!!!
    Employee employee = employeeService.findEmployeeByID(employeeID);
    if (!employee.getStage().equals(Stage.WAITING_APPLICATION_TRAINING)) {
      throw new StageDifferent("The employee is at a different stage. Current stage: " + employee.getStage());
    }
    employee.setStage(Stage.PASSES_ENTRANCE_TEST);
    employeeRepository.save(employee);
  }
}

