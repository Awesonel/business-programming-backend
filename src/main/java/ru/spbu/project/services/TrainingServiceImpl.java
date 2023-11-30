package ru.spbu.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.exceptions.TimeUpException;

@Service
public class TrainingServiceImpl implements  TrainingService{
  private final EmployeeService employeeService;

  public TrainingServiceImpl(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }


  @Override
  public long applyForTraining(TrainingApplicationDTO applicationDTO) {
    return 0;
  }

  @Override
  public void confirmTraining(Long employeeID) throws TimeUpException {

  }

  @Override
  public void refuseTraining(Long employeeID, String reason) throws TimeUpException {
  }
}
