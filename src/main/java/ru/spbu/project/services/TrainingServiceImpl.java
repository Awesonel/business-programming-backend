package ru.spbu.project.services;

import org.springframework.stereotype.Service;
import ru.spbu.project.models.Employee;
import ru.spbu.project.models.Leader;
import ru.spbu.project.models.Test;
import ru.spbu.project.models.dto.TestDTO;
import ru.spbu.project.models.dto.TrainingApplicationDTO;
import ru.spbu.project.models.enums.Stage;
import ru.spbu.project.models.enums.TestType;
import ru.spbu.project.models.exceptions.DifferentStageException;
import ru.spbu.project.models.exceptions.TestTypeException;
import ru.spbu.project.models.exceptions.TimeUpException;
import ru.spbu.project.repositories.EmployeeRepository;
import ru.spbu.project.repositories.LeaderRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import ru.spbu.project.repositories.TestRepository;

@Service
public class TrainingServiceImpl implements TrainingService {

  final LeaderRepository leaderRepository;
  final EmployeeRepository employeeRepository;
  final EmployeeService employeeService;
  final TestRepository testRepository;
  static final int INVITATION_TIME_LIMIT = 3;
  static final int ENTRY_TEST_TIME_LIMIT = 5;

  public TrainingServiceImpl(LeaderRepository leaderRepository,
      EmployeeRepository employeeRepository, EmployeeService employeeService, TestRepository testRepository) {
    this.leaderRepository = leaderRepository;
    this.employeeRepository = employeeRepository;
    this.employeeService = employeeService;
    this.testRepository = testRepository;
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
    Employee employee = new Employee(applicationDTO.getEmployeeName(),
        applicationDTO.getEmployeeSurname(),
        applicationDTO.getEmployeePatronymic(), applicationDTO.getEmployeeJobTitle(),
        applicationDTO.getProject(), applicationDTO.getTrainingPurpose(), leader);
    employee.setStartTime(applicationDTO.getDate());
    employeeRepository.save(employee);
    return employee.getId();
  }

  @Override
  public void confirmTraining(Long employeeId, LocalDate date)
      throws TimeUpException, DifferentStageException {
    Employee employee = employeeService.findEmployeeByID(employeeId);
    checkInvitationTime(employee, date);
    if (!employee.getStage().equals(Stage.WAITING_APPLICATION_TRAINING)) {
      throw new DifferentStageException(
          "The employee is at a different stage. Current stage: " + employee.getStage());
    }
    employee.setStage(Stage.PASSES_ENTRANCE_TEST);
    employee.setStartTime(date);
    employeeRepository.save(employee);
  }

  @Override
  public void refuseTraining(Long employeeID, String reason, LocalDate date)
      throws DifferentStageException, TimeUpException {
    Employee employee = employeeService.findEmployeeByID(employeeID);
    checkInvitationTime(employee, date);
    if (!employee.getStage().equals(Stage.WAITING_APPLICATION_TRAINING)) {
      throw new DifferentStageException(
          "The employee is at a different stage. Current stage: " + employee.getStage());
    }
    employee.setActive(false);
    employee.setReasonForRefuseTraining(reason);
    employee.setStage(Stage.REFUSAL_APPLICATION);
    employeeRepository.save(employee);
  }

  private void checkInvitationTime(Employee employee, LocalDate currentDate)
      throws TimeUpException {
    long timePassed = ChronoUnit.DAYS.between(employee.getStartTime(), currentDate);
    if (timePassed > INVITATION_TIME_LIMIT) {
      employee.setStage(Stage.REFUSAL_APPLICATION);
      employee.setActive(false);
      employeeRepository.save(employee);
      throw new TimeUpException("It is possible to answer application in " + INVITATION_TIME_LIMIT
          + " days," + " but " + timePassed + " days were passed");
    }
  }

  @Override
  public boolean takeEntryTest(Long employeeID, TestDTO testDTO)
      throws TimeUpException, DifferentStageException, TestTypeException {
    Employee employee = employeeService.findEmployeeByID(employeeID);
    if (!employee.getStage().equals(Stage.PASSES_ENTRANCE_TEST)) {
      throw new DifferentStageException("Employee can't pass this test");
    }
    if (!testDTO.getTestType().equals(TestType.ENTRY)) {
      throw new TestTypeException("Test type isn't ENTRY!");
    }
    checkEntryTestTime(employee, testDTO.getDate());
    Test test = new Test(employee, testDTO.getTestType(), testDTO.getScore() / 20,
        testDTO.getDate());
    if (test.getScorePercent() < 0.8) {
      employee.setStage(Stage.FAILED_ENTRANCE_TEST);
    } else {
      employee.setStage(Stage.STUDYING);
      employee.setStartTime(testDTO.getDate());
    }
    employeeRepository.save(employee);
    testRepository.save(test);
    return test.getScorePercent() >= 0.8;
  }

  @Override
  public boolean takeModuleTest(Long employeeId, TestDTO moduleTest) throws TimeUpException, DifferentStageException, TestTypeException {
    // TODO: 01.12.2023
    return true;
  }

  @Override
  public boolean takePracticeTask(Long employeeId, TestDTO practiceTask) throws TimeUpException, DifferentStageException, TestTypeException {
    // TODO: 01.12.2023
    return true;
  }

  private void checkEntryTestTime(Employee employee, LocalDate date) throws TimeUpException {
    long timePassed = ChronoUnit.DAYS.between(employee.getStartTime(), date);
    if (timePassed > ENTRY_TEST_TIME_LIMIT) {
      employee.setStage(Stage.FAILED_ENTRANCE_TEST);
      employee.setActive(false);
      employeeRepository.save(employee);
      throw new TimeUpException("It is possible to pass test in " + ENTRY_TEST_TIME_LIMIT
          + " days," + " but " + timePassed + " days were passed");
    }
  }
}

