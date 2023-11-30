package ru.spbu.project.services;

import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.Leader;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.exceptions.DifferentStageException;
import ru.spbu.project.models.exceptions.TimeUpException;
import ru.spbu.project.repositories.EmployeeRepository;
import ru.spbu.project.repositories.LeaderRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
  final LeaderRepository leaderRepository;
  final EmployeeRepository employeeRepository;
  final EmployeeService employeeService;
  static final int INVITATION_TIME_LIMIT = 3;

  public TrainingServiceImpl(LeaderRepository leaderRepository, EmployeeRepository employeeRepository, EmployeeService employeeService) {
    this.leaderRepository = leaderRepository;
    this.employeeRepository = employeeRepository;
    this.employeeService = employeeService;
  }


  @Override
  public long applyForTraining(TrainingApplicationDTO applicationDTO) {
    List<Leader> leaders = leaderRepository.findByData(
            applicationDTO.getLeaderSurname(),
            applicationDTO.getLeaderName(),
            applicationDTO.getLeaderPatronymic(),
            applicationDTO.getLeaderJobTitle());
    Leader leader;
    if (leaders.isEmpty()) {
      leader = new Leader(applicationDTO.getLeaderName(), applicationDTO.getLeaderSurname(),
              applicationDTO.getLeaderPatronymic(), applicationDTO.getLeaderJobTitle());
      leaderRepository.save(leader);
    } else {
      leader = leaders.get(0);
    }
    Employee employee = new Employee(applicationDTO.getEmployeeName(), applicationDTO.getEmployeeSurname(),
            applicationDTO.getEmployeePatronymic(), applicationDTO.getEmployeeJobTitle(),
            applicationDTO.getProject(), applicationDTO.getTrainingPurpose(), leader);
    employee.setStartTime(applicationDTO.getDate());
    employeeRepository.save(employee);
    return employee.getId();
  }

  @Override
  public void confirmTraining(Long employeeId, LocalDate date) throws TimeUpException, DifferentStageException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    checkInvitationTime(employee, date);
    if (!employee.getStage().equals(Stage.WAITING_APPLICATION_TRAINING)) {
      throw new DifferentStageException("The employee is at a different stage. Current stage: " + employee.getStage());
    }
    employee.setStage(Stage.PASSES_ENTRANCE_TEST);
    employee.setStartTime(LocalDate.now());
    employeeRepository.save(employee);
  }

  @Override
  public void refuseTraining(Long employeeID, String reason, LocalDate date) throws DifferentStageException, TimeUpException {
    Employee employee = employeeService.findEmployeeByID(employeeID);
    checkInvitationTime(employee, date);
    if (!employee.getStage().equals(Stage.WAITING_APPLICATION_TRAINING)) {
      throw new DifferentStageException("The employee is at a different stage. Current stage: " + employee.getStage());
    }
    employee.setActive(false);
    employee.setReasonForRefuseTraining(reason);
    employee.setStage(Stage.REFUSAL_APPLICATION);
    employeeRepository.save(employee);
  }

  private void checkInvitationTime(Employee employee, LocalDate currentDate) throws TimeUpException {
    long timePassed = ChronoUnit.DAYS.between(employee.getStartTime(), currentDate);
    if (timePassed > INVITATION_TIME_LIMIT) {
      employee.setActive(false);
      employeeRepository.save(employee);
      throw new TimeUpException("It is possible to answer application in " + INVITATION_TIME_LIMIT
              + " days," + " but " + timePassed + " days were passed");
    }
  }
}

