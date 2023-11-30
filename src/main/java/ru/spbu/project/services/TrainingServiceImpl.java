package ru.spbu.project.services;

import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.Leader;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.exceptions.TimeUpException;
import ru.spbu.project.repositories.EmployeeRepository;
import ru.spbu.project.repositories.LeaderRepository;

import javax.naming.TimeLimitExceededException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
  final LeaderRepository leaderRepository;
  final EmployeeRepository employeeRepository;
  final EmployeeService employeeService;
  private double invitationTimeLimit;

  public TrainingServiceImpl(LeaderRepository leaderRepository, EmployeeRepository employeeRepository, EmployeeService employeeService) {
    this.leaderRepository = leaderRepository;
    this.employeeRepository = employeeRepository;
    this.employeeService = employeeService;
  }


  @Override
  public long applyForTraining(TrainingApplicationDTO applicationDTO) {
    List<Leader> leaders = leaderRepository.findByData(
            applicationDTO.getLeaderName(),
            applicationDTO.getLeaderSurname(),
            applicationDTO.getLeaderPatronymic(),
            applicationDTO.getLeaderJobTitle());
    if (leaders.isEmpty()) {
      Leader leader = new Leader(applicationDTO.getLeaderName(), applicationDTO.getLeaderSurname(),
              applicationDTO.getLeaderPatronymic(), applicationDTO.getLeaderJobTitle());
      leaderRepository.save(leader);
    }
    Leader leader = leaders.get(0);
    Employee employee = new Employee(applicationDTO.getEmployeeName(), applicationDTO.getEmployeeSurname(),
            applicationDTO.getEmployeePatronymic(), applicationDTO.getEmployeeJobTitle(),
            applicationDTO.getProject(), applicationDTO.getTrainingPurpose(), leader);
    employee.setStartTime(applicationDTO.getDate());
    employeeRepository.save(employee);
    return employee.getId();
  }

  @Override
  public void confirmTraining(Long employeeId, LocalDate date) throws TimeUpException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    checkInvitationTime(employee.getStartTime(), date);
    if (!employee.getStage().equals(Stage.WAITING_APPLICATION_TRAINING)) {
      throw new IllegalArgumentException("The employee is at a different stage. Current stage: " + employee.getStage());
    }
    employee.setStage(Stage.PASSES_ENTRANCE_TEST);
    employee.setStartTime(LocalDate.now());
    employeeRepository.save(employee);
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

  private void checkInvitationTime(LocalDate startTime, LocalDate currentDate) throws TimeUpException {
    long timePassed = ChronoUnit.DAYS.between(currentDate, startTime);
    if (timePassed > invitationTimeLimit) {
      throw new TimeUpException("It is possible to answer application in " + invitationTimeLimit
              + " days," + " but " + timePassed + " days were passed");
    }
  }
}

